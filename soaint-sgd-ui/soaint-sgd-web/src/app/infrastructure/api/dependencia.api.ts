import { Injectable } from '@angular/core';
import {ApiBase} from './api-base';
import {ComunicacionOficialDTO} from '../../domain/comunicacionOficialDTO';
import {environment} from '../../../environments/environment';
import { CacheResponse } from '../../shared/cache-response';
import {Observable} from 'rxjs/Observable';
import { DependenciaDTO } from '../../domain/dependenciaDTO';

@Injectable()
export class DependenciaApiService extends CacheResponse {

  constructor(private _api: ApiBase) {
    super();
   }

  Listar(payload: any): Observable<DependenciaDTO[]> {
    const endpoint = environment.dependencias_endpoint;
    return this.getResponse(payload, this._api.list(endpoint, payload)
        .map(response => {
          this.cacheResponse(payload, response);
          return response.dependencias;
        }), endpoint);
  }

}
