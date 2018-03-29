import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './paisDTO-actions';
import {ApiBase} from '../../api/api-base';
import {CacheResponse} from "../../../shared/cache-response";

@Injectable()
export class Sandbox extends CacheResponse {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
    super();
  }
  loadData(payload: any) {

   return this.getResponse(payload, this._api.list(environment.pais_endpoint, payload)
     .map((response) => {
       let  paises ={
         paises: response.paises.sort((pais1, pais2):number => {
           if (pais1.nombre < pais2.nombre) return -1;
           if (pais1.nombre > pais2.nombre) return 1;
           return 0;
         })
       };

       this.cacheResponse(payload,paises);

       return paises;
     }));
  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  loadDispatch() {
    this._store.dispatch(new actions.LoadAction());
  }



}

