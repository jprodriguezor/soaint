import {Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
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

  // Listas de subscripcion
  contacts: Array<any> = [];
  dependenciasGrupoList: Array<any> = [];

  subscriptionTipoDocumentoPersona: Array<ConstanteDTO> = [];

  subscribers: Array<Subscription> = [];

  @ViewChild('datosContactos') datosContactos;

  @Input() editable = true;
  @Input() destinatario: DestinatarioDTO;
  @Input() tipoComunicacion: any;
  @Output() onChangeSedeAdministrativa: EventEmitter<any> = new EventEmitter();

  constructor(private _store: Store<State>,
              private formBuilder: FormBuilder,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox) {
    this.initStores();
  }

  ngOnInit(): void {
    this.initForm();
    this.initByTipoComunicacion();
    this.initFormByInputDestinatario();
    //this.form.disable();
    this.listenForChanges();
    this.listenForErrors();
    this.visibility['tipoPersona'] = true;
  }

  initByTipoComunicacion() {
    if (isNullOrUndefined(this.tipoComunicacion)) {
      this.tipoComunicacion = COMUNICACION_EXTERNA;
    }
    if (this.tipoComunicacion === COMUNICACION_INTERNA) {
      this.visibility['sedeAdministrativa'] = true;
      this.visibility['dependenciaGrupo'] = true;
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
    this.tipoPersonaSuggestions$ = this._store.select(getTipoPersonaArrayData);
    this.tipoDocumentoSuggestions$ = this._store.select(getTipoDocumentoArrayData);
    this.actuaCalidadSuggestions$ = this._store.select(getActuaCalidadArrayData);
  }

  initByTipoComunicacionInterna() {
    this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData);
  }

  initForm() {
    this.form = this.formBuilder.group({
      'tipoPersona': [{value: null, disabled: !this.editable}, Validators.required],
      'nit': [{value: null, disabled: !this.editable}],
      'actuaCalidad': [{value: null, disabled: !this.editable}],
      'tipoDocumento': [{value: null, disabled: !this.editable}],
      'razonSocial': [{value: null, disabled: !this.editable}, Validators.required],
      'nombreApellidos': [{value: null, disabled: !this.editable}, Validators.required],
      'nroDocumentoIdentidad': [{value: null, disabled: !this.editable}],
      'sedeAdministrativa': [{value: null, disabled: !this.editable}, Validators.required],
      'dependenciaGrupo': [{value: null, disabled: !this.editable}, Validators.required],
    });
  }

  initFormByInputDestinatario() {
    if (!isNullOrUndefined(this.destinatario)) {
      console.log('DestinatarioDTO no es null ->', this.destinatario);
    }
  }

  listenForChanges() {
    this.subscribers.push(this.form.get('sedeAdministrativa').valueChanges.distinctUntilChanged().subscribe((sede) => {
      if (this.editable && sede) {
        this.form.get('dependenciaGrupo').reset();
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

  listenForErrors() {
    this.bindToValidationErrorsOf('sedeAdministrativa');
    this.bindToValidationErrorsOf('dependenciaGrupo');
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

    if (value.codigo === PERSONA_ANONIMA) {
      this.visibility['tipoPersona'] = true;

    } else if (value.codigo === PERSONA_JURIDICA && this.tipoComunicacion === COMUNICACION_EXTERNA) {
      this.visibility['nit'] = true;
      this.visibility['actuaCalidad'] = true;
      this.visibility['razonSocial'] = true;
      this.visibility['nombreApellidos'] = true;
      this.visibility['datosContacto'] = true;
      this.visibility['inactivo'] = true;
      this.visibility['tipoDocumento'] = true;
      this.tipoDocumentoSuggestions$.subscribe(docs => {
        this.subscriptionTipoDocumentoPersona = docs.filter(doc => doc.codigo === TPDOC_NRO_IDENTIFICACION_TRIBUTARIO);
        this.form.get('tipoDocumento').setValue(this.subscriptionTipoDocumentoPersona[0]);
      }).unsubscribe();
      this.visibility['personaJuridica'] = true;
    } else if (value.codigo === PERSONA_NATURAL && this.tipoComunicacion === COMUNICACION_EXTERNA) {
      this.visibility['nombreApellidos'] = true;
      this.visibility['departamento'] = true;
      this.visibility['nroDocumentoIdentidad'] = true;
      this.visibility['datosContacto'] = true;
      this.visibility['tipoDocumento'] = true;


      this.tipoDocumentoSuggestions$.subscribe(docs => {
        this.subscriptionTipoDocumentoPersona = docs.filter(doc => doc.codigo !== TPDOC_NRO_IDENTIFICACION_TRIBUTARIO);
        this.form.get('tipoDocumento').setValue(this.subscriptionTipoDocumentoPersona.filter(doc => doc.codigo === TPDOC_CEDULA_CIUDADANIA)[0]);
      }).unsubscribe();

    }
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
}

