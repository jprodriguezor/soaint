import {ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';

import {
  getTipoDocumentoArrayData,
  getTipoPersonaArrayData,
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {
  COMUNICACION_EXTERNA, COMUNICACION_INTERNA, PERSONA_ANONIMA, PERSONA_JURIDICA,
  PERSONA_NATURAL, TPDOC_CEDULA_CIUDADANIA, TPDOC_NRO_IDENTIFICACION_TRIBUTARIO
} from 'app/shared/bussiness-properties/radicacion-properties';
import {getActuaCalidadArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/actua-calidad-selectors';
import {Subscription} from 'rxjs/Subscription';
import {DestinatarioDTO} from '../../../domain/destinatarioDTO';
import {isNullOrUndefined} from 'util';
import {LoadDatosRemitenteAction} from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-actions';
import {LoadDatosGeneralesAction} from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-actions';
import {LoadAction as SedeAdministrativaLoadAction} from '../../../infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {tipoDestinatarioEntradaSelector} from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors';
import {PushNotificationAction} from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import {DESTINATARIO_PRINCIPAL} from "../../../shared/bussiness-properties/radicacion-properties";
import {ConfirmationService} from 'primeng/components/common/api';

@Component({
  selector: 'app-datos-remitentes',
  templateUrl: './datos-remitentes.component.html',
})
export class DatosRemitentesComponent implements OnInit, OnDestroy {

  form: FormGroup;
  validations: any = {};
  visibility: any = {};
  display = false;

  // Observables
  tipoPersonaSuggestions$: Observable<ConstanteDTO[]>;
  tipoDocumentoSuggestions$: Observable<ConstanteDTO[]>;
  actuaCalidadSuggestions$: Observable<ConstanteDTO[]>;
  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;
  tipoDestinatarioSuggestions$: Observable<ConstanteDTO[]>;


  dependenciasGrupoList: Array<any> = [];
  subscriptionTipoDocumentoPersona: Array<ConstanteDTO> = [];
  subscribers: Array<Subscription> = [];

  editable = true;
  editDestinatario = false;
  @Input() principal = false;
  destinatario: DestinatarioDTO;
  destinatariosContactos: Array<any> = [];
  @Output() destinatarioOutput: EventEmitter<any> = new EventEmitter<any>();
  @ViewChild('destinatarioDatosContactos') destinatarioDatosContactos;


  @Input('tipoComunicacion') tipoComunicacion: string;
  @Output() onChangeSedeAdministrativa: EventEmitter<any> = new EventEmitter();

  constructor(private _store: Store<State>,
              private formBuilder: FormBuilder,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private confirmationService: ConfirmationService,
              private _changeDetectorRef: ChangeDetectorRef) {
    this.initStores();
  }

  ngOnInit(): void {
    this.internalInit();

  }

  internalInit(): void {
    this.initForm();
    this.initByTipoComunicacion();
    this.form.enable();
    this.listenForChanges();
    this.listenForErrors();
  }

  initByTipoComunicacion() {
    if (isNullOrUndefined(this.tipoComunicacion)) {
      this.tipoComunicacion = COMUNICACION_EXTERNA;
    }
    if (this.tipoComunicacion === COMUNICACION_INTERNA) {
      this.visibility['tipoPersona'] = false;
      this.visibility['sede'] = true;
      this.visibility['dependencia'] = true;
      this.initByTipoComunicacionInterna();
    } else {
      this.visibility['tipoPersona'] = true;
      this.initByTipoComunicacionExterna();
    }
  }

  initStores() {

    this._store.dispatch(new LoadDatosRemitenteAction());
    this._store.dispatch(new LoadDatosGeneralesAction());

  }

  initByTipoComunicacionExterna() {
    this.tipoPersonaSuggestions$ = this._store.select(
      getTipoPersonaArrayData).map(tps => tps.filter(tp => tp.codigo !== PERSONA_ANONIMA));
    this.tipoDocumentoSuggestions$ = this._store.select(getTipoDocumentoArrayData);
    this.actuaCalidadSuggestions$ = this._store.select(getActuaCalidadArrayData);
    this.tipoDestinatarioSuggestions$ = this._store.select(tipoDestinatarioEntradaSelector);
  }


  initByTipoComunicacionInterna() {
    this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData);
    this.tipoDestinatarioSuggestions$ = this._store.select(tipoDestinatarioEntradaSelector);
  }

  initForm() {
    this.form = this.formBuilder.group({
      'tipoPersona': [{value: null, disabled: !this.editable}, Validators.required],
      'nit': [{value: null, disabled: !this.editable}],
      'actuaCalidad': [{value: null, disabled: !this.editable}],
      'tipoDocumento': [{value: null, disabled: !this.editable}],
      'razonSocial': [{value: null, disabled: !this.editable}, Validators.required],
      'nombre': [{value: null, disabled: !this.editable}, Validators.required],
      'nroDocumentoIdentidad': [{value: null, disabled: !this.editable}],
      'sede': [{value: null, disabled: !this.editable}, Validators.required],
      'dependencia': [{value: null, disabled: !this.editable}, Validators.required],
      'tipoDestinatario': [{value: null, disabled: !this.editable}, Validators.required],
      'principal': null,
    });
  }

  initFormByDestinatario(destinatario) {
    if (!isNullOrUndefined(destinatario)) {
      this.editDestinatario = true;
      this.destinatario = destinatario;

      this.form.get('tipoPersona').setValue(this.destinatario.tipoPersona);
      this.form.get('nit').setValue(this.destinatario.nit);
      this.form.get('actuaCalidad').setValue(this.destinatario.actuaCalidad);
      this.form.get('tipoDocumento').setValue(this.destinatario.tipoDocumento);

      this.form.get('razonSocial').setValue(this.destinatario.razonSocial);
      this.form.get('nombre').setValue(this.destinatario.nombre);
      this.form.get('nroDocumentoIdentidad').setValue(this.destinatario.nroDocumentoIdentidad);
      this.form.get('sede').setValue(this.destinatario.sede);
      this.form.get('dependencia').setValue(this.destinatario.dependencia);
      this.form.get('tipoDestinatario').setValue(this.destinatario.tipoDestinatario);

      this.visibility['datosContacto'] = true;

      if (!isNullOrUndefined(this.destinatarioDatosContactos)) {

        const newList1 = (!isNullOrUndefined(this.destinatario.datosContactoList) ? this.destinatario.datosContactoList : []);
        this.destinatarioDatosContactos.contacts = [...newList1];
      }

    }
  }

  listenForChanges() {
    this.subscribers.push(this.form.get('sede').valueChanges.distinctUntilChanged().subscribe((sede) => {
      if (this.editable && sede) {
        this.form.get('dependencia').reset();
        const depedenciaSubscription: Subscription = this._dependenciaGrupoSandbox.loadData({codigo: sede.id}).subscribe(dependencias => {
          this.dependenciasGrupoList = dependencias.organigrama;
          depedenciaSubscription.unsubscribe();
        });
      }
    }));

    this.subscribers.push(this.form.get('tipoPersona').valueChanges.distinctUntilChanged().subscribe(value => {
      if (value !== null) {
        this.onSelectTipoPersona(value);
      }
    }));

  }
  /*onchangeTipoDestinatario(event){
    console.log(event);
    if (this.editDestinatario) {
      if ((this.principal && event.value.codigo === 'TP-DESP') && (this.destinatario.tipoDestinatario.codigo !== 'TP-DESP')) {
        this._store.dispatch(new PushNotificationAction({
          severity: 'warn',
          summary: 'No puede escoger mas de un principal'
        }));
        this.form.get('tipoDestinatario').reset();
      }
    }
    else {
      if (this.principal && event.value.codigo === 'TP-DESP') {
        this._store.dispatch(new PushNotificationAction({
          severity: 'warn',
          summary: 'No puede escoger mas de un principal'
        }));
        this.form.get('tipoDestinatario').reset();
      }
    }

  }*/

  listenForErrors() {
    this.bindToValidationErrorsOf('sede');
    this.bindToValidationErrorsOf('dependencia');
    this.bindToValidationErrorsOf('tipoPersona');
  }

  onSelectTipoPersona(value) {
    // const value = event.value;
    if (!this.visibility.tipoPersona) {
      return;
    } else {
      this.visibility = {
        tipoPersona: true
      };
    }

    this.visibility['datosContacto'] = true;

    if (value.codigo === PERSONA_ANONIMA) {
      this.visibility['tipoPersona'] = true;
      this.visibility['datosContacto'] = false;

    } else if (value.codigo === PERSONA_JURIDICA && this.tipoComunicacion === COMUNICACION_EXTERNA) {
      this.visibility['nit'] = true;
      this.visibility['actuaCalidad'] = true;
      this.visibility['razonSocial'] = true;
      this.visibility['nombre'] = true;
      this.visibility['inactivo'] = true;
      this.visibility['tipoDocumento'] = true;
      this.tipoDocumentoSuggestions$.subscribe(docs => {
        this.subscriptionTipoDocumentoPersona = docs.filter(doc => doc.codigo === TPDOC_NRO_IDENTIFICACION_TRIBUTARIO);
        this.form.get('tipoDocumento').setValue(this.subscriptionTipoDocumentoPersona[0]);
      }).unsubscribe();
      this.visibility['personaJuridica'] = true;
    } else if (value.codigo === PERSONA_NATURAL && this.tipoComunicacion === COMUNICACION_EXTERNA) {
      this.visibility['nombre'] = true;
      this.visibility['departamento'] = true;
      this.visibility['nroDocumentoIdentidad'] = true;
      this.visibility['tipoDocumento'] = true;

      this.tipoDocumentoSuggestions$.subscribe(docs => {
        this.subscriptionTipoDocumentoPersona = docs.filter(doc => doc.codigo !== TPDOC_NRO_IDENTIFICACION_TRIBUTARIO);
        this.form.get('tipoDocumento').setValue(this.subscriptionTipoDocumentoPersona.filter(doc => doc.codigo === TPDOC_CEDULA_CIUDADANIA)[0]);
      }).unsubscribe();

    }
    this.refreshView();
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

  ngOnDestroy() {
    this.subscribers.forEach(subscriber => {
      subscriber.unsubscribe();
    });
  }

  updateDestinatarioContacts(event) {
    this.destinatariosContactos = event;
  }

  newRemitente() {
    this.visibility['datosContacto'] = true;
    const dest: DestinatarioDTO = this.form.value;
    dest.isBacken = !isNullOrUndefined(this.destinatario) && this.destinatario.isBacken ? true : false;
    dest.interno = this.tipoComunicacion === COMUNICACION_INTERNA ? true : false;
    if (!dest.interno) {
      if(isNullOrUndefined(this.destinatarioDatosContactos)) {
        dest.datosContactoList =  [];
      }
      else {
        const newList1 =this.destinatarioDatosContactos.contacts;
        dest.datosContactoList = [...newList1];
        this.destinatarioDatosContactos.contacts = [];
        this.destinatarioDatosContactos.form.reset();
      }
    }
    if(dest.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL && this.principal){

      this.confirmationService.confirm({
        message: `<p style="text-align: center">¿Está seguro desea substituir el destinatario principal?</p>`,
        accept: () => {

          this.destinatarioOutput.emit(dest);
          this.form.reset();
          this.reset();
        },
        reject: () =>{
          this._store.dispatch(new PushNotificationAction({
            severity: 'warn',
            summary: 'Debe cambiar el tipo de Destinatario principal'
          }));

        }
      });
    }else{
      this.destinatarioOutput.emit(dest);
      this.form.reset();
      this.reset();
    }
    this.destinatario = null;
  }

  reset() {
    this.visibility['tipoPersona'] = false;
    this.visibility['nit'] = false;
    this.visibility['actuaCalidad'] = false;
    this.visibility['razonSocial'] = false;
    this.visibility['nombre'] = false;
    this.visibility['datosContacto'] = false;
    this.visibility['inactivo'] = false;
    this.visibility['tipoDocumento'] = false;
    this.visibility['nombre'] = false;
    this.visibility['departamento'] = false;
    this.visibility['nroDocumentoIdentidad'] = false;
    this.visibility['tipoDocumento'] = false;
    this.visibility['sede'] = false;
    this.visibility['dependencia'] = false;
    this.internalInit();
    this.refreshView();
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }
}

