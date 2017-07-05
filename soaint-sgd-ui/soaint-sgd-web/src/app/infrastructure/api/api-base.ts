import {Observable} from 'rxjs/Observable';
import {HttpHandler} from '../security/http-handler';
import {RequestOptions, URLSearchParams} from '@angular/http';

export abstract class ApiBase {

  constructor(protected _http: HttpHandler) {
  }

  public list(endpoint: string, payload = {}): Observable<any> {
    return this._http.get(endpoint, payload);
  }

  public listParams(endpoint: string, params: URLSearchParams): Observable<any> {
    console.log(params);
    let options = new RequestOptions({params: params});
    return this._http.get(endpoint, options);
  }

  public post(endpoint: string, payload = {}): Observable<any> {
    return this._http.post(endpoint, payload);
  }

}
