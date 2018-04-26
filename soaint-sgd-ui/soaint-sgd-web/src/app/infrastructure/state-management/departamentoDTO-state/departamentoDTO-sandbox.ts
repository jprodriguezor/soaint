import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './departamentoDTO-actions';
import {ApiBase} from '../../api/api-base';
import {CacheResponse} from "../../../shared/cache-response";


@Injectable()
export class Sandbox extends CacheResponse{

  constructor(private _store: Store<State>,
              private _api: ApiBase) {

    super();
  }

  loadData(payload: any) {

    const departamento_endpoint = `${environment.departamento_endpoint}/${payload.codPais}`;

    return this.getResponse(payload,this._api.list(departamento_endpoint, payload)
      .map((response) => {

        let res = {
          departamentos: response.departamentos.sort((departamento1, departamento2):number => {
            if (departamento1.nombre < departamento2.nombre) return -1;
            if (departamento1.nombre > departamento2.nombre) return 1;
            return 0;
          })
        };
         return res;

      }),departamento_endpoint);

  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  loadDispatch(payload) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

}

