import {State} from './radicarComunicaciones-reducers';
import {createSelector} from 'reselect';
import * as rootStore from 'app/infrastructure/redux-store/redux-reducers';
import {getGrupoIds as rootSedeIds, getEntities as rootSedeEntities} from '../sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {getTipoDestinatarioEntities, getTipoDestinatarioIds} from '../constanteDTO-state/constanteDTO-selectors';

const rootPath = (state: rootStore.State) => state.radicarComunicacion;

/**
 * Because the data structure is defined within the reducer it is optimal to
 * locate our selector functions at this level. If store is to be thought of
 * as a database, and reducers the tables, selectors can be considered the
 * queries into said database. Remember to keep your selectors small and
 * focused so they can be combined and composed to fit each particular
 * use-case.
 */

export const correspondenciaEntrada = createSelector(rootPath, (state: State) => state.entrada.correspondencia);

export const sedeRemitente = createSelector(rootPath, (state: State) => state.entrada.sedeRemitente);
export const destinatarioOriginal = createSelector(rootPath, (state: State) => state.entrada.destinatarioOriginal);


export const sedeDestinatarioEntradaSelector = createSelector(rootSedeIds, rootSedeEntities, sedeRemitente, (ids, entities, sedeRem) => {
  sedeRem = sedeRem || {};
  return ids.filter(id => id !== sedeRem.id).map(id => entities[id]);
});

export const tipoDestinatarioEntradaSelector = createSelector(getTipoDestinatarioIds, getTipoDestinatarioEntities, destinatarioOriginal, (ids, entities, desOrig) => {
  desOrig = desOrig || {};
  return ids.filter(id => id !== desOrig.id).map(id => entities[id]);
});
