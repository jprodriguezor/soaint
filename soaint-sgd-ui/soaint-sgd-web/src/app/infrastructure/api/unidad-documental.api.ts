import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {UnidadDocumentalDTO} from '../../domain/unidadDocumentalDTO';
import {DisposicionFinalDTO} from '../../domain/DisposicionFinalDTO';
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

   crear(unidadDocumental: UnidadDocumentalDTO):Observable<any> {
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

  listarUnidadesDocumentalesDisposicion(payload: DisposicionFinalDTO): Observable<UnidadDocumentalDTO[]>{

        return this._api.post(environment.listar_unidades_documentales_disposicion_endpoint, payload)
          .map( response => {
            return  response.response ? response.response.unidadesDocumentales : Observable.of([]);
          });
  }

  aprobarRechazarUnidadesDocumentalesDisposicion(payload: UnidadDocumentalDTO[]): Observable<MensajeRespuestaDTO>{

        return this._api.post(environment.aprobar_rechazar_unidades_documentales_endpoint, payload)
          .map( response => response);
  }

  listarDocumentosPorArchivar(codDependencia):Observable<any>{

    return this._api.list(environment.listar_documentos_archivar+codDependencia);
  }

  archivarDocumento(payload:any):Observable<any>{

    return this._api.post(environment.archivar_documento_endpoint,payload);
  }

  listarDocumentosArchivadosPorDependencia(codDependencia): Observable<any[]>{

    return this._api.list(environment.listar_documentos_archivados+codDependencia)
               .map(response => response.response.documentos);
  }

  subirDocumentosParaArchivar(documentos: FormData):Observable<any>{

   return  this._api.sendFile(environment.subir_documentos_por_archivar,documentos,[]);
  }

  obtenerDocumentoPorNoRadicado(nroRadicado):Observable<any>{

    return this._api.list( `${environment.obtenerDocumento_nro_radicado_endpoint}/${nroRadicado}`);

  }


}
