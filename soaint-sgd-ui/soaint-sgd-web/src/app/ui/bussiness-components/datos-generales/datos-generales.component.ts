import {Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {
  getTipoComunicacionArrayData,
  getUnidadTiempoArrayData,
  getTipoPersonaArrayData,
  getTipoAnexosArrayData,
  getTipoTelefonoArrayData,
  getMediosRecepcionArrayData,
  getTipologiaDocumentalArrayData
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';

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
  unidadTiempoSuggestions$: Observable<ConstanteDTO[]>;
  tipoAnexosSuggestions$: Observable<ConstanteDTO[]>;
  medioRecepcionSuggestions$: Observable<ConstanteDTO[]>;
  tipologiaDocumentalSuggestions$: Observable<ConstanteDTO[]>;

  constructor(private _store: Store<State>, private _sandbox: Sandbox) {
  }

  ngOnInit(): void {
    this.tipoComunicacionSuggestions$ = this._store.select(getTipoComunicacionArrayData);
    this.unidadTiempoSuggestions$ = this._store.select(getUnidadTiempoArrayData);
    this.tipoAnexosSuggestions$ = this._store.select(getTipoAnexosArrayData);
    this.medioRecepcionSuggestions$ = this._store.select(getMediosRecepcionArrayData);
    this.tipologiaDocumentalSuggestions$ = this._store.select(getTipologiaDocumentalArrayData);
  }

  onSelectTipoComunicacion(value) {
    console.info(value);
  }

  onFilterTipoComunicacion($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoComunicacion', query);
  }

  onDropdownClickTipoComunicacion($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoComunicacion');
  }

  onFilterTipoAnexos($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoAnexos', query);
  }

  onDropdownClickTipoAnexos($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoAnexos');
  }


  onFilterUnidadTiempo($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('unidadTiempo', query);
  }

  onDropdownClickUnidadTiempo($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('unidadTiempo');
  }

  onFilterMediosRecepcion($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('mediosRecepcion', query);
  }

  onDropdownClickMediosRecepcion($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('mediosRecepcion');
  }

  onFilterTipologiaDocumental($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipologiaDocumental', query);
  }

  onDropdownClickTipologiaDocumental($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipologiaDocumental');
  }


}
