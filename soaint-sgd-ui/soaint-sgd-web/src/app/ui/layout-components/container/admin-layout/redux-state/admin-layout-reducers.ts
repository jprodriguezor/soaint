import {ActionTypes, Actions} from './admin-layout-actions';
import * as models from '../models/admin-layout.model';
import {tassign} from 'tassign';

export interface State {
  layoutMode: models.MenuOrientation;
  darkMenu: boolean;
  profileMode: models.ProfileMode;
  windowWidth: number;
  windowHeight: number;
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
  profileMode: 'inline',
  windowWidth: window.screen.width,
  windowHeight: window.screen.height

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

    case ActionTypes.RESIZE_WINDOW_ACTION:
      const height: number = action.payload['height'];
      const width: number = action.payload['width'];
      return tassign(state, {windowHeight: height, windowWidth: width});

    default:
      return state;
  }
}


