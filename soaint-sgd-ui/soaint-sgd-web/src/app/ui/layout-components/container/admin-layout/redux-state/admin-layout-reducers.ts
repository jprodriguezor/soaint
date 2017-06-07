import {ActionTypes, Actions} from './admin-layout-actions';
import * as models from '../models/admin-layout.model';
import {tassign} from 'tassign';

export interface State {
  layoutMode: models.MenuOrientation;
  darkMenu: boolean;
  profileMode: models.ProfileMode;
  // staticMenuDesktopInactive: boolean;
  // staticMenuMobileActive: boolean;
  // menuClick: boolean;
  // topbarItemClick: boolean;
  // activeTopbarItem: any;
  // resetMenu: boolean;
}

const initialState: State = {
  layoutMode: models.MenuOrientation.STATIC,
  darkMenu: false,
  profileMode: 'inline'
  // staticMenuDesktopInactive: true,
  // staticMenuMobileActive: true,
  // menuClick: boolean,
  // topbarItemClick: boolean,
  // activeTopbarItem: any,
  // resetMenu: boolean
}

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {
  switch (action.type) {

    case ActionTypes.CHANGE_MENU_ORIENTATION:
      return tassign(state, {
        layoutMode: action.payload.menuOrientation
      });

    default:
      return state;
  }
}


