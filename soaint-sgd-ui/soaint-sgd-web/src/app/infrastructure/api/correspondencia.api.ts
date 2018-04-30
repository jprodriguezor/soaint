import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {UnidadDocumentalDTO} from '../../domain/unidadDocumentalDTO';
import { MensajeRespuestaDTO } from '../../domain/MensajeRespuestaDTO';
import { AnexoFullDTO } from '../../domain/anexoFullDTO';
import { Subscription } from 'rxjs';

@Injectable()
export class CorrespondenciaApiService {

  constructor(private _api: ApiBase) {

  }

  actualizarComunicacion(payload: any): Observable<any> {
     return  this._api.put(environment.actualizarComunicacion_endpoint, payload);
  }
}
