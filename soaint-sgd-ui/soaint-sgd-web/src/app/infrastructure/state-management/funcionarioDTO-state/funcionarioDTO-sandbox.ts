import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {ListForSelectionApiService} from '../../api/list-for-selection.api.service';
import * as actions from './funcionarioDTO-actions';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _listSelectionService: ListForSelectionApiService) {
  }

  loadAuthenticatedFuncionario(payload?: any) {
    return this._listSelectionService.list(environment.obtenerFuncionario_endpoint, payload);
  }

  loadAllFuncionarios(payload?: any) {
    const endpoint = `${environment.listarFuncionarios_endpoint}/${payload}`;
    return this._listSelectionService.list(endpoint);
  }

  loadDispatch(payload?) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

  loadAllFuncionariosDispatch(payload?) {
    this._store.dispatch(new actions.LoadAllAction(payload));
  }


}

