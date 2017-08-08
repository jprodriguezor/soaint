import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
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
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {
  COMUNICACION_EXTERNA, COMUNICACION_INTERNA, PERSONA_ANONIMA, PERSONA_JURIDICA,
  PERSONA_NATURAL, TPDOC_CEDULA_CIUDADANIA, TPDOC_NRO_IDENTIFICACION_TRIBUTARIO
} from 'app/shared/bussiness-properties/radicacion-properties';
import {getActuaCalidadArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/actua-calidad-selectors';
import {Subscription} from 'rxjs/Subscription';


@Component({
  selector: 'app-datos-remitente',
  templateUrl: './datos-remitente.component.html',
})
export class DatosRemitenteComponent implements OnInit {

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

  @ViewChild('datosContactos') datosContactos;
  @Input() editable = true;
  @Input() tipoComunicacion: any;
  @Output() onChangeSedeAdministrativa: EventEmitter<any> = new EventEmitter();

  constructor(private _store: Store<State>,
              private formBuilder: FormBuilder,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox) {
  }

  ngOnInit(): void {
    this.initForm();
    this.listenForChanges();
    this.listenForErrors();
  }

  initLoadTipoComunicacionExterna() {
    this.tipoPersonaSuggestions$ = this._store.select(getTipoPersonaArrayData);
    this.tipoDocumentoSuggestions$ = this._store.select(getTipoDocumentoArrayData);
    this.actuaCalidadSuggestions$ = this._store.select(getActuaCalidadArrayData);
  }

  initLoadTipoComunicacionInterna() {
    this._store.dispatch(new SedeAdministrativaLoadAction());
    this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData);

  }

  initForm() {
    this.form = this.formBuilder.group({
      'tipoPersona': [{value: null, disabled: !this.editable}, Validators.required],
      'nit': [{value: null, disabled: !this.editable}],
      'actuaCalidad': [{value: null, disabled: !this.editable}],
      'tipoDocumento': [{value: null, disabled: !this.editable}, Validators.required],
      'razonSocial': [{value: null, disabled: !this.editable}, Validators.required],
      'nombreApellidos': [{value: null, disabled: !this.editable}, Validators.required],
      'nroDocumentoIdentidad': [{value: null, disabled: !this.editable}, Validators.required],
      'sedeAdministrativa': [{value: null, disabled: !this.editable}, Validators.required],
      'dependenciaGrupo': [{value: null, disabled: !this.editable}, Validators.required],
    });

  }

  listenForChanges() {
    this.form.get('sedeAdministrativa').valueChanges.subscribe((sede) => {
      if (this.editable && sede) {
        this.form.get('dependenciaGrupo').reset();
        const depedenciaSubscription: Subscription = this._dependenciaGrupoSandbox.loadData({codigo: sede.id}).subscribe(dependencias => {
          this.dependenciasGrupoList = dependencias.organigrama;
          depedenciaSubscription.unsubscribe();
        });
      }
    });

    this.form.get('tipoPersona').valueChanges.subscribe((value) => {
      if (value) {
        this.onSelectTipoPersona(value);
      }
    });

  }

  listenForErrors() {
    this.bindToValidationErrorsOf('sedeAdministrativa');
    this.bindToValidationErrorsOf('dependenciaGrupo');
    this.bindToValidationErrorsOf('tipoPersona');
    this.bindToValidationErrorsOf('tipoDocumento');
    this.bindToValidationErrorsOf('razonSocial');
    this.bindToValidationErrorsOf('nombreApellidos');
    this.bindToValidationErrorsOf('nroDocumentoIdentidad');
  }

  onSelectTipoPersona(value) {
    this.visibility = {
      'tipoPersona': this.visibility.tipoPersona
    };

    this.form.get('tipoDocumento').disable();
    this.form.get('razonSocial').disable();
    this.form.get('nombreApellidos').disable();
    this.form.get('nroDocumentoIdentidad').disable();

    if (value.codigo === PERSONA_ANONIMA) {
      this.visibility['tipoPersona'] = true;
      // this.form.get('tipoPersona').enable();

    } else if (value.codigo === PERSONA_JURIDICA) {
      this.visibility['nit'] = true;
      this.visibility['actuaCalidad'] = true;
      this.visibility['razonSocial'] = true;
      this.form.get('razonSocial').enable();
      this.visibility['nombreApellidos'] = true;
      this.form.get('nombreApellidos').enable();
      this.visibility['datosContacto'] = true;
      this.visibility['inactivo'] = true;
      if (this.tipoComunicacion === COMUNICACION_EXTERNA) {
        this.visibility['tipoDocumento'] = true;
        this.form.get('tipoDocumento').enable();

        this.tipoDocumentoSuggestions$.subscribe(docs => {
          this.subscriptionTipoDocumentoPersona = docs.filter(doc => doc.codigo === TPDOC_NRO_IDENTIFICACION_TRIBUTARIO);
          this.form.get('tipoDocumento').setValue(this.subscriptionTipoDocumentoPersona[0]);
        }).unsubscribe();
      }
    } else if (value.codigo === PERSONA_NATURAL) {
      this.visibility['nombreApellidos'] = true;
      this.form.get('nombreApellidos').enable();
      this.visibility['departamento'] = true;
      this.visibility['nroDocumentoIdentidad'] = true;
      this.form.get('nroDocumentoIdentidad').enable();
      this.visibility['datosContacto'] = true;
      if (this.tipoComunicacion === COMUNICACION_EXTERNA) {
        this.visibility['tipoDocumento'] = true;

        this.tipoDocumentoSuggestions$.subscribe(docs => {
          this.subscriptionTipoDocumentoPersona = docs.filter(doc => doc.codigo !== TPDOC_NRO_IDENTIFICACION_TRIBUTARIO);
          this.form.get('tipoDocumento').setValue(this.subscriptionTipoDocumentoPersona.filter(doc => doc.codigo === TPDOC_CEDULA_CIUDADANIA)[0]);
        }).unsubscribe();

        this.form.get('tipoDocumento').enable();
      }
    }
  }

  setTipoComunicacion(value) {
    if (value) {
      this.visibility = {};
      this.tipoComunicacion = value.codigo;
      if (this.tipoComunicacion === COMUNICACION_INTERNA) {
        this.form.get('tipoPersona').disable();
        this.form.get('sedeAdministrativa').enable();
        this.form.get('dependenciaGrupo').enable();
        this.form.get('tipoDocumento').disable();
        this.visibility['sedeAdministrativa'] = true;
        this.visibility['dependenciaGrupo'] = true;

        this.initLoadTipoComunicacionInterna();

      } else {
        this.form.get('tipoPersona').enable();
        this.form.get('tipoDocumento').enable();
        this.form.get('sedeAdministrativa').disable();
        this.form.get('dependenciaGrupo').disable();
        this.visibility['tipoPersona'] = true;

        this.initLoadTipoComunicacionExterna();
      }
      this.form.get('tipoDocumento').disable();
      this.form.get('razonSocial').disable();
      this.form.get('nombreApellidos').disable();
      this.form.get('nroDocumentoIdentidad').disable();
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


}
