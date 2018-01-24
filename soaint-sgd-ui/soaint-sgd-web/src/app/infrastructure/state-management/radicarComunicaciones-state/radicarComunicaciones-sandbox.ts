import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';

import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './radicarComunicaciones-actions';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {ApiBase} from '../../api/api-base';

@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
  }

  radicar(payload: any) {
    return this._api.post(environment.radicarComunicacion_endpoint, payload);
  }

  quickSave(payload: any) {
    return this._api.post(environment.salvarCorrespondenciaEntrada_endpoint, payload);
  }
  quickRestore(idproceso: string, idtarea: string) {
    const endpoint = environment.restablecerCorrespondenciaEntrada_endpoint;
    return this._api.list(`${endpoint}/${idproceso}/${idtarea}`);
  }

  dispatchRadicarComunicacion(payload: ComunicacionOficialDTO) {
    this._store.dispatch(new actions.RadicarAction(payload));
  }

  dispatchSedeDestinatarioEntradaFilter(payload?: any) {
    this._store.dispatch(new actions.TriggerExcludeSedeRemitenteFromDestinatarioAction(payload));
  }



}

