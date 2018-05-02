import {AgentDTO} from '../../domain/agentDTO';
import {RadicacionBase} from './radicacionBase';
import {
  DATOS_CONTACTO_PRINCIPAL,
  DATOS_CONTACTO_SECUNDARIO, DESTINATARIO_EXTERNO, DESTINATARIO_INTERNO,
  TIPO_AGENTE_DESTINATARIO, TIPO_REMITENTE_EXTERNO, TIPO_REMITENTE_INTERNO
} from '../bussiness-properties/radicacion-properties';
import {RadicacionSalidaFormInterface} from '../interfaces/data-transformers/radicacionSalidaForm.interface';
import {CorrespondenciaDTO} from "../../domain/correspondenciaDTO";
import {RadicacionEntradaFormInterface} from "../interfaces/data-transformers/radicacionEntradaForm.interface";
import {ContactoDTO} from "../../domain/contactoDTO";

export class RadicacionSalidaDTV extends  RadicacionBase {


  getCorrespondencia():CorrespondenciaDTO{

    const datosEnvio = (<RadicacionSalidaFormInterface>this.source).datosEnvio;

    let correspondencia = super.getCorrespondencia();

    if(datosEnvio !== undefined){

      correspondencia.codClaseEnvio = datosEnvio.clase_envio.codigo;
      correspondencia.codModalidadEnvio = datosEnvio.modalidad_correo.codigo;
    }

     return correspondencia;
  }

  getAgentesDestinatario(): Array<AgentDTO> {

    const agentes = [];

    agentes.push(this.getRemitente());

    (<RadicacionSalidaFormInterface>this.source).destinatarioInterno.forEach(agenteInt => {
      const tipoAgente: AgentDTO = {
        ideAgente: null,
        codTipoRemite: TIPO_REMITENTE_INTERNO,
        codTipoPers: null,
        nombre: null,
        razonSocial: null,
        nit: null,
        codCortesia: null,
        codEnCalidad: null,
        codTipDocIdent: null,
        nroDocuIdentidad: null,
        codSede: agenteInt.sedeAdministrativa ? agenteInt.sedeAdministrativa.codigo : null,
        codDependencia: agenteInt.dependenciaGrupo ? agenteInt.dependenciaGrupo.codigo : null,
        fecAsignacion: null,
        codTipAgent: TIPO_AGENTE_DESTINATARIO,
        codEstado: null,
        indOriginal: agenteInt.tipoDestinatario ? agenteInt.tipoDestinatario.codigo : DESTINATARIO_INTERNO,
      };
      agentes.push(tipoAgente);
    });

    (<RadicacionSalidaFormInterface>this.source).destinatarioExt.forEach(agenteExt => {
      const tipoAgente: AgentDTO = {
        ideAgente: null,
        codTipoRemite: TIPO_REMITENTE_EXTERNO,
        codTipoPers: agenteExt.tipoPersona.codTipoPers,
        nombre: agenteExt.Nombre,
        razonSocial: null,
        nit: null,
        codCortesia: null,
        codEnCalidad: null,
        codTipDocIdent: null,
        nroDocuIdentidad: null,
        codSede:  null,
        codDependencia: null,
        fecAsignacion: null,
        codTipAgent: TIPO_AGENTE_DESTINATARIO,
        codEstado: null,
        indOriginal: agenteExt.tipoDestinatario ? agenteExt.tipoDestinatario.codigo : DESTINATARIO_EXTERNO,
        datosContactoList: this.transformContactData(agenteExt.datosContactoList),
      };
      agentes.push(tipoAgente);
    });

    return agentes;
  }


}
