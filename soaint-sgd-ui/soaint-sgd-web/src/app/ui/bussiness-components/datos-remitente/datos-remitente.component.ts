import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {
  getTipoDocumentoArrayData,
  getTipoPersonaArrayData,
  getTipoTelefonoArrayData,
  getTratamientoCortesiaArrayData
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';


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

  constructor(private _store: Store<State>, private _sandbox: Sandbox, private formBuilder: FormBuilder) {
    this.initForm();
  }

  ngOnInit(): void {
    this.tipoTelefonoSuggestions$ = this._store.select(getTipoTelefonoArrayData);
    this.tipoPersonaSuggestions$ = this._store.select(getTipoPersonaArrayData);
    this.tipoDocumentoSuggestons$ = this._store.select(getTipoDocumentoArrayData);
    this.tratamientoCortesiaSuggestons$ = this._store.select(getTratamientoCortesiaArrayData);
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
    this._sandbox.filterDispatch('tipoTelefono', $event.query);
  }

  onDropdownClickTipoTelefono($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoTelefono');
  }

  onFilterTipoPersona($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoPersona', query);
  }

  onDropdownClickTipoPersona($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoPersona');
  }

  onFilterTipoDocumento($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoDocumento', query);
  }

  onDropdownClickTipoDocumento($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoDocumento');
  }

  onDropdownClickTratamientoCortesia($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tratamientoCortesia');
  }

}
