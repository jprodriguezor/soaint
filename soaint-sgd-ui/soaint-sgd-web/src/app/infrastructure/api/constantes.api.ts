import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import { SerieDTO } from 'app/domain/serieDTO';
import { SubserieDTO } from 'app/domain/subserieDTO';
import { ContenidoDependenciaTrdDTO } from 'app/domain/ContenidoDependenciaTrdDTO';
import { ConstanteDTO } from '../../domain/constanteDTO';
import { CacheResponse } from '../../shared/cache-response';

@Injectable()
export class ConstanteApiService extends CacheResponse {

  constructor(private _api: ApiBase) {
    super();
  }

  Listar(payload: any): Observable<ConstanteDTO[]> {
    let endpoint = null;
    switch (payload.key) {
      case 'tipoComunicacion':
        endpoint = environment.tipoComunicacion_endpoint;
        break;
      case 'tipoTelefono':
        endpoint = environment.tipoTelefono_endpoint;
        break;
      case 'tipoPersona':
        endpoint = environment.tipoPersona_endpoint;
        break;
      case 'tipoAnexos':
        endpoint = environment.tipoAnexos_endpoint;
        break;
      case 'tipoDocumento':
        endpoint = environment.tipoDocumento_endpoint;
        break;
      case 'tipoDestinatario':
        endpoint = environment.tipoDestinatario_endpoint;
        break;
      case 'unidadTiempo':
        endpoint = environment.unidadTiempo_endpoint;
        break;
      case 'mediosRecepcion':
        endpoint = environment.mediosRecepcion_endpoint;
        break;
      case 'tratamientoCortesia':
        endpoint = environment.tratamientoCortesia_endpoint;
        break;
      case 'actuaCalidad':
        endpoint = environment.actuaCalidad_endpoint;
        break;
      case 'tipologiaDocumental':
        endpoint = environment.tipologiaDocumental_endpoint;
        break;
      case 'tipoVia':
        endpoint = environment.tipoVia_endpoint;
        break;
      case 'orientacion':
        endpoint = environment.orientacion_endpoint;
        break;
      case 'bis':
        endpoint = environment.bis_endpoint;
        break;
      case 'tipoComplemento':
        endpoint = environment.tipoComplemento_endpoint;
        break;
      case 'prefijoCuadrante':
        endpoint = environment.prefijoCuadrante_endpoint;
        break;
      case 'soporteAnexo':
        endpoint = environment.soporteAnexo_endpoint;
        break;
    }

    if (endpoint !== null) {
      return this.getResponse(payload, this._api.list(endpoint, payload)
        .map(response => {
          this.cacheResponse(payload, response);
          return response.constantes;
        }), endpoint);
    }

  }

}
