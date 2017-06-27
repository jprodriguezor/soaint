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
import {getSedeAdministrativaArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/sede-administrativa-selectors';
import {getArrayData as dependenciaGrupoArrayData} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';

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
  nroDocumentoIdentidadControl: AbstractControl;
  sedeAdministrativaControl: AbstractControl;
  dependenciaGrupoControl: AbstractControl;

  tipoTelefonoSuggestions$: Observable<ConstanteDTO[]>;
  tipoPersonaSuggestions$: Observable<ConstanteDTO[]>;
  tipoDocumentoSuggestons$: Observable<ConstanteDTO[]>;
  tratamientoCortesiaSuggestons$: Observable<ConstanteDTO[]>;
  paisSuggestions$: Observable<PaisDTO[]>;
  departamentoSuggestions$: Observable<DepartamentoDTO[]>;
  municipioSuggestions$: Observable<MunicipioDTO[]>;

  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;

  tipoPersonaSelected: any;

  selectedPais: any;
  selectedDepartamento: any;

  addresses: Array<any> = [];

  display: boolean = false;

  @Input()
  editable: boolean = true;

  @Input()
  datosGenerales: any;

  selectSedeAdministrativa: any;

  constructor(private _store: Store<State>,
              private _constanteSandbox: ConstanteSandbox,
              private _municipioSandbox: MunicipioSandbox,
              private _departamentoSandbox: DepartamentoSandbox,
              private _paisSandbox: PaisSandbox,
              private formBuilder: FormBuilder,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox) {
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
    this.sedeAdministrativaSuggestions$ = this._store.select(getSedeAdministrativaArrayData);
    this.dependenciaGrupoSuggestions$ = this._store.select(dependenciaGrupoArrayData);
  }

  findTipoDocumentoValue() {
    this.tipoDocumentoSuggestons$.forEach((value) => {
      console.log(value);
    });
  }

  onSelectTipoPersona() {

    if (this.datosGenerales.tipoComunicacionControl.value && this.datosGenerales.tipoComunicacionControl.value.codigo === 'EI') {
      this.sedeAdministrativaControl.enable();
    } else if (this.datosGenerales.tipoComunicacionControl.value && this.datosGenerales.tipoComunicacionControl.value.codigo === 'EE') {
      this.sedeAdministrativaControl.disable();
      this.dependenciaGrupoControl.disable();
    }

    if (this.tipoPersonaControl.value) {
      if (this.tipoPersonaControl.value.codigo === 'ANONIM') {
        this.nitControl.disable();
        this.actuaCalidadControl.disable();
        this.tipoDocumentoControl.disable();
        this.razonSocialControl.disable();
        this.nombreApellidosControl.disable();
        this.tipoTelefonoControl.disable();
        this.inactivoControl.disable();
        this.numeroTelControl.disable();
        this.correoEleControl.disable();
        this.paisControl.disable();
        this.departamentoControl.disable();
        this.nroDocumentoIdentidadControl.disable();
        this.municipioControl.disable();
      } else if (this.tipoPersonaControl.value.codigo === 'PERS-JUR') {
        this.nitControl.enable();
        this.actuaCalidadControl.enable();
        if (this.datosGenerales.tipoComunicacionControl.value && this.datosGenerales.tipoComunicacionControl.value.codigo === 'EE') {
          this.tipoDocumentoControl.enable();
          this.tipoDocumentoControl.setValue({
            codPadre: "TIPO-DOC",
            codigo: "NU-ID-TR",
            nombre: "Numero de Identificación Tributario"
          });
        } else {
          this.tipoDocumentoControl.disable();
          this.tipoDocumentoControl.setValue(null);
        }
        this.razonSocialControl.enable();
        this.nombreApellidosControl.enable();
        this.tipoTelefonoControl.enable();
        this.inactivoControl.disable();
        this.numeroTelControl.enable();
        this.correoEleControl.enable();
        this.paisControl.enable();
        this.departamentoControl.enable();
        this.nroDocumentoIdentidadControl.disable();
        this.municipioControl.enable();
      } else if (this.tipoPersonaControl.value.codigo === 'PERS-NAT') {
        this.nitControl.disable();
        this.actuaCalidadControl.disable();
        if (this.datosGenerales.tipoComunicacionControl.value && this.datosGenerales.tipoComunicacionControl.value.codigo === 'EE') {
          this.tipoDocumentoControl.enable();
          this.tipoDocumentoControl.setValue({
            codPadre: "TIPO-DOC",
            codigo: "CED-CIUD",
            nombre: "Cedula de ciudadanía",
          });
        } else {
          this.tipoDocumentoControl.disable();
          this.tipoDocumentoControl.setValue(null);
        }
        this.razonSocialControl.disable();
        this.nroDocumentoIdentidadControl.enable();
        this.nombreApellidosControl.enable();
        this.tipoTelefonoControl.enable();
        this.inactivoControl.disable();
        this.numeroTelControl.enable();
        this.correoEleControl.disable();
        this.paisControl.enable();
        this.departamentoControl.enable();
        this.municipioControl.enable();
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

  initForm() {
    this.tipoPersonaControl = new FormControl(null, Validators.required);
    this.nitControl = new FormControl({value: null, disabled: true});
    this.actuaCalidadControl = new FormControl({value: null, disabled: true});
    this.tipoDocumentoControl = new FormControl({value: null, disabled: true}, Validators.required);
    this.razonSocialControl = new FormControl({value: null, disabled: true}, Validators.required);
    this.nombreApellidosControl = new FormControl({value: null, disabled: true}, Validators.required);
    this.tipoTelefonoControl = new FormControl({value: null, disabled: true});
    this.inactivoControl = new FormControl({value: null, disabled: true});
    this.numeroTelControl = new FormControl({value: null, disabled: true});
    this.correoEleControl = new FormControl({value: null, disabled: true});
    this.sedeAdministrativaControl = new FormControl({value: null, disabled: true}, Validators.required);
    this.dependenciaGrupoControl = new FormControl({value: null, disabled: true}, Validators.required);
    this.paisControl = new FormControl({
      value: {
        codigo: "co",
        nombre: "Colombia"
      }, disabled: true
    });
    this.departamentoControl = new FormControl({value: null, disabled: true});
    this.municipioControl = new FormControl({value: null, disabled: true});
    this.nroDocumentoIdentidadControl = new FormControl({value: null, disabled: true}, Validators.required);

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
      'nroDocumentoIdentidad': this.nroDocumentoIdentidadControl,
      'sedeAdministrativa': this.sedeAdministrativaControl,
      'dependenciaGrupo': this.dependenciaGrupoControl,
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


  onSelectSedeAdministrativa(value) {
    this.selectSedeAdministrativa = value.codigo;
    this.dependenciaGrupoControl.enable();
  }

  onFilterSedeAdministrativa($event) {
    const query = $event.query;
    this._constanteSandbox.filterDispatch('sedeAdministrativa', query);
  }

  onDropdownClickSedeAdministrativa($event) {
    // this method triggers load of suggestions
    this._constanteSandbox.loadDispatch('sedeAdministrativa');
  }

  onFilterDependenciaGrupo($event) {
    this._dependenciaGrupoSandbox.filterDispatch({query: $event.query, codigo: this.selectSedeAdministrativa});
  }

  onDropdownClickDependenciaGrupo($event) {
    // this method triggers load of suggestions
    if (this.selectSedeAdministrativa) {
      this._dependenciaGrupoSandbox.loadDispatch({codigo: this.selectSedeAdministrativa});
    }
  }

}
