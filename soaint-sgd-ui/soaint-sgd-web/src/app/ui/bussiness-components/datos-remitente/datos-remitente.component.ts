import {Component, Input, OnInit} from '@angular/core';
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

import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {getArrayData as municipioArrayData} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-selectors';
import {getArrayData as paisArrayData} from 'app/infrastructure/state-management/paisDTO-state/paisDTO-selectors';
import {getArrayData as departamentoArrayData} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-selectors';
import {PaisDTO} from 'app/domain/paisDTO';
import {MunicipioDTO} from 'app/domain/municipioDTO';
import {DepartamentoDTO} from 'app/domain/departamentoDTO';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {Sandbox as MunicipioSandbox} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-sandbox';
import {Sandbox as DepartamentoSandbox} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-sandbox';
import {Sandbox as PaisSandbox} from 'app/infrastructure/state-management/paisDTO-state/paisDTO-sandbox';


@Component({
  selector: 'app-datos-remitente',
  templateUrl: './datos-remitente.component.html',
})
export class DatosRemitenteComponent implements OnInit {

  form: FormGroup;
  tipoPersonaControl: AbstractControl;
  nitControl: AbstractControl;
  actuaCalidadControl: AbstractControl;
  tipoDocumentoControl: AbstractControl;
  razonSocialControl: AbstractControl;
  nombreApellidosControl: AbstractControl;
  tipoTelefonoControl: AbstractControl;
  inactivoControl: AbstractControl;
  numeroTelControl: AbstractControl;
  correoEleControl: AbstractControl;
  paisControl: AbstractControl;
  departamentoControl: AbstractControl;
  municipioControl: AbstractControl;

  tipoTelefonoSuggestions$: Observable<ConstanteDTO[]>;
  tipoPersonaSuggestions$: Observable<ConstanteDTO[]>;
  tipoDocumentoSuggestons$: Observable<ConstanteDTO[]>;
  tratamientoCortesiaSuggestons$: Observable<ConstanteDTO[]>;
  paisSuggestions$: Observable<PaisDTO[]>;
  departamentoSuggestions$: Observable<DepartamentoDTO[]>;
  municipioSuggestions$: Observable<MunicipioDTO[]>;

  selectedPais: any;
  selectedDepartamento: any;

  addresses: Array<any> = [];

  display: boolean = false;

  @Input()
  editable: boolean = true;

  constructor(private _store: Store<State>,
              private _constanteSandbox: ConstanteSandbox,
              private _municipioSandbox: MunicipioSandbox,
              private _departamentoSandbox: DepartamentoSandbox,
              private _paisSandbox: PaisSandbox,
              private formBuilder: FormBuilder) {
    this.initForm();
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

  initForm() {
    this.tipoPersonaControl = new FormControl(null);
    this.nitControl = new FormControl(null);
    this.actuaCalidadControl = new FormControl(null);
    this.tipoDocumentoControl = new FormControl(null);
    this.razonSocialControl = new FormControl(null);
    this.nombreApellidosControl = new FormControl(null, Validators.required);
    this.tipoTelefonoControl = new FormControl(null);
    this.inactivoControl = new FormControl(null);
    this.numeroTelControl = new FormControl(null);
    this.correoEleControl = new FormControl(null);
    this.paisControl = new FormControl(null);
    this.departamentoControl = new FormControl(null);
    this.municipioControl = new FormControl(null);

    this.form = this.formBuilder.group({
      'tipoPersona': this.tipoPersonaControl,
      'nit': this.nitControl,
      'actuaCalidad': this.actuaCalidadControl,
      'tipoDocumento': this.tipoDocumentoControl,
      'razonSocial': this.razonSocialControl,
      'nombreApellidos': this.nombreApellidosControl,
      'tipoTelefono': this.tipoTelefonoControl,
      'inactivo': this.inactivoControl,
      'numeroTel': this.numeroTelControl,
      'correoEle': this.correoEleControl,
      'pais': this.paisControl,
      'departamento': this.departamentoControl,
      'municipio': this.municipioControl,
    });
  }


  onFilterTipoTelefono($event) {
    this._constanteSandbox.filterDispatch('tipoTelefono', $event.query);
  }

  onDropdownClickTipoTelefono($event) {
    // this method triggers load of suggestions
    this._constanteSandbox.loadDispatch('tipoTelefono');
  }

  onFilterTipoPersona($event) {
    const query = $event.query;
    this._constanteSandbox.filterDispatch('tipoPersona', query);
  }

  onDropdownClickTipoPersona($event) {
    // this method triggers load of suggestions
    this._constanteSandbox.loadDispatch('tipoPersona');
  }

  onFilterTipoDocumento($event) {
    const query = $event.query;
    this._constanteSandbox.filterDispatch('tipoDocumento', query);
  }

  onDropdownClickTipoDocumento($event) {
    // this method triggers load of suggestions
    this._constanteSandbox.loadDispatch('tipoDocumento');
  }

  onSelectPais(value) {
    this.selectedPais = value.codigo;
    console.log(this.selectedPais);
  }

  onFilterPais($event) {
    this._paisSandbox.filterDispatch($event.query);
  }

  onDropdownClickPais() {
    this._paisSandbox.loadDispatch();
  }

  onSelectDepartamento(value) {
    this.selectedDepartamento = value.codDepar;
  }

  onFilterDepartamento($event) {
    if (this.selectedPais) {
      this._departamentoSandbox.filterDispatch({query: $event.query, codPais: this.selectedPais});
    }
  }

  onDropdownClickDepartamento($event) {
    if (this.selectedPais) {
      this._departamentoSandbox.loadDispatch({codPais: this.selectedPais});
    }
  }

  onFilterMunicipio($event) {
    if (this.selectedDepartamento) {
      this._departamentoSandbox.filterDispatch({query: $event.query, codDepar: this.selectedDepartamento});
    }
  }

  onDropdownClickMunicipio($event) {
    if (this.selectedDepartamento) {
      this._municipioSandbox.loadDispatch({codDepar: this.selectedDepartamento});
    }
  }

  onDropdownClickTratamientoCortesia($event) {
    // this method triggers load of suggestions
    this._constanteSandbox.loadDispatch('tratamientoCortesia');
  }

}
