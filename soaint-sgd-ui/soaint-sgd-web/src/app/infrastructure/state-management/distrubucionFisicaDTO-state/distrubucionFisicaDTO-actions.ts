import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/redux-util';

export const ActionTypes = {
  FILTER: type('[distribucioFisicaDTO] FilterAction'),
  FILTER_COMPLETE: type('[distribucioFisicaDTO] FilterCompleteAction'),
  LOAD: type('[distribucioFisicaDTO] LoadAction'),
  RELOAD: type('[distribucioFisicaDTO] ReloadoadAction'),
  LOAD_SUCCESS: type('[distribucioFisicaDTO] LoadSuccessAction'),
  LOAD_FAIL: type('[distribucioFisicaDTO] LoadFailAction'),
  SELECT: type('[distribucioFisicaDTO] SelectAction')
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


