import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/redux-util';

export const ActionTypes = {
  FILTER: type('[constanteDTO] FilterAction'),
  FILTER_COMPLETE: type('[constanteDTO] FilterCompleteAction'),
  LOAD: type('[constanteDTO] LoadAction'),
  LOAD_DATOS_GENERALES: type('[constanteDTO] LoadDatosGeneralesAction'),
  LOAD_DATOS_REMITENTE: type('[constanteDTO] LoadDatosRemitenteAction'),
  LOAD_SUCCESS: type('[constanteDTO] LoadSuccessAction'),
  LOAD_FAIL: type('[constanteDTO] LoadFailAction'),
  SELECT: type('[constanteDTO] SelectAction'),
  MULTI_SELECT: type('[constanteDTO] MultiSelectAction'),
  LOAD_CAUSAL_DEVOLUCION: type('[constanteDTO] LoadCausalDevolucionAction'),
  LOAD_DATOS_ENVIO: type('[constanteDTO] LoadDatosEnvioAction')
};

export interface GenericFilterAutocomplete {
  key: string;
  data?: any
}

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

export class LoadDatosGeneralesAction implements Action {
  type = ActionTypes.LOAD_DATOS_GENERALES;

  constructor(public payload?: any) {
  }
}

export class LoadDatosRemitenteAction implements Action {
  type = ActionTypes.LOAD_DATOS_REMITENTE;

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

export class MultiSelectAction implements Action {
  type = ActionTypes.MULTI_SELECT;

  constructor(public payload?: any) {
  }
}

export class LoadCausalDevolucionAction implements Action {
  type = ActionTypes.LOAD_CAUSAL_DEVOLUCION;

  constructor(public payload?: any) {
  }
}

export class LoadDatosEnvioAction implements Action {
  type = ActionTypes.LOAD_DATOS_ENVIO;

  constructor(public payload?: any) {
  }
}

export type Actions =
  FilterAction |
  LoadAction |
  LoadSuccessAction |
  LoadFailAction |
  SelectAction |
  LoadDatosGeneralesAction |
  LoadDatosRemitenteAction |
  LoadCausalDevolucionAction;


