import {Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import { Sandbox } from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import { getTipoComunicacionArrayData } from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';

@Component({
  selector: 'app-datos-generales',
  templateUrl: './datos-generales.component.html',
  styles: [`
    .ui-datalist-header, .ui-datatable-header {
      text-align: left !important;
    }
  `],
  encapsulation: ViewEncapsulation.None
})
export class DatosGeneralesComponent implements OnInit {


  tipoComunicacionSuggestions$: Observable<ConstanteDTO[]>;


  constructor(private _store: Store<State>, private _sandbox: Sandbox) {
  }

  ngOnInit(): void {
    this.tipoComunicacionSuggestions$ = this._store.select(getTipoComunicacionArrayData);
  }

  onSelectTipoComunicacion(value) {
    console.info(value);
  }

  onFilterTipoComunicacion($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoComunicacion' , query);
  }

  onDropdownClickTipoComunicacion($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoComunicacion');
  }

  onFilterTipoPersona($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoPersona' , query);
  }

  onDropdownClickTipoPersona($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoPersona');
  }

  onFilterTipoAnexos($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoAnexos' , query);
  }

  onDropdownClickTipoAnexos($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoAnexos');
  }

  onFilterTipoDocumento($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoDocumento' , query);
  }

  onDropdownClickTipoDocumento($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoDocumento');
  }

  onFilterTipoTelefono($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoTelefono' , query);
  }

  onDropdownClickTipoTelefono($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoTelefono');
  }

  onFilterTipoDestinatario($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoDestinatario' , query);
  }

  onDropdownClickTipoDestinatario($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoDestinatario');
  }

  onFilterUnidadTiempo($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('unidadTiempo' , query);
  }

  onDropdownClickUnidadTiempo($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('unidadTiempo');
  }

  onFilterMediosRecepcion($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('mediosRecepcion' , query);
  }

  onDropdownClickMediosRecepcion($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('mediosRecepcion');
  }



}
