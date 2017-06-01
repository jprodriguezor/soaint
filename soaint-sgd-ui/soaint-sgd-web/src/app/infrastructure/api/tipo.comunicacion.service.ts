import {Injectable} from '@angular/core';
import {Headers, Http, RequestOptions} from '@angular/http';
import {SessionService, WebModel} from 'app/infrastructure/web/session.service';
import {Observable} from 'rxjs/Observable';
import {environment} from 'environments/environment';
import {ListaSeleccionSimple} from '../../domain/lista.seleccion.simple';

@Injectable()
export class TipoComunicacionService {

  constructor(private _http: Http, private _session: SessionService) {
  }

  public list(): Observable<Array<ListaSeleccionSimple>> {
    let token = this._session.retrieve(WebModel.SECURITY_TOKEN);
    let headers = new Headers({'Authorization': 'Bearer ' + token});
    let options = new RequestOptions({headers: headers});
    return this._http.get(environment.tipo_comunicacion_endpoint, options).map(response => response.json());
  }

}
