import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';

import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './radicarComunicaciones-actions';
import {ComunicacionApiService} from '../../api/comunicacionOficial.api.service';
import {sedeDestinatarioEntradaSelector, tipoDestinatarioEntradaSelector} from './radicarComunicaciones-selectors';
import {getArrayData as DependenciaGrupoSelector} from '../dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';

@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _comunicacionOficial: ComunicacionApiService) {
  }

  radicar(payload: any) {
    return this._comunicacionOficial.post(environment.radicarComunicacion_endpoint, payload);
  }

  radicarDispatch(payload: any) {
    this._store.dispatch(new actions.RadicarAction(payload));
  }

  sedeDestinatariEntradaFilterDispatch(payload?: any) {
    this._store.dispatch(new actions.TriggerExcludeSedeRemitenteFromDestinatarioAction(payload));
  }

  tipoDestinatarioEntradaFilterDispatch(payload?: any) {
    this._store.dispatch(new actions.TriggerSelectedDestinatarioOriginalAction(payload));
  }

  sedeDestinatarioEntradaSelector() {
    return this._store.select(sedeDestinatarioEntradaSelector);
  }

  tipoDestinatarioEntradaSelector() {
    return this._store.select(tipoDestinatarioEntradaSelector);
  }

  dependenciaGrupoEntradaSelector() {
    return this._store.select(DependenciaGrupoSelector);
  }

}

