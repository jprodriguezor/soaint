import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/_util';

export const ActionTypes = {
  ASSIGN: type('[asignacionDTO] AssignAction'),
  ASSIGN_SUCCESS: type('[asignacionDTO] AssignSuccessAction'),
  ASSIGN_FAIL: type('[asignacionDTO] AssignFailAction')
};


export class AssignAction implements Action {
  type = ActionTypes.ASSIGN;

  constructor(public payload?: any) {
  }
}

export class AssignSuccessAction implements Action {
  type = ActionTypes.ASSIGN_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class AssignFailAction implements Action {
  type = ActionTypes.ASSIGN_FAIL;

  constructor(public payload?: any) {
  }
}


export type Actions = AssignAction | AssignSuccessAction | AssignFailAction;


