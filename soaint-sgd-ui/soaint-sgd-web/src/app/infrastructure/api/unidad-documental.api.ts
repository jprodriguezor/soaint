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

  gestionarUnidadesDocumentales(payload: any): Observable<MensajeRespuestaDTO> {
    return this._api.post(environment.gestionar_unidades_documentales_endpoint, payload)
  }

  noTramitarUnidadesDocumentales(payload: any){

    //return this._api.post("",payload)

    return Observable.of(true);

  }

  quickSave(payload: any) {
    return this._api.post(environment.salvarCorrespondenciaEntrada_endpoint, payload);
  }

  listarUnidadesDocumentales(payload:any):Observable<any[]>{

    return this._api.post(environment.listar_unidad_documental_endpoint,payload)
      .map( response => response.response.unidadDocumental);
  }

  ActualizarDisposicionFinal(payload: UnidadDocumentalDTO[]): Observable<MensajeRespuestaDTO> {
    return Observable.of({
      codMensaje: '00000',
      contenidoDependenciaTrdDTOS: null,
      documentoDTOList: null,
      mensaje: 'Operación completada satisfactoriamente',
      response: null,
    });
  }

}
