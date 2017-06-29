import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './comunicacionOficialDTO-actions';
import {ComunicacionApiService} from '../../api/comunicacionOficial.api.service';
import {Http, RequestOptions, URLSearchParams} from '@angular/http';
import {ListForSelectionApiService} from 'app/infrastructure/api/list-for-selection.api.service';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _http: ListForSelectionApiService) {
  }

  loadData(payload: any) {
    return this._http.list(environment.listarCorrespondencia_endpoint, payload);
  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  loadDispatch(payload) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

}

