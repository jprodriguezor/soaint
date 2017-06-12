import {State} from '../constanteDTO-reducers';
import {createSelector} from 'reselect';
import * as rootStore from 'app/infrastructure/redux-store/redux-reducers';

export const rootPath = (state: rootStore.State) => state.constantes;

/**
 * Because the data structure is defined within the reducer it is optimal to
 * locate our selector functions at this level. If store is to be thought of
 * as a database, and reducers the tables, selectors can be considered the
 * queries into said database. Remember to keep your selectors small and
 * focused so they can be combined and composed to fit each particular
 * use-case.
 */

export const getMediosRecepcionEntities = createSelector(rootPath, (state: State) => state.mediosRecepcion.entities);

export const getMediosRecepcionIds = createSelector(rootPath, (state: State) => state.mediosRecepcion.ids);

export const getMediosRecepcionSelectedId = createSelector(rootPath, (state: State) => state.mediosRecepcion.selectedId);

export const getMediosRecepcionSelectedEntity =
  createSelector(getMediosRecepcionEntities, getMediosRecepcionSelectedId, (entities, selectedId) => {
    return entities[selectedId];
  });

export const getUnidadTiempoArrayData = createSelector(getMediosRecepcionEntities, getMediosRecepcionIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});



