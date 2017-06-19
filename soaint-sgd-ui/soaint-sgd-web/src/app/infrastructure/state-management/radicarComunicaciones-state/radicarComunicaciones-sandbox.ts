import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';

import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './radicarComunicaciones-actions';
import {ComunicacionApiService} from '../../api/comunicacionOficial.api.service';

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

}

