import {State} from './admin-layout-reducers';
import { createSelector } from 'reselect';
import * as rootStore from 'app/infrastructure/redux-store/redux-reducers';
import * as procesoSelectors from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-selectors';

// External Modules Selector Bound to this container component
const loginStore = (state: rootStore.State) => state.auth;
export const IsAuthenticated = createSelector(loginStore, (state: any) => state.isAuthenticated);

const adminLayoutStore = (state: rootStore.State) => state.adminLayout;

export const LayoutMode = createSelector(adminLayoutStore, (state: State) => state.layoutMode);
export const ProfileMode = createSelector(adminLayoutStore, (state: State) => state.profileMode);
export const DarkMenu = createSelector(adminLayoutStore, (state: State) => state.darkMenu);
export const layoutWidth = createSelector(adminLayoutStore, (state: State) => state.windowWidth);
export const layoutHeight = createSelector(adminLayoutStore, (state: State) => state.windowHeight);


