import { Action } from '@ngrx/store';
import { type } from 'app/infrastructure/redux-store/_util';

export const ActionTypes = {
    FILTER: type('[dependenciaGrupoDTO] FilterAction'),
    FILTER_COMPLETE: type('[dependenciaGrupoDTO] FilterCompleteAction'),
    LOAD: type('[dependenciaGrupoDTO] LoadAction'),
    LOAD_SUCCESS: type('[dependenciaGrupoDTO] LoadSuccessAction'),
    LOAD_FAIL: type('[dependenciaGrupoDTO] LoadFailAction'),
    SELECT: type('[dependenciaGrupoDTO] SelectAction')
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


