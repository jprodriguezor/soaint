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

  Listar(payload: UnidadDocumentalDTO): Observable<UnidadDocumentalDTO[]> {
    return this._api.post(environment.listar_unidad_documental_endpoint, payload)
    .map((resp) => resp.response.unidadDocumental);
  }

  GetDetalleUnidadDocumental(payload: any) {
    return null;
   }

   crear(unidadDocumental: UnidadDocumentalDTO) {
     return this._api.post('', unidadDocumental);
   }

  abrirUnidadesDocumentales(payload: any): Observable<MensajeRespuestaDTO> {
    return this._api.post(environment.abrir_unidad_documental_endpoint, payload)
  }
  cerrarUnidadesDocumentales(payload: any): Observable<MensajeRespuestaDTO> {
    return this._api.post(environment.cerrar_unidad_documental_endpoint, payload);
  }
  reactivarUnidadesDocumentales(payload: any): Observable<MensajeRespuestaDTO> {
    return this._api.post(environment.reactivar_unidad_documental_endpoint, payload);
  }

}
