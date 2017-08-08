import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './asignacionDTO-actions';
import {ApiBase} from '../../api/api-base';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
  }

  assignComunications(payload: any) {
    return this._api.post(environment.asignarComunicaciones_endpoint, payload);
  }

  reassignComunications(payload: any) {
    return this._http.post(environment.reasignarComunicaciones_endpoint, payload);
  }

  redirectComunications(payload: any) {
    return this._api.post(environment.redireccionarComunicaciones_endpoint, payload);
  }

  assignDispatch(payload) {
    this._store.dispatch(new actions.AssignAction(payload));
  }

  reassignDispatch(payload) {
    this._store.dispatch(new actions.ReassignAction(payload));
  }

  redirectDispatch(payload) {
    this._store.dispatch(new actions.RedirectAction(payload));
  }

  setVisibleJustificationDialogDispatch(payload: boolean) {
    this._store.dispatch(new actions.SetJustificationDialogVisibleAction(payload));
  }

  setVisibleAddObservationsDialogDispatch(payload: boolean) {
    this._store.dispatch(new actions.SetAddObservationsDialogVisibleAction(payload));
  }

  setVisibleRejectDialogDispatch(payload: boolean) {
    this._store.dispatch(new actions.SetRejectDialogVisibleAction(payload));
  }

}

