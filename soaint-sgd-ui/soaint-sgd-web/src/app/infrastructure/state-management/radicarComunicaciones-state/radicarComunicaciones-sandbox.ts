import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';

import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './radicarComunicaciones-actions';
import {ComunicacionApiService} from '../../api/comunicacionOficial.api.service';
import {sedeDestinatarioEntradaSelector, tipoDestinatarioEntradaSelector} from './radicarComunicaciones-selectors';
import {getArrayData as DependenciaGrupoSelector} from '../dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';

@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _comunicacionOficial: ComunicacionApiService) {
  }

  radicar(payload: any) {
    return this._comunicacionOficial.post(environment.radicarComunicacion_endpoint, payload);
  }

  dispatchRadicarComunicacion(payload: ComunicacionOficialDTO) {
    this._store.dispatch(new actions.RadicarAction(payload));
  }

  dispatchSedeDestinatarioEntradaFilter(payload?: any) {
    this._store.dispatch(new actions.TriggerExcludeSedeRemitenteFromDestinatarioAction(payload));
  }

}

