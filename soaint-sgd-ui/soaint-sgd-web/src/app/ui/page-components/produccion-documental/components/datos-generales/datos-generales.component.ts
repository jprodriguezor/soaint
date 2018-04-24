import {
  ChangeDetectorRef,
  Component,
  Input,
  OnDestroy,
  OnInit,
  ViewChild,
  Output,
  EventEmitter,
  ChangeDetectionStrategy
} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {ProduccionDocumentalApiService} from 'app/infrastructure/api/produccion-documental.api';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {getAuthenticatedFuncionario} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {FuncionarioDTO} from 'app/domain/funcionarioDTO';
import {PdMessageService} from '../../providers/PdMessageService';
import {MessagingService} from 'app/shared/providers/MessagingService';
import {TareaDTO} from 'app/domain/tareaDTO';
import {VersionDocumentoDTO} from '../../models/DocumentoDTO';
import {AnexoDTO} from '../../models/DocumentoDTO';
import {Sandbox as DependenciaSandbox} from '../../../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {getActiveTask} from '../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import { StatusDTO } from '../../models/StatusDTO';
import {PushNotificationAction} from '../../../../../infrastructure/state-management/notifications-state/notifications-actions';
import {DocumentoEcmDTO} from '../../../../../domain/documentoEcmDTO';
import {FileUpload} from 'primeng/primeng';
import {DocumentDownloaded} from '../../events/DocumentDownloaded';
import {DocumentUploaded} from '../../events/DocumentUploaded';
import {TASK_PRODUCIR_DOCUMENTO} from "../../../../../infrastructure/state-management/tareasDTO-state/task-properties";

@Component({
  selector: 'pd-datos-generales',
  templateUrl: './datos-generales.component.html',
})

export class PDDatosGeneralesComponent implements OnInit, OnDestroy {

  form: FormGroup;

  validations: any = {};

  taskData: TareaDTO;

  funcionarioLog: FuncionarioDTO;

  tiposComunicacion$: Observable<ConstanteDTO[]>;
  tiposAnexo$: Observable<ConstanteDTO[]>;
  tiposPlantilla$: Observable<ConstanteDTO[]>;

  documentLoaded = false;
  documentPreview = false;
  documentPreviewUrl = '';
  documentPdfFile: PDFDocumentProxy = null;

  pd_newVersionObj = {id: null, nombre: '', tipo: 'html', version: '', contenido: '', file: null, size: 0};
  pd_editarPlantillaVisible = false;
  pd_confirmarVersionadoVisible = false;
  pd_confirmarVersionado = false;
  pd_currentVersionEditable = true;
  pd_currentVersion: VersionDocumentoDTO = Object.assign({}, this.pd_newVersionObj);

  listaVersionesDocumento: VersionDocumentoDTO[] = [];
  listaAnexos: AnexoDTO[] = [];

  fechaCreacion = new Date();
  numeroRadicado = null;
  tipoPlantillaSelected: ConstanteDTO;

  activeTaskUnsubscriber: Subscription;

  nombreSede = '';
  nombreDependencia = '';

  @ViewChild("alertItem") alertItem;

  @Input()
  idecmDocumentoRadicado: string;


  constructor(private _store: Store<State>,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private _dependenciaSandbox: DependenciaSandbox,
              private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private messagingService: MessagingService,
              private pdMessageService: PdMessageService) {

    this.initForm();
  }

