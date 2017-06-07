import {Action} from '@ngrx/store';
import {type} from 'app/redux-store/_util';

export const ActionTypes = {
  CHANGE_MENU_ORIENTATION: type('[Layout] ChangeMenuOrientationAction Dispatch'),
};

export class ChangeMenuOrientationAction implements Action {
  type = ActionTypes.CHANGE_MENU_ORIENTATION;

  constructor(public payload?: any) {
  }
}



export type Actions =
  ChangeMenuOrientationAction
  ;


