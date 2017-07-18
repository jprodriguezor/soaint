import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';

import {
  getTipoDocumentoArrayData,
  getTipoPersonaArrayData,
  getTratamientoCortesiaArrayData,
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {getArrayData as dependenciaGrupoArrayData} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {
  COMUNICACION_EXTERNA, COMUNICACION_INTERNA, PERSONA_ANONIMA, PERSONA_JURIDICA,
  PERSONA_NATURAL
} from 'app/shared/bussiness-properties/radicacion-properties';
import {getActuaCalidadArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/actua-calidad-selectors';



@Component({
  selector: 'app-datos-remitente',
  templateUrl: './datos-remitente.component.html',
})
export class DatosRemitenteComponent implements OnInit {

  form: FormGroup;
  formContactos: FormGroup;
  validations: any = {};
  visibility: any = {};

  addresses: Array<any> = [];
  contacts: Array<any> = [];
  display = false;

  tipoPersonaSuggestions$: Observable<ConstanteDTO[]>;
  tipoDocumentoSuggestons$: Observable<ConstanteDTO[]>;

  actuaCalidadSuggestions$: Observable<ConstanteDTO[]>;
  tratamientoCortesiaSuggestions$: Observable<ConstanteDTO[]>;
  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;


  @Input() editable = true;

  @Input()
  tipoComunicacion: any;

  @Output()
  onChangeSedeAdministrativa: EventEmitter<any> = new EventEmitter();

  constructor(private _store: Store<State>,
              private formBuilder: FormBuilder,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox) {
  }

  ngOnInit(): void {
    this.tipoPersonaSuggestions$ = this._store.select(getTipoPersonaArrayData);
    this.tipoDocumentoSuggestons$ = this._store.select(getTipoDocumentoArrayData);
    this.tratamientoCortesiaSuggestions$ = this._store.select(getTratamientoCortesiaArrayData);
    this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData);
    this.dependenciaGrupoSuggestions$ = this._store.select(dependenciaGrupoArrayData);
    this.actuaCalidadSuggestions$ = this._store.select(getActuaCalidadArrayData);
    this._store.dispatch(new SedeAdministrativaLoadAction());

    this.initForm();
    this.listenForChanges();
    this.listenForErrors();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'tipoPersona': [{value: null, disabled: !this.editable}, Validators.required],
      'nit': [{value: null, disabled: !this.editable}],
      'actuaCalidad': [{value: null, disabled: !this.editable}],
      'tratamientoCortesia': [{value: null, disabled: !this.editable}],
      'tipoDocumento': [{value: null, disabled: !this.editable}, Validators.required],
      'razonSocial': [{value: null, disabled: !this.editable}, Validators.required],
      'nombreApellidos': [{value: null, disabled: !this.editable}, Validators.required],
      'nroDocumentoIdentidad': [{value: null, disabled: !this.editable}, Validators.required],
      'sedeAdministrativa': [{value: null, disabled: !this.editable}, Validators.required],
      'dependenciaGrupo': [{value: null, disabled: !this.editable}, Validators.required],
    });


  }

  listenForChanges() {
    this.form.get('sedeAdministrativa').valueChanges.subscribe((value) => {
      if (this.editable && value) {
        this.onChangeSedeAdministrativa.emit(value);
        this.form.get('dependenciaGrupo').reset();
        this._dependenciaGrupoSandbox.loadDispatch({codigo: value.id});
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
      this.visibility['nroDocumentoIdentidad'] = true;
      this.visibility['tratamientoCortesia'] = true;
      this.form.get('nroDocumentoIdentidad').enable();
      if (this.tipoComunicacion === COMUNICACION_EXTERNA) {
        this.visibility['tipoDocumento'] = true;
        this.form.get('tipoDocumento').enable();
      }
    } else if (value.codigo === PERSONA_NATURAL) {
      this.visibility['nombreApellidos'] = true;
      this.form.get('nombreApellidos').enable();
      this.visibility['departamento'] = true;
      this.visibility['nroDocumentoIdentidad'] = true;
      this.form.get('nroDocumentoIdentidad').enable();
      this.visibility['tratamientoCortesia'] = true;
      this.visibility['datosContacto'] = true;
      if (this.tipoComunicacion === COMUNICACION_EXTERNA) {
        this.visibility['tipoDocumento'] = true;
        this.form.get('tipoDocumento').enable();
      }
    }
  }

  setTipoComunicacion(value) {
    if (value) {
      this.tipoComunicacion = value.codigo;
      if (this.tipoComunicacion === COMUNICACION_INTERNA) {
        this.form.get('tipoPersona').disable();
        this.form.get('sedeAdministrativa').enable();
        this.form.get('dependenciaGrupo').enable();
        this.form.get('tipoDocumento').disable();
        this.visibility['sedeAdministrativa'] = true;
        this.visibility['dependenciaGrupo'] = true;
        this.visibility['tipoPersona'] = false;
        this.visibility['direccion'] = false;
      } else {
        this.form.get('tipoPersona').enable();
        this.form.get('tipoDocumento').enable();
        this.form.get('sedeAdministrativa').disable();
        this.form.get('dependenciaGrupo').disable();
        this.visibility['sedeAdministrativa'] = false;
        this.visibility['dependenciaGrupo'] = false;
        this.visibility['tipoPersona'] = true;
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
