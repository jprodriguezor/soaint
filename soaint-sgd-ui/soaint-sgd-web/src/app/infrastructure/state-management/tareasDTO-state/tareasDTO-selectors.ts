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

export const getEntities = (state: rootStore.State) => state.tareas.entities;

export const getGrupoIds = (state: rootStore.State) => state.tareas.ids;

export const getActiveTask = (state: rootStore.State) => state.tareas.activeTask;

export const getNextTask = (state: rootStore.State) => state.tareas.nextTask;

export const taskStats = (state: rootStore.State) => state.tareas.stats;

export const isOpenTask = (state: rootStore.State) => state.tareas.openTask;

//export  const completeTask = ()


export const getArrayData = createSelector(getEntities, getGrupoIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});

export const getTasksStadistics = createSelector(getEntities, taskStats,(entities, otherStats) => {
  let stadistics = [];
  let reserved = 0;
  let ready = 0;
  let canceled = 0;
  let inProgress = 0;
  for (const key in entities) {
    if (entities[key].estado === 'RESERVADO') {
      reserved = reserved + 1;
    } else if (entities[key].estado === 'LISTO') {
      ready = ready + 1;
    } else if (entities[key].estado === 'CANCELADO') {
      canceled = canceled + 1;
    } else if (entities[key].estado === 'ENPROGRESO') {
      inProgress = inProgress + 1;
    }
  }

  stadistics.push({name: 'RESERVADO', value: reserved});
  stadistics.push({name: 'ENPROGRESO', value: inProgress});
  stadistics.push({name: 'LISTO', value: ready});
  stadistics.push({name: 'CANCELADO', value: canceled});
  stadistics = stadistics.concat(otherStats);
  return stadistics;
});


export const getReservedTasksArrayData = createSelector(getEntities, getGrupoIds, (entities, ids) => {
  return ids.map(id => entities[id]).filter(data => data.estado === 'RESERVADO');
});

export const getReadyTasksArrayData = createSelector(getEntities, getGrupoIds, (entities, ids) => {
  return ids.map(id => entities[id]).filter(data => data.estado === 'LISTO');
});

export const getInProgressTasksArrayData = createSelector(getEntities, getGrupoIds, (entities, ids) => {
  return ids.map(id => entities[id]).filter(data => data.estado === 'ENPROGRESO');
});

export const getCanceledTasksArrayData = createSelector(getEntities, getGrupoIds, (entities, ids) => {
  return ids.map(id => entities[id]).filter(data => data.estado === 'CANCELADO');
});
