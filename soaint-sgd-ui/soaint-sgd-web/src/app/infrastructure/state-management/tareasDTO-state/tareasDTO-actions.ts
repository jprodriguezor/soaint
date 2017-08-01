import { Action } from '@ngrx/store';
import { type } from 'app/infrastructure/redux-store/_util';

export const ActionTypes = {
    FILTER: type('[TareaDTO] FilterAction'),
    FILTER_COMPLETE: type('[TareaDTO] FilterCompleteAction'),
    LOAD: type('[TareaDTO] LoadAction'),
    LOAD_SUCCESS: type('[TareaDTO] LoadSuccessAction'),
    LOAD_FAIL: type('[TareaDTO] LoadFailAction'),
    START_TASK: type('[TareaDTO] StartTaskAction'),
    START_TASK_SUCCESS: type('[TareaDTO] StartTaskSuccessAction'),
    START_TASK_FAIL: type('[TareaDTO] StartTaskFailAction'),
    COMPLETE_TASK: type('[TareaDTO] CompleteTaskAction'),
    COMPLETE_TASK_SUCCESS: type('[TareaDTO] CompleteTaskSuccessAction'),
    COMPLETE_TASK_FAIL: type('[TareaDTO] CompleteTaskFailAction'),

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

export class StartTaskSuccessAction implements Action {
  type = ActionTypes.START_TASK_SUCCESS;
  constructor(public payload?: any) { }
}

export class StartTaskFailAction implements Action {
  type = ActionTypes.START_TASK_FAIL;
  constructor(public payload?: any) { }
}

export class CompleteTaskAction implements Action {
  type = ActionTypes.COMPLETE_TASK;
  constructor(public payload?: any) { }
}

export class CompleteTaskSuccessAction implements Action {
  type = ActionTypes.COMPLETE_TASK_SUCCESS;
  constructor(public payload?: any) { }
}

export class CompleteTaskFailAction implements Action {
  type = ActionTypes.COMPLETE_TASK_FAIL;
  constructor(public payload?: any) { }
}

export type Actions =
  FilterAction |
  LoadAction |
  LoadSuccessAction |
  LoadFailAction |
  StartTaskAction |
  StartTaskSuccessAction |
  StartTaskFailAction |
  CompleteTaskAction |
  CompleteTaskSuccessAction |
  CompleteTaskFailAction
  ;


