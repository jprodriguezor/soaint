import { Action } from '@ngrx/store';
import { type } from 'app/infrastructure/redux-store/redux-util';

export const ActionTypes = {
    LOAD: type('[dependenciaGrupoDTO] LoadAction'),
    LOAD_SUCCESS: type('[dependenciaGrupoDTO] LoadSuccessAction'),
    LOAD_FAIL: type('[dependenciaGrupoDTO] LoadFailAction'),

};


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

export type Actions =
  LoadAction |
  LoadSuccessAction |
  LoadFailAction
  ;


