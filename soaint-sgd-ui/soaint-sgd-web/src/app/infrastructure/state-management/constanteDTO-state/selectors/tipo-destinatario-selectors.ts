import {State} from '../constanteDTO-reducers';
import {createSelector} from 'reselect';
import * as rootStore from 'app/infrastructure/redux-store/redux-reducers';
import {DESTINATARIO_PRINCIPAL} from '../../../../shared/bussiness-properties/radicacion-properties';

const rootPath = (state: rootStore.State) => state.constantes;

/**
 * Because the data structure is defined within the reducer it is optimal to
 * locate our selector functions at this level. If store is to be thought of
 * as a database, and reducers the tables, selectors can be considered the
 * queries into said database. Remember to keep your selectors small and
 * focused so they can be combined and composed to fit each particular
 * use-case.
 */

export const getTipoDestinatarioEntities = createSelector(rootPath, (state: State) => state.tipoDestinatario.entities);

export const getTipoDestinatarioIds = createSelector(rootPath, (state: State) => state.tipoDestinatario.ids);

export const getTipoDestinatarioSelectedId = createSelector(rootPath, (state: State) => state.tipoDestinatario.selectedId);

// Get Tipo destinatario principal id=> 52
export const getDestinatarioPrincial = createSelector(getTipoDestinatarioEntities, (entities) => {
  return entities[DESTINATARIO_PRINCIPAL];
});

export const getTipoDestinatarioArrayData = createSelector(getTipoDestinatarioEntities, getTipoDestinatarioIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});



