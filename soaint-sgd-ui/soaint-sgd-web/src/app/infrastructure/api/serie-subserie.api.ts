import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import { SerieDTO } from 'app/domain/serieDTO';
import { SubserieDTO } from 'app/domain/subserieDTO';
import { ContenidoDependenciaTrdDTO } from 'app/domain/ContenidoDependenciaTrdDTO';

@Injectable()
export class SerieSubserieApiService {

  constructor(private _api: ApiBase) {
  }

  ListarSerieSubserie(payload: any): Observable<ContenidoDependenciaTrdDTO> {
    const resp = this._api.post(environment.listar_serie_subserie, payload)
                     .map(response =>
                        (response.contenidoDependenciaTrdDTOS) ? response.contenidoDependenciaTrdDTOS[0] : Observable.empty<ContenidoDependenciaTrdDTO>());
    return resp

  }

}
