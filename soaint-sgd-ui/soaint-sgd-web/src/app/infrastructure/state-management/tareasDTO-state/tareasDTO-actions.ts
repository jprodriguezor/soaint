import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/redux-util';

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
  CANCEL_TASK: type('[TareaDTO] CancelTaskAction'),
  CANCEL_TASK_SUCCESS: type('[TareaDTO] CancelTaskSuccessAction'),
  CANCEL_TASK_FAIL: type('[TareaDTO] CancelTaskFailAction'),
  LOCK_ACTIVE_TASK: type('[TareaDTO] LockActiveTaskAction'),
  UNLOCK_ACTIVE_TASK: type('[TareaDTO] UnlockActiveTaskAction'),
  SCHEDULE_NEXT_TASK: type('[TareaDTO] ScheduleNextTaskAction'),
  CONTINUE_WITH_NEXT_TASK: type('[TareaDTO] ContinueWithNextTaskAction')
};

export class FilterAction implements Action {
  type = ActionTypes.FILTER;

  constructor(public payload?: any) {
  }
}

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

export class StartTaskAction implements Action {
  type = ActionTypes.START_TASK;

  constructor(public payload?: any) {
  }
}

export class StartTaskSuccessAction implements Action {
  type = ActionTypes.START_TASK_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class StartTaskFailAction implements Action {
  type = ActionTypes.START_TASK_FAIL;

  constructor(public payload?: any) {
  }
}

export class CompleteTaskAction implements Action {
  type = ActionTypes.COMPLETE_TASK;

  constructor(public payload?: any) {
  }
}

export class CompleteTaskSuccessAction implements Action {
  type = ActionTypes.COMPLETE_TASK_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class CompleteTaskFailAction implements Action {
  type = ActionTypes.COMPLETE_TASK_FAIL;

  constructor(public payload?: any) {
  }
}

export class CancelTaskAction implements Action {
  type = ActionTypes.CANCEL_TASK;

  constructor(public payload?: any) {
  }
}

export class CancelTaskSuccessAction implements Action {
  type = ActionTypes.CANCEL_TASK_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class CancelTaskFailAction implements Action {
  type = ActionTypes.CANCEL_TASK_FAIL;

  constructor(public payload?: any) {
  }
}

export class UnlockActiveTaskAction implements Action {
  type = ActionTypes.UNLOCK_ACTIVE_TASK;

  constructor(public payload?: any) {
  }
}

export class LockActiveTaskAction implements Action {
  type = ActionTypes.LOCK_ACTIVE_TASK;

  constructor(public payload?: any) {
  }
}

export class ContinueWithNextTaskAction implements Action {
  type = ActionTypes.CONTINUE_WITH_NEXT_TASK;

  constructor(public payload?: any) {
  }
}

export class ScheduleNextTaskAction implements Action {
  type = ActionTypes.SCHEDULE_NEXT_TASK;

  constructor(public payload?: any) {
  }
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
  CompleteTaskFailAction |
  CancelTaskAction |
  CancelTaskSuccessAction |
  CancelTaskFailAction |
  UnlockActiveTaskAction |
  ContinueWithNextTaskAction |
  ScheduleNextTaskAction
  ;


