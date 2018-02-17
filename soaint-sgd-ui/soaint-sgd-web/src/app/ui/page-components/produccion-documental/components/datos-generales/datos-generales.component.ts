import {ChangeDetectorRef, Component, Input, OnInit, ViewChild} from '@angular/core';
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
import {TareaDTO} from 'app/domain/tareaDTO';
import {VersionDocumentoDTO} from '../../models/DocumentoDTO';
import {AnexoDTO} from '../../models/DocumentoDTO';
import {Sandbox as DependenciaSandbox} from '../../../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {getActiveTask} from '../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {StatusDTO} from '../../models/StatusDTO';
import {WARN_REDIRECTION} from '../../../../../shared/lang/es';
import {PushNotificationAction} from '../../../../../infrastructure/state-management/notifications-state/notifications-actions';
import {DocumentoEcmDTO} from '../../../../../domain/documentoEcmDTO';
import {FileUpload} from 'primeng/primeng';
import {environment} from '../../../../../../environments/environment';

@Component({
  selector: 'pd-datos-generales',
  templateUrl: './datos-generales.component.html'
})

export class PDDatosGeneralesComponent implements OnInit {

    form: FormGroup;

    validations: any = {};

    taskData: TareaDTO;

    funcionarioLog: FuncionarioDTO;

    tiposComunicacion$: Observable<ConstanteDTO[]>;
    tiposAnexo$: Observable<ConstanteDTO[]>;
    tiposPlantilla$: Observable<ConstanteDTO[]>;

    pd_editarPlantillaVisible = false;
    pd_confirmarVersionadoVisible = false;
    pd_confirmarVersionado = false;
    pd_currentVersionEditable = false;
    pd_currentVersion: VersionDocumentoDTO = {id: 'none', nombre: '', tipo: 'html', version: '', contenido: '', file: null, size: 0};

    listaVersionesDocumento: VersionDocumentoDTO[] = [];
    listaAnexos: AnexoDTO[] = [];

    fechaCreacion = new Date();
    numeroRadicado = null;
    tipoPlantillaSelected: ConstanteDTO;
    fileContent: {id: number; file: Blob };

    activeTaskUnsubscriber: Subscription;

    constructor(private _store: Store<State>,
                private _produccionDocumentalApi: ProduccionDocumentalApiService,
                private _dependenciaSandbox: DependenciaSandbox,
                private formBuilder: FormBuilder,
                private _changeDetectorRef: ChangeDetectorRef,
                private messaging Mess
                private pdMessageService: PdMessageService) {

      this.initForm();
    }

