import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/_util';

export const ActionTypes = {
  FILTER: type('[comunicacionOficialDTO] FilterAction'),
  FILTER_COMPLETE: type('[comunicacionOficialDTO] FilterCompleteAction'),
  LOAD: type('[comunicacionOficialDTO] LoadAction'),
  RELOAD: type('[comunicacionOficialDTO] ReloadoadAction'),
  LOAD_SUCCESS: type('[comunicacionOficialDTO] LoadSuccessAction'),
  LOAD_FAIL: type('[comunicacionOficialDTO] LoadFailAction'),
  SELECT: type('[comunicacionOficialDTO] SelectAction')
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

export class ReloadAction implements Action {
  type = ActionTypes.RELOAD;

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

export class SelectAction implements Action {
  type = ActionTypes.SELECT;

  constructor(public payload?: any) {
  }
}


export type Actions = FilterAction | LoadAction | LoadSuccessAction | LoadFailAction | SelectAction | ReloadAction;


