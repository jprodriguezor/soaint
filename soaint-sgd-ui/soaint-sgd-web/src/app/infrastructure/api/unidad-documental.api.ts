import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class UnidadDocumentalApiService {

  constructor(private _api: ApiBase) {
  }

  Listar(payload: any) {
    // return this._api.post(environment.listar_unidad_documental, payload).map(response => response);
    return Observable.empty<Response>();
    }

}
