import {State} from '../constanteDTO-reducers';
import {createSelector} from 'reselect';
import * as rootStore from 'app/infrastructure/redux-store/redux-reducers';

const rootPath = (state: rootStore.State) => state.constantes;

/**
 * Because the data structure is defined within the reducer it is optimal to
 * locate our selector functions at this level. If store is to be thought of
 * as a database, and reducers the tables, selectors can be considered the
 * queries into said database. Remember to keep your selectors small and
 * focused so they can be combined and composed to fit each particular
 * use-case.
 */

export const getTratamientoCortesiaEntities = createSelector(rootPath, (state: State) => state.tratamientoCortesia.entities);

export const getTratamientoCortesiaIds = createSelector(rootPath, (state: State) => state.tratamientoCortesia.ids);

export const getTratamientoCortesiaSelectedId = createSelector(rootPath, (state: State) => state.tratamientoCortesia.selectedId);

export const getTratamientoCortesiaSelectedEntity =
  createSelector(getTratamientoCortesiaEntities, getTratamientoCortesiaSelectedId, (entities, selectedId) => {
    return entities[selectedId];
  });

export const getTratamientoCortesiaArrayData = createSelector(getTratamientoCortesiaEntities, getTratamientoCortesiaIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});



