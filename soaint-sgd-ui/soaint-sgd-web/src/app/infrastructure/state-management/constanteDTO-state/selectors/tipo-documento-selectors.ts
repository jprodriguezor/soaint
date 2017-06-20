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

export const getTipoDocumentoEntities = createSelector(rootPath, (state: State) => state.tipoDocumento.entities);

export const getTipoDocumentoIds = createSelector(rootPath, (state: State) => state.tipoDocumento.ids);

export const getTipoDocumentoSelectedId = createSelector(rootPath, (state: State) => state.tipoDocumento.selectedId);

export const getTipoDocumentoSelectedEntity =
  createSelector(getTipoDocumentoEntities, getTipoDocumentoSelectedId, (entities, selectedId) => {
    return entities[selectedId];
  });

export const getTipoDocumentoArrayData = createSelector(getTipoDocumentoEntities, getTipoDocumentoIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});



