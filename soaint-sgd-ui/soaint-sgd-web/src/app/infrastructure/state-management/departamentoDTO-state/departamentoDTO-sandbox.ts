import {ConstanteDTO} from '../../../domain/constanteDTO';
import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {ListForSelectionApiService} from '../../api/list-for-selection.api.service';
import * as actions from './departamentoDTO-actions';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _listSelectionService: ListForSelectionApiService) {
  }

  loadData(payload: any) {
    const departamento_endpoint = `${environment.departamento_endpoint}/${payload.codPais}`;
    return this._listSelectionService.list(departamento_endpoint, payload);
  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  loadDispatch(payload) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

}

