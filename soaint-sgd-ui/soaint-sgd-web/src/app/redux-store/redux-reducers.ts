import * as fromRouter from '@ngrx/router-store';
import * as loginStore from 'app/ui/page-components/login/redux-state/login-reducers';
import * as adminLayoutStore from 'app/ui/layout-components/container/admin-layout/redux-state/admin-layout-reducers';

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
    router: fromRouter.routerReducer,
};
