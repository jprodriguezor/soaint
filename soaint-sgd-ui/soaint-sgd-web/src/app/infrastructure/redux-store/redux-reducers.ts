import * as fromRouter from '@ngrx/router-store';
import * as loginStore from 'app/ui/page-components/login/redux-state/login-reducers';
import * as adminLayoutStore from 'app/ui/layout-components/container/admin-layout/redux-state/admin-layout-reducers';
import * as constantesStore from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-reducers';
import * as procesoStore from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-reducers';
import * as paisStore from 'app/infrastructure/state-management/paisDTO-state/paisDTO-reducers';
import * as municipioStore from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-reducers';
import * as departamentoStore from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-reducers';
import * as correspondenciaStore from 'app/infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-reducers';
import * as distribucionStore from 'app/infrastructure/state-management/distrubucionFisicaDTO-state/distrubucionFisicaDTO-reducers';
import * as dependenciaGrupoStore from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-reducers';
import * as sedeAdministrativaStore from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-reducers';
import * as tareasStore from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-reducers';
import * as comunicacionOficialStore from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-reducers';
import * as funcionarioStore from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-reducers';
import * as asignacionStore from 'app/infrastructure/state-management/asignacionDTO-state/asignacionDTO-reducers';
import * as cargarPlanillasStore from 'app/infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-reducers';
import * as notificationStore from 'app/infrastructure/state-management/notifications-state/notifications-reducers';

/**
 * As mentioned, we treat each reducer like a table in a database. This means
 * our top level state interface is just a map of keys to inner state types.
 */
export interface State {
  auth: loginStore.State
  adminLayout: adminLayoutStore.State,
  constantes: constantesStore.State,
  paises: paisStore.State,
  municipios: municipioStore.State,
  departamentos: departamentoStore.State,
  comunicacionesOficiales: correspondenciaStore.State,
  distribucionFisica: distribucionStore.State,
  cargarPlanillas: cargarPlanillasStore.State,
  asignaciones: asignacionStore.State,
  dependenciaGrupo: dependenciaGrupoStore.State,
  sedeAdministrativa: sedeAdministrativaStore.State,
  tareas: tareasStore.State,
  proceso: procesoStore.State,
  funcionario: funcionarioStore.State,
  radicarComunicacion: comunicacionOficialStore.State,
  notifications: notificationStore.State,
  router: fromRouter.RouterState
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
  comunicacionesOficiales: correspondenciaStore.reducer,
  distribucionFisica: distribucionStore.reducer,
  cargarPlanillas: cargarPlanillasStore.reducer,
  asignaciones: asignacionStore.reducer,
  dependenciaGrupo: dependenciaGrupoStore.reducer,
  sedeAdministrativa: sedeAdministrativaStore.reducer,
  tareas: tareasStore.reducer,
  funcionario: funcionarioStore.reducer,
  radicarComunicacion: comunicacionOficialStore.reducer,
  notifications: notificationStore.reducer,
  router: fromRouter.routerReducer,
};


