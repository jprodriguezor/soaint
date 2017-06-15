import {Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';
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

  tipoTelefonoSuggestions$: Observable<ConstanteDTO[]>;
  tipoPersonaSuggestions$: Observable<ConstanteDTO[]>;
  tipoDocumentoSuggestons$: Observable<ConstanteDTO[]>;
  tratamientoCortesiaSuggestons$: Observable<ConstanteDTO[]>;
  paisSuggestions$: Observable<PaisDTO[]>;
  departamentoSuggestions$: Observable<DepartamentoDTO[]>;
  municipioSuggestions$: Observable<MunicipioDTO[]>;


  constructor(private _store: Store<State>,
              private _constanteSandbox: ConstanteSandbox,
              private _municipioSandbox: MunicipioSandbox,
              private _departamentoSandbox: DepartamentoSandbox,
              private _paisSandbox: PaisSandbox) {
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

  onFilterPais($event) {
    this._paisSandbox.filterDispatch($event.query);
  }

  onDropdownClickPais($event) {
    this._paisSandbox.loadDispatch();
  }

  onFilterMunicipio($event) {
    this._municipioSandbox.filterDispatch($event.query);
  }

  onDropdownClickMunicipio($event) {
    this._municipioSandbox.loadDispatch();
  }

  onFilterDepartamento($event) {
    this._departamentoSandbox.filterDispatch($event.query);
  }

  onDropdownClickDepartamento($event) {
    this._departamentoSandbox.loadDispatch();
  }

  onDropdownClickTratamientoCortesia($event) {
    // this method triggers load of suggestions
    this._constanteSandbox.loadDispatch('tratamientoCortesia');
  }

}
