import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {enableDebugTools} from '@angular/platform-browser';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class DatosGeneralesApiService {

  constructor(private _api: ApiBase) {
  }

  loadMetricasTiempo(payload) {
    // const end_point = environment.metricasTiempoRadicacion_rule_endpoint
    // return this._api.list(end_point, payload);

    return Observable.of({
      codMedioRecepcion: 'TL-DOCOF',
      tiempoRespuesta: '10',
      codUnidaTiempo: 'DIAS',
      inicioConteo: 'DSH'
    });
  }
}
