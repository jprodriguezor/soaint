import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';

import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {ListForSelectionApiService} from '../../api/list-for-selection.api.service';
import {createSelector} from 'reselect';
import * as selectors from './procesoDTO-selectors';
import * as actions from './procesoDTO-actions';

@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _listSelectionService: ListForSelectionApiService) {
  }

  loadData(payload: any) {
    return this._listSelectionService.list(environment.proceso_endpoint, payload);
  }

  startProcess(payload: any) {

    return this._listSelectionService.post(environment.startProcess_endpoint,
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
    return this._listSelectionService.post(environment.tasksInsideProcess_endpoint, {
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

