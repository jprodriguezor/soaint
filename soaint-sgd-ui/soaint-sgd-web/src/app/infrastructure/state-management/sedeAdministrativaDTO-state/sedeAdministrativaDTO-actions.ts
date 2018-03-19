import { Action } from '@ngrx/store';
import { type } from 'app/infrastructure/redux-store/redux-util';

export const ActionTypes = {
    LOAD: type('[sedeAdministrativa] LoadAction'),
    LOAD_SUCCESS: type('[sedeAdministrativa] LoadSuccessAction'),
    LOAD_FAIL: type('[sedeAdministrativa] LoadFailAction')
};


export class LoadAction implements Action {
  type = ActionTypes.LOAD;

  constructor(public payload?: any) { console.log('Dispatched activity');

  }
}

export class LoadSuccessAction implements Action {
  type = ActionTypes.LOAD_SUCCESS;
  constructor(public payload?:  any) { }
}

export class LoadFailAction implements Action {
  type = ActionTypes.LOAD_FAIL;
  constructor(public payload?: any) { }
}


export type Actions =
  LoadAction |
  LoadSuccessAction |
  LoadFailAction

  ;


