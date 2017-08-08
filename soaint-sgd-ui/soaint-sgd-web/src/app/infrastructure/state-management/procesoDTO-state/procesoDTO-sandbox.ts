import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {createSelector} from 'reselect';
import * as selectors from './procesoDTO-selectors';
import * as actions from './procesoDTO-actions';
import {ApiBase} from '../../api/api-base';

@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
  }

  loadData(payload: any) {
    return this._api.list(environment.proceso_endpoint, payload);
  }

  startProcess(payload: any) {

    return this._api.post(environment.startProcess_endpoint,
      {
        idProceso: payload.codigoProceso,
        idDespliegue: payload.idDespliegue,
        estados: [
          'LISTO'
        ]
      });
  }

  loadTasksInsideProcess(payload: any) {
    const params = payload.data || payload;
    return this._api.post(environment.tasksInsideProcess_endpoint, {
      idProceso: params.nombreProceso,
      instanciaProceso: params.codigoProceso,
      estados: [
        'RESERVADO',
        'COMPLETADO',
        'ENPROGRESO',
        'LISTO'
      ]
    });
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

}

