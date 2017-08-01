import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/_util';

export const ActionTypes = {
  ASSIGN: type('[asignacionDTO] AssignAction'),
  ASSIGN_SUCCESS: type('[asignacionDTO] AssignSuccessAction'),
  ASSIGN_FAIL: type('[asignacionDTO] AssignFailAction'),
  REDIRECT: type('[asignacionDTO] RedirectAction'),
  REDIRECT_SUCCESS: type('[asignacionDTO] RedirectSuccessAction'),
  REDIRECT_FAIL: type('[asignacionDTO] RedirectFailAction'),
  SET_JUSTIF_DIALOG_VISIBLE: type('[asignacionDTO] SetJustificationDialogVisibleAction'),
  SET_ADD_OBSERV_DIALOG_VISIBLE: type('[asignacionDTO] SetAddObservationsDialogVisibleAction'),
  SET_REJECT_DIALOG_VISIBLE: type('[asignacionDTO] SetRejectDialogVisibleAction')
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

export class RedirectAction implements Action {
  type = ActionTypes.REDIRECT;

  constructor(public payload?: any) {
  }
}

export class RedirectSuccessAction implements Action {
  type = ActionTypes.REDIRECT_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class RedirectFailAction implements Action {
  type = ActionTypes.REDIRECT_FAIL;

  constructor(public payload?: any) {
  }
}

export class SetJustificationDialogVisibleAction implements Action {
  type = ActionTypes.SET_JUSTIF_DIALOG_VISIBLE;

  constructor(public payload?: boolean) {
  }
}

export class SetAddObservationsDialogVisibleAction implements Action {
  type = ActionTypes.SET_ADD_OBSERV_DIALOG_VISIBLE;

  constructor(public payload?: boolean) {
  }
}

export class SetRejectDialogVisibleAction implements Action {
  type = ActionTypes.SET_REJECT_DIALOG_VISIBLE;

  constructor(public payload?: boolean) {
  }
}


export type Actions =
  AssignAction
  | AssignSuccessAction
  | AssignFailAction
  | RedirectAction
  | RedirectSuccessAction
  | RedirectFailAction
  | SetJustificationDialogVisibleAction
  | SetAddObservationsDialogVisibleAction
  | SetRejectDialogVisibleAction;


