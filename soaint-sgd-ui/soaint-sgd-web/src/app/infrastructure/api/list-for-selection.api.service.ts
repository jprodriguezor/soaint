import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {HttpHandler} from 'app/infrastructure/security/http-handler';

@Injectable()
export class ListForSelectionApiService extends ApiBase {

  constructor(protected _http: HttpHandler) {
      super(_http);
    }
}