  ngOnInit(): void {
    this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });

    this.activeTaskUnsubscriber = this._store.select(getActiveTask)

      .flatMap(activeTask => {
        console.log('TASK -> ', activeTask);
        this.taskData = activeTask;
        return this._dependenciaSandbox.loadDependencies({});
      })
      .subscribe((results) => {


        if (this.taskData && this.taskData.variables) {
          this.taskData.variables.nombreDependencia = results.dependencias.find((element) => element.codigo === this.taskData.variables.codDependencia).nombre;
          this.taskData.variables.nombreSede = results.dependencias.find((element) => element.codSede === this.taskData.variables.codigoSede).nomSede;
          this._changeDetectorRef.detectChanges();
        }

      }
      );

    this.tiposComunicacion$ = this._produccionDocumentalApi.getTiposComunicacionSalida({});
    this.tiposAnexo$ = this._produccionDocumentalApi.getTiposAnexo({});
    this.tiposPlantilla$ = this._produccionDocumentalApi.getTiposPlantilla({});
    this.listenForErrors();

  }

  updateStatus(currentStatus: StatusDTO) {
    if (currentStatus.datosGenerales.tipoComunicacion) {
      this.form.get('tipoComunicacion').setValue(currentStatus.datosGenerales.tipoComunicacion);
      this.form.get('tipoPlantilla').setValue(currentStatus.datosGenerales.tipoPlantilla);
      this.pdMessageService.sendMessage(currentStatus.datosGenerales.tipoComunicacion);
    }
    this.listaVersionesDocumento = [...currentStatus.datosGenerales.listaVersionesDocumento];
    this.listaAnexos = [...currentStatus.datosGenerales.listaAnexos];
    this.refreshView();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'tipoComunicacion': [null, Validators.required],
      'tipoPlantilla': [null, Validators.required],
      'elaborarDocumento': [null],
      'soporte': 'electronico',
      'tipoAnexo': [null],
      'descripcion': [null],
    });
  }

  resetCurrentVersion() {
    this.pd_currentVersion = Object.assign({}, this.pd_newVersionObj);
    return this.pd_currentVersion;
  }

  attachDocument(event, versionUploader: FileUpload) {
    if (event.files.length === 0) { return false; }
    const nv: VersionDocumentoDTO = {
      id: this.listaVersionesDocumento.length > 0 ? this.listaVersionesDocumento[this.listaVersionesDocumento.length - 1].id : null,
      nombre: event.files[0].name,
      tipo: 'pdf',
      version: '',
      contenido: '',
      file: event.files[0],
      size: event.files[0].size
    };

    this.uploadVersionDocumento(nv);
    versionUploader.clear();
    versionUploader.basicFileInput.nativeElement.value = '';
  }

  obtenerDocumentoRadicado() {

    // console.log(this.taskData);
    // if (this.documentoRadicadoUrl) {
    this.documentPreview = true;
    // } else {
    //   console.log('No se pudo mostrar el documento del radicado asociado');
    // }
  }

  loadHtmlVersion() {
      this._produccionDocumentalApi.obtenerVersionDocumento({
          id: this.pd_currentVersion.id,
          version: this.pd_currentVersion.version
      }).subscribe(
          res => {
              if ('html' === this.pd_currentVersion.tipo && (200 === res.status || 204 === res.status)) {
                  this.pd_currentVersion.contenido = res._body;
                  this.pd_editarPlantillaVisible = true;
              } else {
                  this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: res.statusText}));
              }
          },
          error => this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: error})),
          () => this.refreshView()
      );
  }

  mostrarVersionDocumento(index: number) {
    if (index === -1) {
      this.pd_currentVersionEditable = true;
      this.pd_editarPlantillaVisible = true;
      return true;
    }
    this.pd_currentVersion = Object.assign({}, this.listaVersionesDocumento[index]);

    if ('pdf' === this.pd_currentVersion.tipo) {
      this.idecmDocumentoRadicado = this.pd_currentVersion.id;
      this.showPdfViewer(this._produccionDocumentalApi.obtenerVersionDocumentoUrl({
        id: this.pd_currentVersion.id,
        version: this.pd_currentVersion.version
      }));

      window.dispatchEvent(new Event("resize"));
    }

     else {
        this.loadHtmlVersion();
    }
  }

  eliminarVersionDocumento(index) {
    this.pd_currentVersion = Object.assign({}, this.listaVersionesDocumento[index]);
    this._produccionDocumentalApi.eliminarVersionDocumento({id: this.pd_currentVersion.id}).subscribe(
      res => {
        this.removeFromList(index, 'listaVersionesDocumento');
        this.resetCurrentVersion();
      },
      error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error})),
      () => this.refreshView()
    );
  }

  confirmarVersionDocumento() {
    const blob = new Blob([this.pd_currentVersion.contenido], {type: 'text/html'});
    const nv = {
      id: this.pd_currentVersion.id,
      nombre: this.pd_currentVersion.nombre,
      tipo: 'html',
      version: '1.0',
      contenido: this.pd_currentVersion.contenido,
      file: blob,
      size: blob.size
    };
    this.pd_confirmarVersionado = false;
    this.pd_confirmarVersionadoVisible = false;
    this.pd_editarPlantillaVisible = false;
    this.pd_currentVersionEditable = false;
    this.uploadVersionDocumento(nv);
  }

  uploadVersionDocumento(doc: VersionDocumentoDTO) {
      const formData = new FormData();
      formData.append('documento', doc.file, doc.nombre);
      if (doc.id) {
          formData.append('idDocumento', doc.id);
      }
      formData.append('nombreDocumento', doc.nombre);
      formData.append('tipoDocumento', doc.tipo);
      if(this.taskData !== null){
        formData.append('sede', this.taskData.variables.nombreSede);
        formData.append('dependencia', this.taskData.variables.nombreDependencia);
        formData.append('codigoDependencia', this.taskData.variables.codDependencia);
        formData.append('nroRadicado', this.taskData.variables && this.taskData.variables.numeroRadicado || null);
        formData.append("selector",this.taskData.nombre == TASK_PRODUCIR_DOCUMENTO ? 'PD' : 'Otra cosa');
      }

      let docEcmResp: DocumentoEcmDTO = null;
      this._produccionDocumentalApi.subirVersionDocumento(formData).subscribe(
      resp => {
        if ('0000' === resp.codMensaje) {
          docEcmResp = resp.documentoDTOList[0];
          const versiones = [...this.listaVersionesDocumento];
          console.log(versiones);
          doc.id = docEcmResp && docEcmResp.idDocumento || (new Date()).toTimeString();
          doc.version = docEcmResp && docEcmResp.versionLabel || '1.0';
          versiones.push(doc);
          console.log(versiones);
          this.listaVersionesDocumento = [...versiones];
          console.log(this.listaVersionesDocumento);
        //  this.form.get('tipoPlantilla').reset();
          this.resetCurrentVersion();
          this.messagingService.publish(new DocumentUploaded(docEcmResp));
        } else {
          this._store.dispatch(new PushNotificationAction({severity: 'error', summary: resp.mensaje}));
        }
      },
      error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error}))
    );
  }

  getListaVersiones(): VersionDocumentoDTO[] {
    return this.listaVersionesDocumento;
  }

  selectAnexo(){

    if(!this.form.get('tipoAnexo').value){

      this.alertItem.ShowMessage("Debe de seleccionar un tipo de anexo");

      return false;

    }
  }

  agregarAnexo(event?, anexoUploader?: FileUpload) {
    const anexo: AnexoDTO = {
      id: (new Date()).toTimeString(), descripcion: this.form.get('descripcion').value,
      soporte: this.form.get('soporte').value, tipo: this.form.get('tipoAnexo').value
    };

    if (event && anexoUploader) {


      anexo.file = event.files[0];
      const formData = new FormData();
      formData.append('documento', anexo.file, anexo.file.name);
      formData.append('nombreDocumento', anexo.file.name);
      formData.append('tipoDocumento', anexo.file.type);
      if(this.taskData !== null){
      formData.append('sede', this.taskData.variables.nombreSede);
      formData.append('codigoDependencia', this.taskData.variables.codDependencia);
      formData.append('dependencia', this.taskData.variables.nombreDependencia);
      formData.append('nroRadicado', this.taskData.variables && this.taskData.variables.numeroRadicado || null);
      formData.append("selector",this.taskData.nombre == TASK_PRODUCIR_DOCUMENTO ? 'PD' : 'Otra cosa');

      }
      let docEcmResp: DocumentoEcmDTO = null;
      this._produccionDocumentalApi.subirAnexo(formData).subscribe(
        resp => {
          if ('0000' === resp.codMensaje) {
            docEcmResp = resp.documentoDTOList[0];
            anexo.id = docEcmResp && docEcmResp.idDocumento || (new Date()).toTimeString();
            anexo.descripcion = docEcmResp && docEcmResp.nombreDocumento || null;
            this.addAnexoToList(anexo);
            this.messagingService.publish(new DocumentUploaded(docEcmResp));
          } else {
            this._store.dispatch(new PushNotificationAction({severity: 'error', summary: resp.mensaje}));
          }
        },
        error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error}))
      );
      anexoUploader.clear();
      anexoUploader.basicFileInput.nativeElement.value = '';
    } else {
      this.addAnexoToList(anexo);
    }
  }

  addAnexoToList(anexo: AnexoDTO) {
    this.listaAnexos = [
      ...[anexo],
      ...this.listaAnexos.filter(
            value =>
            value.tipo.nombre !== anexo.tipo.nombre ||
            value.soporte !== anexo.soporte
      )
    ];
  }

  mostrarAnexo(index: number) {
    const anexo = this.listaAnexos[index];
    this.idecmDocumentoRadicado = anexo.id;
    this.showPdfViewer(this._produccionDocumentalApi.obtenerDocumentoUrl({id: anexo.id}));

    // window.open(this._produccionDocumentalApi.obtenerDocumentoUrl({id: anexo.id}));
  }

  eliminarAnexo(i) {
    const anexo = this.listaAnexos[i];
    if (anexo.soporte === 'fisico') {
      this.removeFromList(i, 'listaAnexos');
    } else {
      this._produccionDocumentalApi.eliminarAnexo({id: anexo.id}).subscribe(
        res => {
          this.removeFromList(i, 'listaAnexos');
          this.refreshView();
        },
        error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error}))
      );
    }
  }

  showPdfViewer(pdfUrl: string) {
    this.documentPreviewUrl = pdfUrl;
    this.documentPreview = true;

  }

  tipoComunicacionChange(event) {
    this.pdMessageService.sendMessage(event.value);
  }

  tipoPlanillaChange(event) {
    this.tipoPlantillaSelected = event.value;
    this.pd_currentVersion.nombre = event.value.nombre + '_';
    this._produccionDocumentalApi.getTipoPlantilla({codigo: this.tipoPlantillaSelected.nombre.toLowerCase()}).subscribe(
      result => this.pd_currentVersion.contenido = result
    );
  }

  hideVersionesDocumentoDialog() {
    this.pd_confirmarVersionadoVisible = false;
    this.pd_confirmarVersionado = false;
  }

  removeFromList(i: any, listname: string) {
    const list = [...this[listname]];
    list.splice(i, 1);
    this[listname] = list;
  }

  addToList(el: any, listname: string) {
    const list = [...this[listname]];
    list.push(el);
    this[listname] = [...list];
  }


  listenForErrors() {
    this.bindToValidationErrorsOf('tipoPlantilla');
    this.bindToValidationErrorsOf('tipoComunicacion');
  }

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);
    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    }
  }

  bindToValidationErrorsOf(control: string) {
    const ac = this.form.get(control);
    ac.valueChanges.subscribe(value => {
      if ((ac.touched || ac.dirty) && ac.errors) {
        const error_keys = Object.keys(ac.errors);
        const last_error_key = error_keys[error_keys.length - 1];
        this.validations[control] = VALIDATION_MESSAGES[last_error_key];
      } else {
        delete this.validations[control];
      }
    });
  }

  afterLoadComplete(pdf: PDFDocumentProxy) {
      this.documentLoaded = true;
      this.documentPdfFile = pdf;
      console.log('Document loaded');
  }

  hidePdf() {
    this.documentPreviewUrl = '';
    this.documentLoaded = false;
    this.documentPreview = false;
  }

  previewDocument(file) {
    window.open(URL.createObjectURL(file), '_blank');
  }

  documentViewer(url) {
    const win = window.open(url);
    win.focus();
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

  ngOnDestroy() {
    this.activeTaskUnsubscriber.unsubscribe();
  }

  isValid(): boolean {
    return this.listaVersionesDocumento.length > 0;
  }

  onErrorUpload(event) {
    console.log(event);
  }
}

