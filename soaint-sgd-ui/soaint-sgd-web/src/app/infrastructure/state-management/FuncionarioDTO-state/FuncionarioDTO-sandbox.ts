import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {ListForSelectionApiService} from '../../api/list-for-selection.api.service';
import * as actions from './FuncionarioDTO-actions';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _listSelectionService: ListForSelectionApiService) {
  }

  loadData(payload?: any) {
    return this._listSelectionService.list(environment.obtenerFuncionario_endpoint, payload);
  }

  loadDispatch(payload?) {
    this._store.dispatch(new actions.LoadAction(payload));
  }


}

