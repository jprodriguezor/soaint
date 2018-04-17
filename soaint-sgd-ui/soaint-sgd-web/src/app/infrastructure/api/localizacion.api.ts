import { Injectable } from '@angular/core';
import {ApiBase} from './api-base';
import {ComunicacionOficialDTO} from '../../domain/comunicacionOficialDTO';
import {environment} from '../../../environments/environment';
import { CacheResponse } from '../../shared/cache-response';
import {Observable} from 'rxjs/Observable';
import { DependenciaDTO } from '../../domain/dependenciaDTO';
import { MunicipioDTO } from '../../domain/municipioDTO';
import { DepartamentoDTO } from '../../domain/departamentoDTO';

@Injectable()
export class LocalizacionApiService extends CacheResponse {

  constructor(private _api: ApiBase) {
    super();
   }

  ListarMunicipiosActivos(payload: any): Observable<MunicipioDTO[]> {
    const endpoint = environment.municipio_endpoint;
    return this.getResponse(payload, this._api.list(endpoint, payload)
        .map(response => {
          return response.municipios;
        }), endpoint);
  }

  ListarDepartamentosActivos(payload: any): Observable<DepartamentoDTO[]> {
    const endpoint = environment.departamento_endpoint;
    return this.getResponse(payload, this._api.list(endpoint, payload)
        .map(response => {
          return response.departamentos;
        }), endpoint);
  }

}
