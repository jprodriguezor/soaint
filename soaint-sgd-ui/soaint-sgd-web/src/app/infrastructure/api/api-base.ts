import {Observable} from 'rxjs/Observable';
import {HttpHandler} from '../security/http-handler';

export abstract class ApiBase {

  constructor(protected _http: HttpHandler) {
  }

  public list(endpoint: string, payload = {}): Observable<any> {
    return this._http.get(endpoint);
  }

  public post(endpoint: string, payload = {}): Observable<any> {
    return this._http.post(endpoint, payload);
  }

}
