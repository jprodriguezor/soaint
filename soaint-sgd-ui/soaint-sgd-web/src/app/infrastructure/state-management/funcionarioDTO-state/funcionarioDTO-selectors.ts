import 'rxjs/add/operator/reduce';
import * as rootStore from 'app/infrastructure/redux-store/redux-reducers';
import {createSelector} from 'reselect';
import {State} from './funcionarioDTO-reducers';

const rootPath = (state: rootStore.State) => state.funcionario;

/**
 * Because the data structure is defined within the reducer it is optimal to
 * locate our selector functions at this level. If store is to be thought of
 * as a database, and reducers the tables, selectors can be considered the
 * queries into said database. Remember to keep your selectors small and
 * focused so they can be combined and composed to fit each particular
 * use-case.
 */

export const getAuthenticatedFuncionario = createSelector(rootPath, (state: State) => state.authenticatedFuncionario);

export const getEntities = createSelector(rootPath, (state: State) => state.entities);

export const getGrupoIds = createSelector(rootPath, (state: State) => state.ids);

export const getArrayData = createSelector(getEntities, getGrupoIds, (entities, ids) => {
  return ids.map(id => entities[id]);
});

export const getSuggestionsDependencyGroupFuncionarioArray = createSelector(rootPath, (state: State) => state.authenticatedFuncionario ? state.authenticatedFuncionario.dependencias : []);

export const getFirstDependencyGroupFuncionario = createSelector(rootPath, (state: State) => state.authenticatedFuncionario ? state.authenticatedFuncionario.dependencias[0] : null);

export const getSelectedDependencyGroupFuncionario = createSelector(rootPath, (state: State) => state.selectedDependencyGroup);


