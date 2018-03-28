import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './dependenciaGrupoDTO-actions';
import {ApiBase} from '../../api/api-base';
import { CacheResponse } from '../../../shared/cache-response';


@Injectable()
export class Sandbox extends CacheResponse {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
                super();
  }

  loadData(payload: any) {
    return this._api.list(`${environment.dependenciaGrupo_endpoint}/${payload.codigo}`, payload);
    // return Observable.of(this.getMock()).delay(400);
  }

  loadDependencies(payload: any) {
    return this.getResponse(this._api.list(environment.dependencias_endpoint, payload)
    .map(response => {
      this.cacheResponse(payload, response);
      return response;
    }), payload)
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

