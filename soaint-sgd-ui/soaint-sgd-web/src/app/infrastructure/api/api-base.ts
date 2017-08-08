import {Observable} from 'rxjs/Observable';
import {HttpHandler} from '../utils/http-handler';
import {Injectable} from '@angular/core';
import {Headers, RequestOptions} from '@angular/http';


@Injectable()
export class ApiBase {

  constructor(protected _http: HttpHandler) {
  }

  public list(endpoint: string, payload = {}): Observable<any> {
    return this._http.get(endpoint, payload);
  }

  public post(endpoint: string, payload = {}): Observable<any> {
    return this._http.post(endpoint, payload);
  }

  public sendFile(endpoint: string, formData): Observable<any> {
    return this._http.putFile(endpoint, formData);
  }

}
