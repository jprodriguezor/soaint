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
import {VersionDocumento, VersionDocumentoDTO} from '../../models/DocumentoDTO';
import {AnexoDTO} from '../../models/DocumentoDTO';
import {Sandbox as DependenciaSandbox} from "../../../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox";
import {getActiveTask} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {Subscription} from "rxjs/Subscription";
import {StatusDTO} from "../../models/StatusDTO";

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

    producirDocumento = {
      editarPlantillaVisible : false,
      currentVersionEditable : true,
      currentVersionVisible : false,
      currentVersionConfirmed : false,
      newVersionName : '',
      newVersionContent: '',
      newVersionIndex : -1
    };
    listaVersionesDocumento: VersionDocumentoDTO[] = [];

    fechaCreacion = new Date();

    tipoPlantillaSelected: ConstanteDTO;

    listaAnexos: AnexoDTO[] = [];
    fileContent: {id: number; file: Blob };

    nombreSede:string;
    nombreDependencia:string;

    activeTaskUnsubscriber: Subscription;

    constructor(private _store: Store<State>,
                private _produccionDocumentalApi: ProduccionDocumentalApiService,
                private _dependenciaSandbox: DependenciaSandbox,
                private formBuilder: FormBuilder,
                private _changeDetectorRef: ChangeDetectorRef,
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
          this.taskData.variables.nombreDependencia = results.dependencias.find((element) => element.codigo === activeTask.variables.codDependencia).nombre;
          this.taskData.variables.nombreSede = results.dependencias.find((element) => element.codSede === activeTask.variables.codigoSede).nomSede;
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

    versionDocumentoUpload(newDoc:VersionDocumento) {
      const formData = new FormData();
      formData.append('documento', newDoc.file, newDoc.nombre);
      const payload = {
        nombre:newDoc.nombre,
        sede:this.taskData.variables.nombreSede,
        dependencia:this.taskData.variables.nombreDependencia,
        tipo:newDoc.tipo,
        id:newDoc.id
      };
      this._produccionDocumentalApi.subirVersionDocumento(formData,payload).subscribe(response => {
        const versiones = this.listaVersionesDocumento;
        newDoc.idEcm = response.message;
        this.producirDocumento.newVersionIndex = versiones.push(newDoc) - 1;
        this.listaVersionesDocumento = [...versiones];
        this.refreshView()
      });
    }

    versionDocumentoSelected(event) {
      if (event.files.length === 0) {
        return false;
      }
      const newDoc = new VersionDocumento(event.files[0].name,null,event.files[0].size,'pdf',event.files[0]);
      this.versionDocumentoUpload(newDoc);
    }

    generarVersion() {
      this.form.get('tipoPlantilla').reset();
      const newDoc = new VersionDocumento(this.producirDocumento.newVersionName, this.producirDocumento.newVersionContent, 0, 'html', new Blob([this.producirDocumento.newVersionContent], {type : 'text/html'}));
      this.versionDocumentoUpload(newDoc);
      this.producirDocumento.editarPlantillaVisible = false;
      this.hideVersionesDocumentoDialog();
    }


    eliminarVersion(i) {

    }

    mostrarVersion(i) {

    }

    tipoComunicacionChange(event) {
        this.pdMessageService.sendMessage(event.value);
    }

    tipoPlanillaChange(event) {
        this.tipoPlantillaSelected = event.value;
        this.producirDocumento.newVersionName = event.value.nombre + "_";
        this._produccionDocumentalApi.getTipoPlantilla({codigo:this.tipoPlantillaSelected.nombre.toLowerCase()}).subscribe(
          result => this.producirDocumento.newVersionContent = result,
          error => console.log("Error :: " + error)
        );
    }

    hideVersionesDocumentoDialog() {
        this.producirDocumento.currentVersionConfirmed = false;
        this.producirDocumento.currentVersionVisible = false;
    }

    agregarAnexo() {
        const anexos = this.listaAnexos;
        const anexo: AnexoDTO = {
          id : (new Date()).getTime().toString(),
          soporte: this.form.get('soporte').value,
          tipo: this.form.get('tipoAnexo').value,
          descripcion: this.form.get('descripcion').value,
          file: this.fileContent
        };
        anexos.push(anexo);
        this.listaAnexos = [...anexos];
        this.refreshView();
    }

    removeFromList(i, listname: string) {
      const list = [...this[listname]];
      list.splice(i, 1);
      this[listname] = list;
      // this._changeDetectorRef.detectChanges();
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
        window.open(URL.createObjectURL(file), '_blank');
      }
      return true;
    }

    onErrorUpload(event) {
        console.log(event);
    }

    refreshView() {
      this._changeDetectorRef.detectChanges();
    }

    ngOnDestroy() {
      this.activeTaskUnsubscriber.unsubscribe();
    }

    isValid(): boolean {
      return this.listaVersionesDocumento.length > 0
      && this.listaAnexos.length > 0;
    }
}

