import {Injectable} from '@angular/core';
import {Http, RequestOptions, Headers} from '@angular/http';
import {SessionService, WebModel} from 'app/infrastructure/web/session.service';
import {Observable} from 'rxjs/Observable';
import {TipoComunicacionInterface} from 'app/domain/interfaces/tipo-comunicacion.interface';
import {environment} from 'environments/environment';

@Injectable()
export class TipoComunicacionApiService {

  constructor(private _http: Http, private _session: SessionService) {
  }

  public list(): Observable<Array<TipoComunicacionInterface>> {
    let token = this._session.retrieve(WebModel.SECURITY_TOKEN);
    let headers = new Headers({'Authorization': 'Bearer ' + token});
    let options = new RequestOptions({headers: headers});
    return this._http.get(environment.tipoComunicacion_endpoint, options).map(response => response.json());
  }

}
