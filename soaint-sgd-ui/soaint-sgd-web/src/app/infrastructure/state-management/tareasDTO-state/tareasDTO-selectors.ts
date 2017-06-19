import {State} from './tareasDTO-reducers';
import 'rxjs/add/operator/reduce';
import {createSelector} from 'reselect';
import * as rootStore from 'app/infrastructure/redux-store/redux-reducers';

const rootPath = (state: rootStore.State) => state.tareas;

/**
 * Because the data structure is defined within the reducer it is optimal to
 * locate our selector functions at this level. If store is to be thought of
 * as a database, and reducers the tables, selectors can be considered the
 * queries into said database. Remember to keep your selectors small and
 * focused so they can be combined and composed to fit each particular
 * use-case.
 */

export const getEntities = createSelector(rootPath, (state: State) => state.entities);

export const getGrupoIds = createSelector(rootPath, (state: State) => state.ids);

export const getArrayData = createSelector(getEntities, getGrupoIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});

export const getTasksStadistics = createSelector(getEntities, (entities) => {
  let stadistics = [];
  let reserved = 0;
  let completed = 0;
  let canceled = 0;
  let inProgress = 0;
  for (const key in entities) {
    if (entities[key].estado === 'Reserved') {
      reserved = reserved + 1;
    } else if (entities[key].estado === 'Completed') {
      completed = completed + 1;
    } else if (entities[key].estado === 'Canceled') {
      canceled = canceled + 1;
    } else if (entities[key].estado === 'InProgress') {
      inProgress = inProgress + 1;
    }
  }

  stadistics.push({name: 'Reservadas', value: reserved});
  stadistics.push({name: 'Completadas', value: completed});
  stadistics.push({name: 'En Proceso', value: inProgress});
  stadistics.push({name: 'Canceladas', value: canceled});
  return stadistics;
});


export const getReservedTasksArrayData = createSelector(getEntities, getGrupoIds, (entities, ids) => {
  return ids.map(id => entities[id]).filter(data => data.estado == 'Reserved');
});

export const getCompletedTasksArrayData = createSelector(getEntities, getGrupoIds, (entities, ids) => {
  return ids.map(id => entities[id]).filter(data => data.estado == 'Completed');
});

export const getInProgressTasksArrayData = createSelector(getEntities, getGrupoIds, (entities, ids) => {
  return ids.map(id => entities[id]).filter(data => data.estado == 'InProgress');
});

export const getCanceledTasksArrayData = createSelector(getEntities, getGrupoIds, (entities, ids) => {
  return ids.map(id => entities[id]).filter(data => data.estado == 'Canceled');
});
