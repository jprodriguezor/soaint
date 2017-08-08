import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './departamentoDTO-actions';
import {ApiBase} from '../../api/api-base';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
  }

  loadData(payload: any) {
    const departamento_endpoint = `${environment.departamento_endpoint}/${payload.codPais}`;
    return this._api.list(departamento_endpoint, payload);
  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  loadDispatch(payload) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

}

