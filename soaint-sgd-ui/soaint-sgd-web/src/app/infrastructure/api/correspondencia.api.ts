import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {UnidadDocumentalDTO} from '../../domain/unidadDocumentalDTO';
import { MensajeRespuestaDTO } from '../../domain/MensajeRespuestaDTO';
import { AnexoFullDTO } from '../../domain/anexoFullDTO';
import { Subscription } from 'rxjs';
import { ComunicacionOficialDTO } from '../../domain/comunicacionOficialDTO';

@Injectable()
export class CorrespondenciaApiService {

  constructor(private _api: ApiBase) {

  }

  ListarComunicacionesSalidaDistibucionFisica(payload: any): Observable<ComunicacionOficialDTO[]> {
    return  this._api.list(environment.listarCorrespondencia_salida_distribucion_fisica_endpoint, payload)
            .map(_map => _map.comunicacionesOficiales);
    // return Observable.of([
    //   {correspondencia:{
    //     ideDocumento:1566,
    //     descripcion:"ergerg",
    //     tiempoRespuesta:"10",
    //     codUnidadTiempo:"UNID-TID",
    //     codMedioRecepcion:"ME-RECVN",
    //     fecRadicado:"1527528817584",
    //     nroRadicado:"1000EE2018000214",
    //     fecDocumento:null,
    //     codTipoDoc:null,
    //     codTipoCmc:"EE",
    //     ideInstancia:"175231",
    //     reqDistFisica:"1",
    //     codFuncRadica:2,
    //     codSede:"1000",
    //     codDependencia:"10001040",
    //     reqDigita:"0",
    //     codEmpMsj:null,
    //     nroGuia:null,
    //     fecVenGestion:"1528923637584",
    //     codEstado:"AS",
    //     inicioConteo:null,
    //     codClaseEnvio:null,
    //     codModalidadEnvio:null
    //   },
    //   agenteList:[
    //     {
    //       ideAgente:3170,
    //       codTipoRemite:"INT",
    //       codTipoPers:null,
    //       nombre:null,
    //       razonSocial:null,
    //       nit:null,
    //       codCortesia:null,
    //       codEnCalidad:null,
    //       ideFunci:2,
    //       codTipDocIdent:null,
    //       nroDocuIdentidad:null,
    //       codSede:"1040",
    //       codDependencia:"10401040",
    //       codEstado:"SA",
    //       fecAsignacion:null,
    //       codTipAgent:"TP-AGEI",
    //       indOriginal:"TP-DESP",
    //       numRedirecciones:0,
    //       numDevoluciones:0,
    //       datosContactoList:null,
    //     },
    //       {
    //         ideAgente:3169,
    //         codTipoRemite:"EXT",
    //         codTipoPers:"TP-PERA",
    //         nombre:null,
    //         razonSocial:null,
    //         nit:null,
    //         codCortesia:null,
    //         codEnCalidad:null,
    //         ideFunci:null,
    //         codTipDocIdent:null,
    //         nroDocuIdentidad:null,
    //         codSede:null,
    //         codDependencia:null,
    //         codEstado:null,
    //         fecAsignacion:null,
    //         codTipAgent:"TP-AGEE",
    //         indOriginal:null,
    //         numRedirecciones:null,
    //         numDevoluciones:null,
    //         datosContactoList:[]
    //       }],
    //       ppdDocumentoList:[{
    //         idePpdDocumento:1559,
    //         codTipoDoc:"TL-DOCOF",
    //         fecDocumento:"1527528817584",
    //         asunto:"ergerg",
    //         nroFolios:3445345,
    //         nroAnexos:1,
    //         codEstDoc:null,
    //         ideEcm:"5abf945c-2e43-4830-bd68-166c6e76eabe"
    //       }],
    //       anexoList:null,
    //       referidoList:null,
    //       datosContactoList:null}]);
 }

  actualizarComunicacion(payload: any): Observable<any> {
     return  this._api.put(environment.actualizarComunicacion_endpoint, payload);
  }
}
