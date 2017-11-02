import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/redux-util';

export const ActionTypes = {
  LOAD_SUCCESS: type('[FuncionarioDTO] LoadSuccessAction'),
  LOAD_ALL: type('[FuncionarioDTO] LoadAllAction'),
  LOAD_ALL_BY_ROL: type('[FuncionarioDTO] LoadAllByRolAction'),
  LOAD_ALL_SUCCESS: type('[FuncionarioDTO] LoadAllSuccessAction'),
  LOAD_ALL_FAIL: type('[FuncionarioDTO] LoadAllFailAction'),

  SELECT_DEPENDENCY_GROUP: type('[FuncionarioDTO] SelectDependencyGroupAction'),
};


export class LoadSuccessAction implements Action {
  type = ActionTypes.LOAD_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class LoadAllAction implements Action {
  type = ActionTypes.LOAD_ALL;

  constructor(public payload?: any) {
  }
}

export class LoadAllByRolAction implements Action {
  type = ActionTypes.LOAD_ALL_BY_ROL;

  constructor(public payload?: any) {
  }
}

export class LoadAllSuccessAction implements Action {
  type = ActionTypes.LOAD_ALL_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class LoadAllFailAction implements Action {
  type = ActionTypes.LOAD_ALL_FAIL;

  constructor(public payload?: any) {
  }
}


export class SelectDependencyGroupAction implements Action {
  type = ActionTypes.SELECT_DEPENDENCY_GROUP;

  constructor(public payload?: any) {
  }
}

export type Actions =
  LoadSuccessAction |
  LoadAllAction |
  LoadAllFailAction |
  LoadAllSuccessAction |
  LoadAllByRolAction |
  SelectDependencyGroupAction
  ;


