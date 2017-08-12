import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/redux-util';

export const ActionTypes = {
  LOAD: type('[radicarComunicacionesDTO] LoadAction'),
  LOAD_SUCCESS: type('[radicarComunicacionesDTO] LoadSuccessAction'),
  LOAD_FAIL: type('[radicarComunicacionesDTO] LoadFailAction'),
  RADICAR: type('[radicarComunicacionesDTO] RadicarAction'),
  RADICAR_SUCCESS: type('[radicarComunicacionesDTO] RadicarSuccessAction'),
  RADICAR_FAIL: type('[radicarComunicacionesDTO] RadicarFailAction'),
  TRIGGER_EXCLUDE_SEDE_REMITENTE_FROM_DESTINATARIO: type('[radicarComunicacionesDTO] TriggerExcludeSedeRemitenteFromDestinatarioAction'),
  TRIGGER_SELECTED_DESTINATARIO_ORIGINAL: type('[radicarComunicacionesDTO] TriggerSelectedDestinatarioOriginalAction')
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

export class TriggerExcludeSedeRemitenteFromDestinatarioAction implements Action {
  type = ActionTypes.TRIGGER_EXCLUDE_SEDE_REMITENTE_FROM_DESTINATARIO;

  constructor(public payload?: any) {
  }
}

export class TriggerSelectedDestinatarioOriginalAction implements Action {
  type = ActionTypes.TRIGGER_SELECTED_DESTINATARIO_ORIGINAL;

  constructor(public payload?: any) {
  }
}

export type Actions =
  LoadAction |
  LoadSuccessAction |
  LoadFailAction |
  RadicarAction |
  RadicarSuccessAction |
  RadicarFailAction |
  TriggerExcludeSedeRemitenteFromDestinatarioAction |
  TriggerSelectedDestinatarioOriginalAction
  ;


