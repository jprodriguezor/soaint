import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './paisDTO-actions';
import {ApiBase} from '../../api/api-base';

@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
  }

  loadData(payload: any) {

    return this._api.list(environment.pais_endpoint, payload)

      .map((response) => {
        return {
          paises: response.paises.sort((pais1, pais2):number => {
              if (pais1.nombre < pais2.nombre) return -1;
              if (pais1.nombre > pais2.nombre) return 1;
              return 0;
            })
        }
      });
  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  loadDispatch() {
    this._store.dispatch(new actions.LoadAction());
  }

}

