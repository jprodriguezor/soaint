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

export const getSedeAdministrativaEntities = createSelector(rootPath, (state: State) => state.sedeAdministrativa.entities);

export const getSedeAdministrativaIds = createSelector(rootPath, (state: State) => state.sedeAdministrativa.ids);

export const getSedeAdministrativaSelectedId = createSelector(rootPath, (state: State) => state.sedeAdministrativa.selectedId);

export const getSedeAdministrativaSelectedEntity =
  createSelector(getSedeAdministrativaEntities, getSedeAdministrativaSelectedId, (entities, selectedId) => {
    return entities[selectedId];
  });

export const getSedeAdministrativaArrayData = createSelector(getSedeAdministrativaEntities, getSedeAdministrativaIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});



