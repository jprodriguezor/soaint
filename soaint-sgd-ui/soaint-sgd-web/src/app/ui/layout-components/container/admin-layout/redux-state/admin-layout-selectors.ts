import {State} from './admin-layout-reducers';
import { createSelector } from 'reselect';
import * as rootStore from 'app/redux-store/redux-store';

// External Modules Selector Bound to this container component
const loginStore = (state: rootStore.State) => state.auth;
export const IsAuthenticated = createSelector(loginStore, (state: any) => state.isAuthenticated);

const adminLayoutStore = (state: rootStore.State) => state.adminLayout;

export const LayoutMode = createSelector(adminLayoutStore, (state: State) => state.layoutMode);
export const ProfileMode = createSelector(adminLayoutStore, (state: State) => state.profileMode);
export const DarkMenu = createSelector(adminLayoutStore, (state: State) => state.darkMenu);


