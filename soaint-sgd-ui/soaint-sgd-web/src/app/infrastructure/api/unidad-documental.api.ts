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

  GetDetalleUnidadDocumental(payload: string): Observable<UnidadDocumentalDTO> {
    return this._api.list(environment.detalle_unidad_documental_endpoint + payload)
    .map(response => response.response.unidadDocumental);
  }

   crear(unidadDocumental: UnidadDocumentalDTO) {
     return this._api.post('', unidadDocumental);
   }

  gestionarUnidadesDocumentales(payload: any): Observable<MensajeRespuestaDTO> {
    return this._api.post(environment.gestionar_unidades_documentales_endpoint, payload)
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
