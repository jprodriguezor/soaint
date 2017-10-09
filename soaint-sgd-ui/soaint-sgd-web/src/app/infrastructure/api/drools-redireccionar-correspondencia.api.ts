import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {Observable} from 'rxjs/Observable';
import {environment} from '../../../environments/environment';
import {droolsPayload, RulesServer} from '../../shared/drools-config-properties/drools-properties';
import {tassign} from 'tassign';

@Injectable()
export class DroolsRedireccionarCorrespondenciaApi {

  constructor(private _api: ApiBase) {
  }

  check(redireccionesNumero) {
    const end_point = environment.verificarRedirecciones_rule_endpoint;
    const payload = this.payload(redireccionesNumero);
    return this._api.list(end_point, { payload: JSON.stringify(payload)})
      .map(response => {
        debugger;
        const result = RulesServer.extractFromResponse(response, 'co.com.soaint.sgd.model.Redireccion').respuestaRedireccion;
        console.log(result);
        return result;
      });

    // return Observable.of({
    //   numRedirecciones : 3,
    //   respuestaRedireccion : true
    // });
  }

   payload(payload) {
    return tassign(droolsPayload, {
      commands: [{
        insert: {
          'out-identifier': 'Redireccion',
          'return-object': true,
          'object': {
            'co.com.soaint.sgd.model.Redireccion': {
              'numRedirecciones': payload
            }
          }
        }
      }, ...droolsPayload.commands]
    })
  }
}