    ngOnInit(): void {
      this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
        this.funcionarioLog = funcionario;
      });

      this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
        this.taskData = activeTask;
        this._dependenciaSandbox.loadDependencies({}).subscribe((results) => {
            if (this.taskData && this.taskData.variables) {
                this.taskData.variables.nombreDependencia = results.dependencias.find((element) => element.codigo === activeTask.variables.codDependencia).nombre;
                this.taskData.variables.nombreSede = results.dependencias.find((element) => element.codSede === activeTask.variables.codigoSede).nomSede;
            }
        });
      });

      this.tiposComunicacion$ = this._produccionDocumentalApi.getTiposComunicacionSalida({});
      this.tiposAnexo$ = this._produccionDocumentalApi.getTiposAnexo({});
      this.tiposPlantilla$ = this._produccionDocumentalApi.getTiposPlantilla({});
      this.listenForErrors();
    }

    updateStatus(currentStatus: StatusDTO) {
      if (currentStatus.datosGenerales.tipoComunicacion) {
        this.form.get('tipoComunicacion').setValue(currentStatus.datosGenerales.tipoComunicacion);
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
      this.pd_currentVersion = {id: 'none', nombre: '', tipo: 'html', version: '', contenido: '', file: null, size: 0};
      return this.pd_currentVersion;
    }

    attachDocument(event, versionUploader: FileUpload) {
      if (event.files.length === 0) { return false; }
      const nv: VersionDocumentoDTO = {id: 'none', nombre: event.files[0].name, tipo: 'pdf', version: '', contenido: '', file: event.files[0], size: event.files[0].size};
      if (this.listaVersionesDocumento.length > 0) {
        nv.id = this.listaVersionesDocumento[this.listaVersionesDocumento.length - 1].id;
      }
      this.uploadVersionDocumento(nv);
      versionUploader.clear();
    }

    obtenerDocumentoRadicado() {
        this._produccionDocumentalApi.obtenerDatosDocXnroRadicado({id: this.taskData.variables.numeroRadicado}).subscribe(
            res => {
                if (res.ideEcm) {
                    const url = `${environment.pd_gestion_documental.descargarDocumentoPorId}?identificadorDoc=${res.ideEcm}`;
                    window.open(url);
                } else {
                    this._store.dispatch(new PushNotificationAction({severity: 'error', summary: `No se encontro ningún documento asociado al radicado: ${this.numeroRadicado}`}));
                }
            },
            error => this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: error}))
        );
    }

    mostrarVersionDocumento(index: number) {
      if (index === - 1) {
        this.pd_currentVersionEditable = true;
        this.pd_editarPlantillaVisible = true;
        return true;
      }
      this.pd_currentVersion = Object.assign({}, this.listaVersionesDocumento[index]);
      this._produccionDocumentalApi.obtenerVersionDocumento({id: this.pd_currentVersion.id, version: this.pd_currentVersion.version}).subscribe(
        res => {
          if (200 === res.status && 'pdf' === this.pd_currentVersion.tipo) {
            window.open(res.url);
          } else if ('html' === this.pd_currentVersion.tipo && (200 === res.status || 204 === res.status)) {
            this.pd_currentVersion.contenido = res._body;
            if (index === this.listaVersionesDocumento.length - 1) {
              this.pd_currentVersionEditable = true;
            }
            this.pd_editarPlantillaVisible = true;
          } else {
            this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: res.statusText}));
          }
        },
        error => this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: error})),
        () => {
          this.refreshView();
        }
      );
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
      const blob = new Blob([this.pd_currentVersion.contenido], {type : 'text/html'});
      const nv = {
        id: this.pd_currentVersion.id,
        nombre: this.pd_currentVersion.nombre,
        tipo: 'html',
        version:  '1.0',
        contenido:  this.pd_currentVersion.contenido,
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
      const payload = {nombre: doc.nombre, sede: this.taskData.variables.nombreSede, dependencia: this.taskData.variables.nombreDependencia,
        tipo: doc.tipo, id: doc.id
      };
      let docEcmResp: DocumentoEcmDTO = null;
      this._produccionDocumentalApi.subirVersionDocumento(formData, payload).subscribe(
        resp => {
          if ('0000' === resp.codMensaje) {
            docEcmResp = resp.metadatosDocumentosDTOList[0];
            const versiones = [...this.listaVersionesDocumento];
            console.log(versiones);
            doc.id = docEcmResp && docEcmResp.idDocumento || (new Date()).toTimeString();
            doc.version = docEcmResp && docEcmResp.versionLabel || '1.0';
            versiones.push(doc);
            console.log(versiones);
            this.listaVersionesDocumento = [...versiones];
            console.log(this.listaVersionesDocumento);
            this.form.get('tipoPlantilla').reset();
            this.resetCurrentVersion();
          } else {
            this._store.dispatch(new PushNotificationAction({severity: 'error', summary: resp.mensaje}));
          }
        },
        error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error})),
        () => {
          this.refreshView();
        }
      );
    }

    getListaVersiones(): VersionDocumentoDTO[] {
        return this.listaVersionesDocumento;
    }



    agregarAnexo(event) {
      const anexo: AnexoDTO = { id: (new Date()).toTimeString(), descripcion : this.form.get('descripcion').value,
        soporte: this.form.get('soporte').value, tipo: this.form.get('tipoAnexo').value
      };
      if (event) {
        anexo.file = event.files[0];
        const formData = new FormData();
        formData.append('documento', anexo.file, anexo.file.name);
        let docEcmResp: DocumentoEcmDTO = null;
        this._produccionDocumentalApi.subirAnexo(formData, {
          nombre: anexo.file.name,
          dependencia: this.taskData.variables.nombreDependencia,
          sede: this.taskData.variables.nombreSede
        }).subscribe(
          resp => {
            if ('0000' === resp.codMensaje) {
                docEcmResp = resp.metadatosDocumentosDTOList[0];
                anexo.id = docEcmResp && docEcmResp.idDocumento || (new Date()).toTimeString();
                anexo.descripcion = docEcmResp && docEcmResp.nombreDocumento || null;
                const versiones = [...this.listaAnexos];
                versiones.push(anexo);
                this.listaAnexos = [...versiones];
                this.refreshView();
            } else {
                this._store.dispatch(new PushNotificationAction({severity: 'error', summary: resp.mensaje}));
            }
          },
          error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error}))
        );
      } else {
        this.addToList(anexo, 'listaAnexos');
      }
    }

    mostrarAnexo(index: number) {
      const anexo = this.listaAnexos[index];
      this._produccionDocumentalApi.obtenerVersionDocumento({id: anexo.id, version: '1.0'}).subscribe(
        res => {
          if (200 === res.status) {
            window.open(res.url);
          } else {
            this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: res.statusText}));
          }
        },
        error => this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: error}))
      );
    }

    eliminarAnexo(i) {
      const anexo = this.listaAnexos[i];
      if (anexo.soporte === 'fisico') {
        this.removeFromList(i, 'listaAnexos');
      } else {
        this._produccionDocumentalApi.eliminarVersionDocumento({id: anexo.id}).subscribe(
          res => {
            this.removeFromList(i, 'listaAnexos');
            this.refreshView();
          },
          error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error}))
        );
      }
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

    visualizarDocumentos(index, lista) {
      const file = this[lista][index].file;
      console.log(file);
      if (file === undefined || !file.name) {
        console.log('Error el visualizar documento');
        return false;
      } else {
        this.previewDocument(file);
      }
      return true;
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

