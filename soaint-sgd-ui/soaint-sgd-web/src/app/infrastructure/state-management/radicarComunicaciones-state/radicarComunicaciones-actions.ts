import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/_util';

export const ActionTypes = {
  LOAD: type('[radicarComunicacionesDTO] LoadAction'),
  LOAD_SUCCESS: type('[radicarComunicacionesDTO] LoadSuccessAction'),
  LOAD_FAIL: type('[radicarComunicacionesDTO] LoadFailAction'),
  RADICAR: type('[radicarComunicacionesDTO] RadicarAction'),
  RADICAR_SUCCESS: type('[radicarComunicacionesDTO] RadicarSuccessAction'),
  RADICAR_FAIL: type('[radicarComunicacionesDTO] RadicarFailAction')
};

export class RadicarAction implements Action {
  type = ActionTypes.RADICAR;

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

export type Actions =
  LoadAction |
  LoadSuccessAction |
  LoadFailAction |
  RadicarAction |
  RadicarSuccessAction |
  RadicarFailAction
  ;


