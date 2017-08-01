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

export const getTipoTelefonoEntities = createSelector(rootPath, (state: State) => state.tipoTelefono.entities);

export const getTipoTelefonoIds = createSelector(rootPath, (state: State) => state.tipoTelefono.ids);

export const getTipoTelefonoSelectedId = createSelector(rootPath, (state: State) => state.tipoTelefono.selectedId);

export const getTipoTelefonoSelectedEntity =
  createSelector(getTipoTelefonoEntities, getTipoTelefonoSelectedId, (entities, selectedId) => {
    return entities[selectedId];
  });

export const getTipoTelefonoArrayData = createSelector(getTipoTelefonoEntities, getTipoTelefonoIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});



