import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {Observable} from 'rxjs/Observable';
import {environment} from '../../../environments/environment';
import {RulesServer} from '../../shared/drools-config-properties/drools-properties';
import {CacheResponse} from "../../shared/cache-response";

@Injectable()
export class DatosGeneralesApiService  extends CacheResponse{

  constructor(private _api: ApiBase) {

    super();
  }

  loadMetricasTiempo(tipologiaDocumental) {
    const end_point = environment.metricasTiempoRadicacion_rule_endpoint;
    const payload = RulesServer.requestPayload(tipologiaDocumental.codigo);
    return this.getResponse({payload: JSON.stringify(payload)},this._api.list(end_point, {payload: JSON.stringify(payload)})
      .map(response => {
        const res = RulesServer.extractFromResponse(response, 'co.com.soaint.sgd.model.MedioRecepcion');

       return res;

      }),end_point) ;

    // return Observable.of({
    //   tiempoRespuesta: '10',
    //   codUnidaTiempo: 'UNID-TID',
    //   inicioConteo: 'DSH'
    // });
  }
}
