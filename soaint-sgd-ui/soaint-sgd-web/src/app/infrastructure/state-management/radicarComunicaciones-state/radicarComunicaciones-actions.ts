import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/_util';

export const ActionTypes = {
  RADICAR: type('[comunicacionOficialDTO] RadicarAction'),
  RADICAR_SUCCESS: type('[comunicacionOficialDTO] RadicarSuccessAction'),
  RADICAR_FAIL: type('[comunicacionOficialDTO] RadicarFailAction')
};

export class RadicarAction implements Action {
  type = ActionTypes.RADICAR;

  constructor(public payload?: any) {
  }
}


export class RadicarSuccessAction implements Action {
  type = ActionTypes.RADICAR_SUCCESS;

  constructor(public payload?: any) {
  }
}

export class RadicarFailAction implements Action {
  type = ActionTypes.RADICAR_FAIL;

  constructor(public payload?: any) {
  }
}


export type Actions = RadicarAction;


