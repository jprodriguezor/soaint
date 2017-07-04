import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';

import {
  getTipoDocumentoArrayData,
  getTipoPersonaArrayData,
  getTipoTelefonoArrayData,
  getTratamientoCortesiaArrayData
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {getArrayData as municipioArrayData} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-selectors';
import {getArrayData as paisArrayData} from 'app/infrastructure/state-management/paisDTO-state/paisDTO-selectors';
import {getArrayData as departamentoArrayData} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-selectors';
import {PaisDTO} from 'app/domain/paisDTO';
import {MunicipioDTO} from 'app/domain/municipioDTO';
import {DepartamentoDTO} from 'app/domain/departamentoDTO';
import {Sandbox as MunicipioSandbox} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-sandbox';
import {Sandbox as DepartamentoSandbox} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-sandbox';
import {Sandbox as PaisSandbox} from 'app/infrastructure/state-management/paisDTO-state/paisDTO-sandbox';
import {getArrayData as dependenciaGrupoArrayData} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';


@Component({
  selector: 'app-datos-remitente',
  templateUrl: './datos-remitente.component.html',
})
export class DatosRemitenteComponent implements OnInit {

  form: FormGroup;
  validations: any = {};
  visibility: any = {};

  addresses: Array<any> = [];
  display = false;

  tipoTelefonoSuggestions$: Observable<ConstanteDTO[]>;
  tipoPersonaSuggestions$: Observable<ConstanteDTO[]>;
  tipoDocumentoSuggestons$: Observable<ConstanteDTO[]>;
  tratamientoCortesiaSuggestons$: Observable<ConstanteDTO[]>;
  paisSuggestions$: Observable<PaisDTO[]>;
  departamentoSuggestions$: Observable<DepartamentoDTO[]>;
  municipioSuggestions$: Observable<MunicipioDTO[]>;

  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;

  @Input()
  editable = true;

  @Input()
  tipoComunicacion: any;

  @Output()
  onChangeSedeAdministrativa: EventEmitter<any> = new EventEmitter();

  constructor(private _store: Store<State>,
              private _municipioSandbox: MunicipioSandbox,
              private _departamentoSandbox: DepartamentoSandbox,
              private _paisSandbox: PaisSandbox,
              private formBuilder: FormBuilder,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox) {
  }

  showDialog() {
    this.display = true;
  }

  ngOnInit(): void {
    this.tipoTelefonoSuggestions$ = this._store.select(getTipoTelefonoArrayData);
    this.tipoPersonaSuggestions$ = this._store.select(getTipoPersonaArrayData);
    this.tipoDocumentoSuggestons$ = this._store.select(getTipoDocumentoArrayData);
    this.tratamientoCortesiaSuggestons$ = this._store.select(getTratamientoCortesiaArrayData);
    this.paisSuggestions$ = this._store.select(paisArrayData);
    this.municipioSuggestions$ = this._store.select(municipioArrayData);
    this.departamentoSuggestions$ = this._store.select(departamentoArrayData);
    this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData);
    this.dependenciaGrupoSuggestions$ = this._store.select(dependenciaGrupoArrayData);

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
      'tipoDocumento': [{value: null, disabled: !this.editable}, Validators.required],
      'razonSocial': [{value: null, disabled: !this.editable}, Validators.required],
      'nombreApellidos': [{value: null, disabled: !this.editable}, Validators.required],
      'tipoTelefono': [{value: null, disabled: !this.editable}],
      'inactivo': [{value: null, disabled: !this.editable}],
      'numeroTel': [{value: null, disabled: !this.editable}],
      'correoEle': [{value: null, disabled: !this.editable}],
      'pais': [{value: null, disabled: !this.editable}],
      'departamento': [{value: null, disabled: true}],
      'municipio': [{value: null, disabled: true}],
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

    const paisControl = this.form.get('pais');
    const departamentoControl = this.form.get('departamento');
    const municipioControl = this.form.get('municipio');

    paisControl.valueChanges.subscribe(value => {
      if (this.editable && value) {
        departamentoControl.enable();
      } else {
        departamentoControl.reset();
        departamentoControl.disable();
      }
    });

    departamentoControl.valueChanges.subscribe(value => {
      if (this.editable && value) {
        municipioControl.enable();
      } else {
        municipioControl.reset();
        municipioControl.disable();
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

    if (value.codigo === 'ANONIM') {
      this.visibility['tipoPersona'] = true;
      // this.form.get('tipoPersona').enable();

    } else if (value.codigo === 'PERS-JUR') {
      this.visibility['nit'] = true;
      this.visibility['actuaCalidad'] = true;
      this.visibility['razonSocial'] = true;
      this.form.get('razonSocial').enable();
      this.visibility['nombreApellidos'] = true;
      this.form.get('nombreApellidos').enable();
      this.visibility['tipoTelefono'] = true;
      this.visibility['inactivo'] = true;
      this.visibility['numeroTel'] = true;
      this.visibility['correoEle'] = true;
      this.visibility['pais'] = true;
      this.visibility['departamento'] = true;
      this.visibility['nroDocumentoIdentidad'] = true;
      this.form.get('nroDocumentoIdentidad').enable();
      this.visibility['municipio'] = true;
      this.visibility['direccion'] = true;
      if (this.tipoComunicacion === 'EE') {
        this.visibility['tipoDocumento'] = true;
        this.form.get('tipoDocumento').enable();
      }
    } else if (value.codigo === 'PERS-NAT') {

      this.visibility['nombreApellidos'] = true;
      this.form.get('nombreApellidos').enable();
      this.visibility['tipoTelefono'] = true;
      this.visibility['numeroTel'] = true;
      this.visibility['pais'] = true;
      this.visibility['departamento'] = true;
      this.visibility['nroDocumentoIdentidad'] = true;
      this.form.get('nroDocumentoIdentidad').enable();
      this.visibility['municipio'] = true;
      this.visibility['direccion'] = true;

      if (this.tipoComunicacion === 'EE') {
        this.visibility['tipoDocumento'] = true;
        this.form.get('tipoDocumento').enable();
      }
    }

  }

  deleteAdress(index) {
    let radref = [...this.addresses];
    radref.splice(index, 1);
    this.addresses = radref;
  }

  hideDialog($event) {
    this.display = false;
    let addresses = [...this.addresses];
    addresses.push($event);
    this.addresses = addresses;
  }


  setTipoComunicacion(value) {
    if (value) {
      this.tipoComunicacion = value.codigo;
      if (this.tipoComunicacion === 'EI') {
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

  onDropdownClickPais() {
    this._paisSandbox.loadDispatch();
  }

  onDropdownClickDepartamento($event) {
    const pais = this.form.get('pais').value;
    if (pais) {
      this._departamentoSandbox.loadDispatch({codPais: pais.codigo});
    }
  }

  onDropdownClickMunicipio($event) {
    const departamento = this.form.get('departamento').value;
    if (departamento) {
      this._municipioSandbox.loadDispatch({codDepar: departamento.codigo});
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

  onFilterPais(event) {
  }


}
