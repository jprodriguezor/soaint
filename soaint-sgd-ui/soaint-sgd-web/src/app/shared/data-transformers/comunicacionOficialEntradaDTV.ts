import {AgentDTO} from '../../domain/agentDTO';
import {ContactoDTO} from '../../domain/contactoDTO';
import {
  COMUNICACION_INTERNA,
  DATOS_CONTACTO_PRINCIPAL, DATOS_CONTACTO_SECUNDARIO, DESTINATARIO_INTERNO, DESTINATARIO_PRINCIPAL,
  TIPO_AGENTE_DESTINATARIO,
  TIPO_AGENTE_REMITENTE, TIPO_REMITENTE_EXTERNO,
  TIPO_REMITENTE_INTERNO
} from '../bussiness-properties/radicacion-properties';

import {RadicacionBase} from "./radicacionBase";
import {RadicacionEntradaFormInterface} from "../interfaces/data-transformers/radicacionEntradaForm.interface";
import {DireccionDTO} from "../../domain/DireccionDTO";

export class ComunicacionOficialEntradaDTV  extends  RadicacionBase{

 getAgentesDestinatario(): Array<AgentDTO> {

    const agentes = [];

    agentes.push(this.getRemitente());

    (<RadicacionEntradaFormInterface>this.source).agentesDestinatario.forEach(agenteInt => {
      const tipoAgente: AgentDTO = {
        ideAgente: agenteInt.ideAgente,
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
        indOriginal: agenteInt.tipoDestinatario ? agenteInt.tipoDestinatario.codigo : DESTINATARIO_INTERNO
        ,
      };
      agentes.push(tipoAgente);
    });

    return agentes;
  }


  getDatosContactos(): Array<ContactoDTO> {

   return this.transformContactData((<RadicacionEntradaFormInterface>this.source).datosContactos
     ? (<RadicacionEntradaFormInterface>this.source).datosContactos : []);

  }

  isRemitenteInterno() {
    return this.source.generales.tipoComunicacion.codigo === COMUNICACION_INTERNA;
  }

}
