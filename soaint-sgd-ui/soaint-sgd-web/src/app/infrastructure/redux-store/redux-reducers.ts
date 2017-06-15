import * as fromRouter from '@ngrx/router-store';
import * as loginStore from 'app/ui/page-components/login/redux-state/login-reducers';
import * as adminLayoutStore from 'app/ui/layout-components/container/admin-layout/redux-state/admin-layout-reducers';
import * as constantesStore from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-reducers';
import * as procesoStore from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-reducers';
import * as paisStore from 'app/infrastructure/state-management/paisDTO-state/paisDTO-reducers';
import * as municipioStore from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-reducers';
import * as departamentoStore from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-reducers';

/**
 * As mentioned, we treat each reducer like a table in a database. This means
 * our top level state interface is just a map of keys to inner state types.
 */
export interface State {
  auth: loginStore.State
  adminLayout: adminLayoutStore.State,
  // notification: notificationStore.State,
  constantes: constantesStore.State,
  paises: paisStore.State,
  municipios: municipioStore.State,
  departamentos: departamentoStore.State,
  proceso: procesoStore.State,
  router: fromRouter.RouterState;
}


/**
 * Because metareducers take a reducer function and return a new reducer,
 * we can use our compose helper to chain them together. Here we are
 * using combineReducers to make our top level reducer, and then
 * wrapping that in storeLogger. Remember that compose applies
 * the result from right to left.
 */
export const reducers = {
  auth: loginStore.reducer,
  adminLayout: adminLayoutStore.reducer,
  constantes: constantesStore.reducer,
  proceso: procesoStore.reducer,
  paises: paisStore.reducer,
  municipios: municipioStore.reducer,
  departamentos: departamentoStore.reducer,
  router: fromRouter.routerReducer,
};


