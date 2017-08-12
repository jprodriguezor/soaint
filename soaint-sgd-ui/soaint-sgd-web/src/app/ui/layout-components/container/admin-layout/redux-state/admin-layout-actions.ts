import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/redux-util';

export const ActionTypes = {
  CHANGE_MENU_ORIENTATION: type('[Layout] ChangeMenuOrientationAction Dispatch'),
  RESIZE_WINDOW_ACTION: type('[Layout] ResizeWindowAction Dispatch')
};

export class ChangeMenuOrientationAction implements Action {
  type = ActionTypes.CHANGE_MENU_ORIENTATION;

  constructor(public payload?: any) {
  }
}

export class ResizeWindowAction implements Action {
  type = ActionTypes.RESIZE_WINDOW_ACTION;

  constructor(public payload?: any) {
  }
}


export type Actions =
  ChangeMenuOrientationAction |
  ResizeWindowAction
  ;


