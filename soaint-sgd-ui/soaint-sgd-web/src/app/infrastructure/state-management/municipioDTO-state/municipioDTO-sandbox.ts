import {ConstanteDTO} from '../../../domain/constanteDTO';
import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './municipioDTO-actions';
import {ApiBase} from '../../api/api-base';
import {CacheResponse} from "../../../shared/cache-response";


@Injectable()
export class Sandbox extends CacheResponse{

  constructor(private _store: Store<State>,
              private _api: ApiBase) {

    super();
  }

  loadData(payload: any) {
    const _endpoint = `${environment.municipio_endpoint}/${payload.codDepar}`;

    return this.getResponse(payload,this._api.list(_endpoint, payload)
      .map((response) => {
        let municipios = {
          municipios: response.municipios.sort((municipio1, municipio2):number => {
            if (municipio1.nombre < municipio2.nombre) return -1;
            if (municipio1.nombre > municipio2.nombre) return 1;
            return 0;
          })
        };


        return municipios;

      }),_endpoint);


  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  loadDispatch(payload) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

}

