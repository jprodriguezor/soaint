import {Action} from '@ngrx/store';

import {type} from 'app/infrastructure/redux-store/redux-util';

export const ActionTypes = {

  FILTER: type('[unidadDocumentalDTO] FilterAction'),
  FILTER_COMPLETE: type('[unidadDocumentalDTO] FilterCompleteAction'),
  LOAD: type('[unidadDocumentalDTO] LoadAction'),
  RELOAD: type('[unidadDocumentalDTO] ReloadAction') ,
  LOAD_SUCCESS: type('[unidadDocumentalDTO] LoadSuccessAction'),
  LOAD_FAIL: type('[unidadDocumentalDTO] LoadFailAction'),
  SELECT: type('[unidadDocumentalDTO] SelectAction'),
};

