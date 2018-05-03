import {ComunicacionOficialDTO} from "../../domain/comunicacionOficialDTO";
import {CorrespondenciaDTO} from "../../domain/correspondenciaDTO";
import {State as RootState} from "../../infrastructure/redux-store/redux-reducers";
import {
  getUnidadTiempoArrayData
} from "../../infrastructure/state-management/constanteDTO-state/selectors/unidad-tiempo-selectors";
import {combineLatest} from "rxjs/observable/combineLatest";
import {Store} from "@ngrx/store";
import {Observable} from "rxjs/Observable";
import {getMediosRecepcionArrayData} from "../../infrastructure/state-management/constanteDTO-state/selectors/medios-recepcion-selectors";
import {getTipologiaDocumentalArrayData} from "../../infrastructure/state-management/constanteDTO-state/selectors/tipologia-documental-selectors";
import {getTipoComunicacionArrayData} from "../../infrastructure/state-management/constanteDTO-state/selectors/tipo-comunicacion-selectors";
import {AnexoDTO} from "../../domain/anexoDTO";
import {getTipoAnexosArrayData} from "../../infrastructure/state-management/constanteDTO-state/selectors/tipo-anexos-selectors";
import {getSoporteAnexoArrayData} from "../../infrastructure/state-management/constanteDTO-state/selectors/soporte-anexos-selectors";
import {AgentDTO} from "../../domain/agentDTO";
import {getArrayData as Dependencias} from "../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors";
import {getArrayData as SedesAdminintrativas} from "../../infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors";
import {
  TIPO_AGENTE_DESTINATARIO,
  TIPO_REMITENTE_EXTERNO,
  TIPO_REMITENTE_INTERNO
} from "../bussiness-properties/radicacion-properties";
import {ApiBase} from "../../infrastructure/api/api-base";
import {environment} from "../../../environments/environment";
import {tipoDestinatarioEntradaSelector} from "../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors";

export class ComunicacionToSource {

  constructor(private comunicacion:ComunicacionOficialDTO,private store:Store<RootState>,private _api:ApiBase){

  }

  transform(): Observable<any>{


    let agentList = this.comunicacion.agenteList;
    agentList[0].datosContactoList = this.comunicacion.datosContactoList;

    return  combineLatest(
      this.buildGenerales(this.comunicacion.correspondencia),
      this.buildListaAnexos(this.comunicacion.anexoList),
      this.buildListAgeInt(agentList.filter(agent => agent.codTipoRemite == TIPO_REMITENTE_INTERNO)),
      (generales,descripcionAnexos,destinatarioInterno) => {

        return {
          generales:Object.assign(generales,{descripcionAnexos:descripcionAnexos}),
          radicadosReferidos: this.comunicacion.referidoList.map( referido => {
            return {
              ideReferido : referido.ideReferido,
              nombre: referido.nroRadRef
            }
          }),
          date: this.comunicacion.correspondencia.fecRadicado,
          datosContacto:{
            listaDestinatariosInternos : destinatarioInterno,
            listaDestinatariosExternos : agentList.filter( (agent,index) =>  index > 0 && agent.codTipoRemite == TIPO_REMITENTE_EXTERNO)
          },

        };
      }
    )

  }

  buildGenerales(correspondencia:CorrespondenciaDTO): Observable<any> {

    return    combineLatest(
      this.store.select(getUnidadTiempoArrayData).map( entities => entities.find(entity => entity.codigo == correspondencia.codUnidadTiempo)),
      this.store.select(getMediosRecepcionArrayData).map(entities => entities.find( entity => entity.codigo == correspondencia.codMedioRecepcion)),
      this.store.select(getTipologiaDocumentalArrayData).map(entities => entities.find( entity => entity.codigo == correspondencia.codTipoDoc)),
      this.store.select(getTipoComunicacionArrayData).map(entities => entities.find( entity => entity.codigo == correspondencia.codTipoCmc)),
      (unidadTiempo,medioRecepcion,tipologiaDocumental,tipoComunicacion) => {
        return {
          ideDocumento: correspondencia.ideDocumento,
          unidadTiempo:unidadTiempo,
          asunto: correspondencia.descripcion,
          tiempoRespuesta : correspondencia.tiempoRespuesta,
          empresaMensajeria:correspondencia.codEmpMsj,
          numeroGuia:correspondencia.nroGuia,
          inicioConteo:correspondencia.inicioConteo,
          medioRecepcion:medioRecepcion,
          tipologiaDocumental:tipologiaDocumental,
          tipoComunicacion:tipoComunicacion
        };
      }
    );
  }

  buildListaAnexos( listaAnexos:AnexoDTO[]):Observable<any[]>{

       return  listaAnexos.length  ?  Observable.from(listaAnexos)
           .switchMap( (anexo) =>
             combineLatest(
               this.store.select(getTipoAnexosArrayData).map( entities => entities.find( entity => entity.codigo == anexo.codAnexo)),
               this.store.select(getSoporteAnexoArrayData).map( entities =>entities.find( entity => entity.codigo == anexo.codTipoSoporte)),
                 (tipoAnexo,soporteAnexo) => {
                  return {
                    ideAnexo : anexo.ideAnexo,
                    tipoAnexo:tipoAnexo,
                    descripcion: anexo.descripcion,
                    soporteAnexo:soporteAnexo
                  }
                 }))
      .scan((acc,curr) => {
        acc.push(curr);
        return acc;
      },[]) : Observable.of([]);
  }

  buildListAgeInt(agentes:AgentDTO[]):Observable<any[]>{

     return Observable.from(agentes)
       .switchMap(agente =>
     combineLatest(
       this._api.list(environment.dependenciaGrupo_endpoint+"/all-dependencias").map( entities => entities.dependencias.find(entity => entity.codigo == agente.codDependencia)),
       this.store.select(SedesAdminintrativas).map( entities => entities.find(entity => entity.codigo == agente.codSede)),
       this.store.select(tipoDestinatarioEntradaSelector).map( entities => { console.log("entities",entities) ;return  entities.find(entity => entity.codigo == agente.indOriginal)}),
        (dependenciaGrupo,sedeAdministrativa,tipoDestinatario) => {

         return {
           ideAgente : agente.ideAgente,
           tipoDestinatario : tipoDestinatario,
           sede: sedeAdministrativa,
           dependencia:dependenciaGrupo
         };
       }
     )
    ).scan((acc,curr) => {
      acc.push(curr);
      return acc;
    },[]);

  }





}
