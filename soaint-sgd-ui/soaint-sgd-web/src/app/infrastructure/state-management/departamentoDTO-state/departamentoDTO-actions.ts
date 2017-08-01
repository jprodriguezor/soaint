import { Action } from '@ngrx/store';
import { type } from 'app/infrastructure/redux-store/_util';

export const ActionTypes = {
    FILTER: type('[departamentoDTO] FilterAction'),
    FILTER_COMPLETE: type('[departamentoDTO] FilterCompleteAction'),
    LOAD: type('[departamentoDTO] LoadAction'),
    LOAD_SUCCESS: type('[departamentoDTO] LoadSuccessAction'),
    LOAD_FAIL: type('[departamentoDTO] LoadFailAction'),
    SELECT: type('[departamentoDTO] SelectAction')
};


export class FilterAction implements Action {
  type = ActionTypes.FILTER;
  constructor(public payload?: any) { }
}

export class LoadAction implements Action {
  type = ActionTypes.LOAD;
  constructor(public payload?: any) { }
}

export class LoadSuccessAction implements Action {
  type = ActionTypes.LOAD_SUCCESS;
  constructor(public payload?:  any) { }
}

export class LoadFailAction implements Action {
  type = ActionTypes.LOAD_FAIL;
  constructor(public payload?: any) { }
}

export class SelectAction implements Action {
  type = ActionTypes.SELECT;
  constructor(public payload?: any) { }
}


export type Actions =
  FilterAction |
  LoadAction |
  LoadSuccessAction |
  LoadFailAction |
  SelectAction
  ;


