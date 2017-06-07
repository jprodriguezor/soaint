import {State} from './login-reducers';
import { createSelector } from 'reselect';
import * as rootStore from 'app/redux-store/redux-store';

const loginStore = (state: rootStore.State) => state.auth;

export const getToken = createSelector(loginStore, (state: State) => state.token);
export const getError = createSelector(loginStore, (state: State) => state.error);
export const isLoading = createSelector(loginStore, (state: State) => state.isLoading);
export const isAuthenticated = createSelector(loginStore, (state: State) => state.isAuthenticated);
