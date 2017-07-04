import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/_util';

export const ActionTypes = {
  LOAD: type('[FuncionarioDTO] LoadAction'),
  LOAD_SUCCESS: type('[FuncionarioDTO] LoadSuccessAction'),
  LOAD_FAIL: type('[FuncionarioDTO] LoadFailAction'),
};


export class LoadAction implements Action {
  type = ActionTypes.LOAD;

  constructor(public payload?: any) {
  }
}

export class LoadSuccessAction implements Action {
  type = ActionTypes.LOAD_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class LoadFailAction implements Action {
  type = ActionTypes.LOAD_FAIL;

  constructor(public payload?: any) {
  }
}


export type Actions =
  LoadAction |
  LoadSuccessAction |
  LoadFailAction
  ;


