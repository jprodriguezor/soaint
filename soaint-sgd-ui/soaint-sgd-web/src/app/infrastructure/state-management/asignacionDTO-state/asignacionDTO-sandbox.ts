import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './asignacionDTO-actions';
import {ListForSelectionApiService} from 'app/infrastructure/api/list-for-selection.api.service';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _http: ListForSelectionApiService) {
  }

  assignComunications(payload: any) {
    return this._http.post(environment.asignarComunicaciones_endpoint, payload);
  }

  assignDispatch(payload) {
    this._store.dispatch(new actions.AssignAction(payload));
  }

}

