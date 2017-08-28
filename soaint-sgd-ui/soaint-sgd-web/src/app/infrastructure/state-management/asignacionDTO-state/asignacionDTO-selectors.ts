import {State} from './asignacionDTO-reducers';
import {createSelector} from 'reselect';
import * as rootStore from 'app/infrastructure/redux-store/redux-reducers';

const rootPath = (state: rootStore.State) => state.asignaciones;

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

export const getJustificationDialogVisible = createSelector(rootPath, (state: State) => state.justificationDialogVisible);

export const getAgragarObservacionesDialogVisible = createSelector(rootPath, (state: State) => state.agregarObservacionesDialogVisible);

export const getRejectDialogVisible = createSelector(rootPath, (state: State) => state.rejectDialogVisible);

export const getDetailsDialogVisible = createSelector(rootPath, (state: State) => state.detailsDialogVisible);


export const getArrayData = createSelector(getEntities, getGrupoIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});
