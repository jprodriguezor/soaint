import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import { SerieDTO } from 'app/domain/serieDTO';
import { SubserieDTO } from 'app/domain/subserieDTO';

@Injectable()
export class SerieSubserieApiService {

  constructor(private _api: ApiBase) {
  }

  ListarSerie(payload: any) {
    return this._api.post(environment.listar_serie, payload).map(response => response);
  }

  ListarSubserie(payload: any) {
    return this._api.post(environment.listar_subserie, payload).map(response => response);
  }

}
