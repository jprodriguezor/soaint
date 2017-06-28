import { Action } from '@ngrx/store';
import { type } from 'app/infrastructure/redux-store/_util';

export const ActionTypes = {
    FILTER: type('[TareaDTO] FilterAction'),
    FILTER_COMPLETE: type('[TareaDTO] FilterCompleteAction'),
    LOAD: type('[TareaDTO] LoadAction'),
    LOAD_SUCCESS: type('[TareaDTO] LoadSuccessAction'),
    LOAD_FAIL: type('[TareaDTO] LoadFailAction'),
    START_TASK: type('[TareaDTO] StartTaskAction')
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

export class StartTaskAction implements Action {
  type = ActionTypes.START_TASK;
  constructor(public payload?: any) { }
}


export type Actions =
  FilterAction |
  LoadAction |
  LoadSuccessAction |
  LoadFailAction |
  StartTaskAction
  ;


