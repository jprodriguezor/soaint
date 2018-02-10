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
import {Sandbox as DependenciaSandbox} from "../../../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox";
import {getActiveTask} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {Subscription} from "rxjs/Subscription";
import {StatusDTO} from "../../models/StatusDTO";
import {WARN_REDIRECTION} from "../../../../../shared/lang/es";
import {PushNotificationAction} from "../../../../../infrastructure/state-management/notifications-state/notifications-actions";

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
    pd_currentVersion: VersionDocumentoDTO = {id:'none',nombre:'',tipo:'html',version:'',contenido:'',file:null,size:0};

    listaVersionesDocumento: VersionDocumentoDTO[] = [];
    listaAnexos: AnexoDTO[] = [];

    fechaCreacion = new Date();
    tipoPlantillaSelected: ConstanteDTO;
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

    attachDocument(event) {
      if (event.files.length === 0) {
        return false;
      }
      this.pd_currentVersion.file = event.files[0];
      this.pd_currentVersion.nombre = event.files[0].name;
      this.pd_currentVersion.size = event.files[0].size;
      this.pd_currentVersion.tipo = 'pdf';
      this.uploadVersionDocumento();
    }



    mostrarVersionDocumento(index:number) {
      this.pd_currentVersion = this.listaVersionesDocumento[index];
      this._produccionDocumentalApi.obtenerVersionDocumento({id:this.pd_currentVersion.id,version:this.pd_currentVersion.version}).subscribe(
        res => {
          console.log(res);
        }
      );

      // if (index === this.listaVersionesDocumento.length - 1) {
      //   this.pd_currentVersionEditable = true;
      // }
      // this.pd_editarPlantillaVisible = true;
    }

    confirmarVersionDocumento() {
      this.pd_currentVersion.file = new Blob([this.pd_currentVersion.contenido], {type : 'text/html'});
      this.pd_currentVersion.size = this.pd_currentVersion.file.size;
      this.pd_confirmarVersionado = false;
      this.pd_confirmarVersionadoVisible = false;
      this.pd_editarPlantillaVisible = false;
      this.pd_currentVersionEditable = false;
      this.uploadVersionDocumento();
    }

    uploadVersionDocumento() {
      const formData = new FormData();
      formData.append('documento', this.pd_currentVersion.file, this.pd_currentVersion.nombre);
      const payload = {
        nombre:this.pd_currentVersion.nombre,
        sede:this.taskData.variables.nombreSede,
        dependencia:this.taskData.variables.nombreDependencia,
        tipo:this.pd_currentVersion.tipo,
        id:this.pd_currentVersion.id
      };
      this._produccionDocumentalApi.subirVersionDocumento(formData,payload).subscribe(
        resp => {
          if (resp.codMensaje==='0000') {
            const versiones = this.listaVersionesDocumento;
            this.pd_currentVersion.id = resp.content && resp.content.idDocumento || resp.mensaje || (new Date()).toTimeString();
            this.pd_currentVersion.version = resp.content && resp.content.versionLabel || "1.0";
            versiones.push(this.pd_currentVersion);
            this.listaVersionesDocumento = [...versiones];
            this.form.get('tipoPlantilla').reset();
            this.refreshView()
          } else {
            this._store.dispatch(new PushNotificationAction({severity: 'error',summary: resp.mensaje}));
          }
        }
      );
    }











    tipoComunicacionChange(event) {
        this.pdMessageService.sendMessage(event.value);
    }

    tipoPlanillaChange(event) {
        this.tipoPlantillaSelected = event.value;
        this.pd_currentVersion.nombre = event.value.nombre + "_";
        this._produccionDocumentalApi.getTipoPlantilla({codigo:this.tipoPlantillaSelected.nombre.toLowerCase()}).subscribe(
          result => this.pd_currentVersion.contenido = result
        );
    }

    hideVersionesDocumentoDialog() {
        this.pd_confirmarVersionadoVisible = false;
        this.pd_confirmarVersionado = false;
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

      }
      return true;
    }

    previewDocument(file:File) {
      window.open(URL.createObjectURL(file), '_blank');
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

