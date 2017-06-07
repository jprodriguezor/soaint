/**
 * Every reducer module's default export is the reducer function itself. In
 * addition, each module should export a type or interface that describes
 * the state of the reducer plus any selector functions. The `* as`
 * notation packages up all of the exports into a single object.
 */
import * as fromRouter from '@ngrx/router-store';
import * as loginStore from 'app/ui/page-components/login/redux-state/login-reducers';
import * as adminLayoutStore from 'app/ui/layout-components/container/admin-layout/redux-state/admin-layout-reducers';

/**
 * As mentioned, we treat each reducer like a table in a database. This means
 * our top level state interface is just a map of keys to inner state types.
 */
export interface State {
  auth: loginStore.State
  adminLayout: adminLayoutStore.State,
  router: fromRouter.RouterState;
}

