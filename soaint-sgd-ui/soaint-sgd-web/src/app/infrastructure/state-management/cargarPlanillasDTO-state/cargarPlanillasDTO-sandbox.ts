import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './cargarPlanillasDTO-actions';
import {ApiBase} from '../../api/api-base';
import { IfObservable } from 'rxjs/observable/IfObservable';
import {Observable } from 'rxjs/Observable';
import { PlanAgenDTO } from '../../../domain/PlanAgenDTO';
import { PlanAgentesDTO } from '../../../domain/PlanAgentesDTO';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
  }

  loadData(payload: any) {
    return this._api.list(environment.listarPlanillas_endpoint, payload);
  }

  loadPlanillasSalida(payload: any): Observable<any> {
    return this._api.list(environment.listarPlanillasSalida_endpoint, payload);
  }
  
  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  loadDispatch(payload) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

}

