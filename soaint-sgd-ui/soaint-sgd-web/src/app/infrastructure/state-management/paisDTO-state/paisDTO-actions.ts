import { Action } from '@ngrx/store';
import { type } from 'app/infrastructure/redux-store/redux-util';

export const ActionTypes = {
    FILTER: type('[paisDTO] FilterAction'),
    FILTER_COMPLETE: type('[paisDTO] FilterCompleteAction'),
    LOAD: type('[paisDTO] LoadAction'),
    LOAD_SUCCESS: type('[paisDTO] LoadSuccessAction'),
    LOAD_FAIL: type('[paisDTO] LoadFailAction'),
    SELECT: type('[paisDTO] SelectAction')
};


export class FilterAction implements Action {
  type = ActionTypes.FILTER;
  constructor(public payload?: any) { }
}

export class LoadAction implements Action {
  type = ActionTypes.LOAD;

  constructor(public payload?: any) {  }
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


