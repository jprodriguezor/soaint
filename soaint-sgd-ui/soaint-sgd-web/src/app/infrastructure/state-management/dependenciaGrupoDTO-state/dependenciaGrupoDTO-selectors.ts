
import {State} from './dependenciaGrupoDTO-reducers';
import {createSelector} from 'reselect';
import * as rootStore from 'app/infrastructure/redux-store/redux-reducers';

const rootPath = (state: rootStore.State) => state.dependenciaGrupo;

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

export const getSelectedId = createSelector(rootPath, (state: State) => state.selectedId);

export const getSelectedEntity =
  createSelector(getEntities, getSelectedId, (entities, selectedId) => {
    return entities[selectedId];
  });

export const getArrayData = createSelector(getEntities, getGrupoIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});
