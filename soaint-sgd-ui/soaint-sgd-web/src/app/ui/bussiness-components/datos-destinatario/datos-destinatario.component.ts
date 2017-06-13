import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/operator/filter';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {
  getTipoDestinatarioArrayData,
  getSedeAdministrativaArrayData,
  getDependenciaGrupoArrayData
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';

@Component({
  selector: 'app-datos-destinatario',
  templateUrl: './datos-destinatario.component.html'
})
export class DatosDestinatarioComponent implements OnInit {


  tipoDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;


  constructor(private _store: Store<State>, private _sandbox: Sandbox) {
  }

  ngOnInit(): void {
    this.tipoDestinatarioSuggestions$ = this._store.select(getTipoDestinatarioArrayData);
    this.sedeAdministrativaSuggestions$ = this._store.select(getSedeAdministrativaArrayData);
    this.dependenciaGrupoSuggestions$ = this._store.select(getDependenciaGrupoArrayData);
  }

  onFilterTipoDestinatario($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoDestinatario', query);
  }

  onDropdownClickTipoDestinatario($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoDestinatario');
  }

  onFilterSedeAdministrativa($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('sedeAdministrativa', query);
  }

  onDropdownClickSedeAdministrativa($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('sedeAdministrativa');
  }

  onFilterDependenciaGrupo($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('dependenciaGrupo', query);
  }

  onDropdownClickDependenciaGrupo($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('dependenciaGrupo');
  }


}
