import {ConstanteDTO} from '../../../domain/constanteDTO';
import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './dependenciaGrupoDTO-actions';
import {Observable} from 'rxjs/Observable';
import {ApiBase} from '../../api/api-base';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
  }

  loadData(payload: any) {
    const _endpoint = `${environment.dependenciaGrupo_endpoint}/${payload.codigo}`;
    return this._api.list(_endpoint, payload);

    // return Observable.of(this.getMock()).delay(400);
  }

  loadDispatch(payload) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

  getMock(): any {
    return {
      constantes: [
        {ideConst: 1, nombre: 'Dependencia #1', codigo: 1},
        {ideConst: 2, nombre: 'Dependencia #2', codigo: 2},
        {ideConst: 3, nombre: 'Dependencia #3', codigo: 3},
        {ideConst: 4, nombre: 'Dependencia #4', codigo: 4},
      ]
    }
  }

}

