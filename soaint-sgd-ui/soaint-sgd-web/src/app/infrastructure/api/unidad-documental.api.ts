import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class UnidadDocumentalApiService {

  constructor(private _api: ApiBase) {
  }

  guardarEstadoTarea(payload: any) {
    return this._api.post(environment.taskStatus_endpoint, payload).map(response => response);
  }

  obtenerEstadoTarea(payload: {idInstanciaProceso: string, idTareaProceso: string}) {
    return this._api.list(`${environment.taskStatus_endpoint}/${payload.idInstanciaProceso}/${payload.idTareaProceso}`, {}).map(response => response.payload);
  }

}
