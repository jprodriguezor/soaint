import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './funcionarioDTO-actions';
import {ApiBase} from '../../api/api-base';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
  }

  loadAuthenticatedFuncionario(payload?: any) {
    return this._api.list(`${environment.obtenerFuncionario_endpoint}/${payload.username}`, payload.payload);
  }

  loadAllFuncionarios(payload?: any) {
    const endpoint = `${environment.listarFuncionarios_endpoint}/${payload}`;
    return this._api.list(endpoint);
  }

  loadDispatch(payload?) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

  loadAllFuncionariosDispatch(payload?) {
    this._store.dispatch(new actions.LoadAllAction(payload));
  }


}

