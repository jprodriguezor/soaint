import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import { DetalleUnidadDocumentalDTO } from 'app/ui/page-components/unidades-documentales/models/DetalleUnidadDocumentalDTO';
import {UnidadDocumentalDTO} from '../../domain/unidadDocumentalDTO';
import { MensajeRespuestaDTO } from '../../domain/MensajeRespuestaDTO';

@Injectable()
export class UnidadDocumentalApiService {

  constructor(private _api: ApiBase) {
  }

  Listar(payload: any): Observable<UnidadDocumentalDTO[]> {
    return this._api.post(environment.listar_unidad_documental_endpoint, payload)
    .map((resp) => resp.response);
  }

  GetDetalleUnidadDocumental(payload: any) {
    return null;
   }

   crear(unidadDocumental: UnidadDocumentalDTO) {
     return this._api.post('', unidadDocumental);
   }

}
