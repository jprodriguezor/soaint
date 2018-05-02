import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {UnidadDocumentalDTO} from '../../domain/unidadDocumentalDTO';
import { MensajeRespuestaDTO } from '../../domain/MensajeRespuestaDTO';

@Injectable()
export class UnidadDocumentalApiService {

  constructor(private _api: ApiBase) {
  }

  Listar(payload: UnidadDocumentalDTO): Observable<UnidadDocumentalDTO[]> {
    return this._api.post(environment.listar_unidad_documental_endpoint, payload)
    .map((resp) => {
      if (resp.response) {
        return resp.response.unidadDocumental
      } else {
        return Observable.of([]);
      }
    });
  }

  GetDetalleUnidadDocumental(payload: string): Observable<UnidadDocumentalDTO> {
    return this._api.list(environment.detalle_unidad_documental_endpoint + payload)
    .map(response => response.response.unidadDocumental);
  }

   crear(unidadDocumental: UnidadDocumentalDTO) {
     return this._api.post(environment.crear_unidad_documental, unidadDocumental);
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

  noTramitarUnidadesDocumentales(payload: any){

    //return this._api.post("",payload)

    return Observable.of(true);

  }

  listarUnidadesDocumentales(payload:any):Observable<any[]>{

    return this._api.post(environment.listar_unidad_documental_endpoint,payload)
      .map( response => response.response.unidadDocumental);
  }

}
