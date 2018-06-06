import {Injectable, OnDestroy, OnInit} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {createSelector} from 'reselect';
import * as selectors from './procesoDTO-selectors';
import * as actions from './procesoDTO-actions';
import {ApiBase} from '../../api/api-base';
import {Subscription} from 'rxjs/Subscription';

@Injectable()
export class Sandbox implements OnDestroy {

  authPayload: { usuario: string, pass: string } |  {};
  authPayloadUnsubscriber: Subscription;

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
    this.authPayloadUnsubscriber = this._store.select(createSelector((s: State) => s.auth.profile, (profile) => {
      return profile ? {usuario: profile.username, pass: profile.password} : {};
    })).subscribe((value) => {
      this.authPayload = value;
    });
  }

  loadData(payload: any) {
    return this._api.list(environment.proceso_endpoint, payload);
  }

  startProcess(payload: any, dependency?: any) {

    return this._api.post(environment.startProcess_endpoint,
      Object.assign({}, {
        idProceso: payload.codigoProceso,
        idDespliegue: payload.idDespliegue,
        estados: [
          'LISTO'
        ],
        parametros: {
          codDependencia: dependency.codigo
        }
      }, this.authPayload));
  }

  IniciarProcesso(payload: any) {
    return this._api.post(environment.startProcess_endpoint,
      Object.assign({}, payload, this.authPayload));
  }

  loadTasksInsideProcess(payload: any) {
    const params = payload.data || payload;
    return this._api.post(environment.tasksInsideProcess_endpoint,
      Object.assign({}, {
        idProceso: params.nombreProceso || params.idProceso,
        instanciaProceso: params.codigoProceso || params.idInstanciaProceso,
        estados: [
          'RESERVADO',
          'COMPLETADO',
          'ENPROGRESO',
          'LISTO'
        ]
      }, this.authPayload));
  }

  filterDispatch(target, query) {
    this._store.dispatch(new actions.FilterAction({key: target, data: query}));
  }

  loadDispatch(payload?) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

  initProcessDispatch(entity) {
    this._store.dispatch(new actions.StartProcessAction(entity));
  }

  selectorMenuOptions() {
    return createSelector(selectors.getEntities, selectors.getGrupoIds, (entities, ids) => {
      return ids.map(id => {
        return {
          label: entities[id].nombreProceso, icon: 'assignment',
          command: () => this._store.dispatch(new actions.StartProcessAction(entities[id]))
        }
      })
    });
  }

  ngOnDestroy() {
    this.authPayloadUnsubscriber.unsubscribe();
  }
}

