import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/_util';
import {OrganigramaDTO} from '../../../domain/organigramaDTO';

export const ActionTypes = {
  LOAD: type('[FuncionarioDTO] LoadAction'),
  LOAD_SUCCESS: type('[FuncionarioDTO] LoadSuccessAction'),
  LOAD_FAIL: type('[FuncionarioDTO] LoadFailAction'),

  LOAD_ALL: type('[FuncionarioDTO] LoadAllAction'),
  LOAD_ALL_SUCCESS: type('[FuncionarioDTO] LoadAllSuccessAction'),
  LOAD_ALL_FAIL: type('[FuncionarioDTO] LoadAllFailAction'),

  SELECT_DEPENDENCY_GROUP: type('[FuncionarioDTO] SelectDependencyGroupAction'),
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

export class LoadAllAction implements Action {
  type = ActionTypes.LOAD_ALL;

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

  constructor(public payload?: OrganigramaDTO) {
  }
}


export type Actions =
  LoadAction |
  LoadSuccessAction |
  LoadFailAction |
  LoadAllAction |
  LoadAllFailAction |
  LoadAllSuccessAction |
  SelectDependencyGroupAction
  ;


