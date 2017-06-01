import {Injectable} from '@angular/core';
import {Http, RequestOptions, Headers} from '@angular/http';
import {SessionService, WebModel} from 'app/infrastructure/web/session.service';
import {Observable} from 'rxjs/Observable';
import {UnidadTiempoInterface} from 'app/domain/interfaces/unidad-tiempo.interface';
import {environment} from 'environments/environment';

@Injectable()
export class UnidadTiempoApiService {

  constructor(private _http: Http, private _session: SessionService) {
  }

  public list(): Observable<Array<UnidadTiempoInterface>> {
    let token = this._session.retrieve(WebModel.SECURITY_TOKEN);
    let headers = new Headers({'Authorization': 'Bearer ' + token});
    let options = new RequestOptions({headers: headers});
    return this._http.get(environment.unidadTiempo_endpoint, options).map(response => response.json());
  }

}
