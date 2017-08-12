import { Action } from '@ngrx/store';
import { type } from 'app/infrastructure/redux-store/redux-util';

export const ActionTypes = {
    FILTER: type('[procesoDTO] FilterAction'),
    FILTER_COMPLETE: type('[procesoDTO] FilterCompleteAction'),
    LOAD: type('[procesoDTO] LoadAction'),
    LOAD_SUCCESS: type('[procesoDTO] LoadSuccessAction'),
    LOAD_FAIL: type('[procesoDTO] LoadFailAction'),
    SELECT: type('[procesoDTO] SelectAction'),
    START_PROCESS: type('[procesoDTO] StartProcessAction'),
    LOAD_TASKS_INSIDE_PROCESS: type('[procesoDTO] LoadTasksInsideProcessAction'),
    LOAD_TASK_SUCCESS: type('[procesoDTO] LoadTaksSuccessAction'),
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

export class StartProcessAction implements Action {
  type = ActionTypes.START_PROCESS;
  constructor(public payload?: any) { }
}

export class LoadTasksInsideProcessAction implements Action {
  type = ActionTypes.LOAD_TASKS_INSIDE_PROCESS;
  constructor(public payload?: any) { }
}

export class LoadTaskSuccessAction implements Action {
  type = ActionTypes.LOAD_TASK_SUCCESS;
  constructor(public payload?: any) { }
}

export type Actions =
  FilterAction |
  LoadAction |
  LoadSuccessAction |
  LoadFailAction |
  SelectAction |
  StartProcessAction |
  LoadTasksInsideProcessAction |
  LoadTaskSuccessAction
  ;


