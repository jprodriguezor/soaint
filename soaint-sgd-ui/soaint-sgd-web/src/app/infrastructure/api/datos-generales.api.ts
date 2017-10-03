import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {Observable} from 'rxjs/Observable';
import {environment} from '../../../environments/environment';
import {RulesServer} from '../../shared/drools-config-properties/drools-properties';

@Injectable()
export class DatosGeneralesApiService {

  constructor(private _api: ApiBase) {
  }

  loadMetricasTiempo(tipologiaDocumental) {
    // const end_point = environment.metricasTiempoRadicacion_rule_endpoint;
    // const payload = RulesServer.requestPayload(tipologiaDocumental.codigo);
    // return this._api.list(end_point, { payload: JSON.stringify(payload)})
    //   .map(response => {
    //     return RulesServer.extractFromResponse(response, 'co.com.soaint.sgd.model.MedioRecepcion');
    //   });

    return Observable.of({
      tiempoRespuesta: '10',
      codUnidaTiempo: 'UNID-TID',
      inicioConteo: 'DSH'
    });
  }
}
