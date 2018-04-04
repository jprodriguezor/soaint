import {AgentDTO} from "../../domain/agentDTO";
import {
  COMUNICACION_EXTERNA,
  COMUNICACION_INTERNA,
  TIPO_AGENTE_REMITENTE, TIPO_REMITENTE_EXTERNO,
  TIPO_REMITENTE_INTERNO
} from "../bussiness-properties/radicacion-properties";


export interface  AgenteDTV{

  getRemitente(remitente:any):AgentDTO;
}


export class AgenteInternoDTV implements AgenteDTV{

  getRemitente(remitente:any):AgentDTO{

    return {
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
      codSede: remitente.sedeAdministrativa ? remitente.sedeAdministrativa.codigo : null,
      codDependencia: remitente.dependenciaGrupo ? remitente.dependenciaGrupo.codigo : null,
      fecAsignacion: null,
      codTipAgent: TIPO_AGENTE_REMITENTE,
      codEstado: null
    };
  }
}

export class AgenteExternoDTV implements AgenteDTV{

  getRemitente(remitente:any):AgentDTO{

    return  {
      ideAgente: null,
      codTipoRemite: TIPO_REMITENTE_EXTERNO,
      codTipoPers: remitente.tipoPersona ? remitente.tipoPersona.codigo : null,
      nombre: remitente.nombreApellidos || null,
      razonSocial: remitente.razonSocial || null,
      nit: remitente.nit || null,
      codEnCalidad: remitente.actuaCalidad ? remitente.actuaCalidad.codigo : null,
      codTipDocIdent: remitente.tipoDocumento ? remitente.tipoDocumento.codigo : null,
      nroDocuIdentidad: remitente.nroDocumentoIdentidad,
      codSede: null,
      codDependencia: null,
      fecAsignacion: null,
      codTipAgent: TIPO_AGENTE_REMITENTE,
      indOriginal: null,
      codEstado: null
    };

  }
}

export class AgenteFactoryDTV{

  static getAgente(tipo:string):AgenteDTV{

    switch(tipo) {

      case COMUNICACION_INTERNA: return new AgenteInternoDTV();

      case COMUNICACION_EXTERNA : return new AgenteExternoDTV();
    }

    return null;

  }
}
