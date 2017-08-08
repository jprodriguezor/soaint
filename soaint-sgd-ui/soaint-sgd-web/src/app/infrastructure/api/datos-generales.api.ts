import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class DatosGeneralesApiService {

  constructor(private _api: ApiBase) {
  }

  loadMetricasTiempo(tipologiaDocumental) {
    // const end_point = environment.metricasTiempoRadicacion_rule_endpoint;
    // const payload = RulesServer.requestPayload(tipologiaDocumental);
    // return this._api.list(end_point, payload);

    return Observable.of({
      tiempoRespuesta: '10',
      codUnidaTiempo: 'UNID-TID',
      inicioConteo: 'DSH'
    });
  }
}
