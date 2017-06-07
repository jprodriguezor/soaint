import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {HttpHandler} from '../security/http-handler';

export abstract class ApiBase {

  constructor(protected _http: HttpHandler) {}

  public list(endpoint: string): Observable<Array<any>> {

    return this._http.get(endpoint).map(response => response.json());
  }

}
