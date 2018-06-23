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
import {isNullOrUndefined} from "util";
import {ComunicacionOficialDTO} from "../../domain/comunicacionOficialDTO";

export class RadicacionSalidaDTV extends  RadicacionBase {

  hasError:boolean = false;

  getCorrespondencia():CorrespondenciaDTO{

    const datosEnvio = (<RadicacionSalidaFormInterface>this.source).datosEnvio;

    let correspondencia = super.getCorrespondencia();

    correspondencia.reqDistFisica = this.source.generales.reqDistFisica == 1 ? '1' : '0';

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
        ideFunci:agenteInt.funcionario.id
      };
      agentes.push(tipoAgente);
    });

    (<RadicacionSalidaFormInterface>this.source).destinatarioExt.forEach(agenteExt => {

      const datosContactos = this.transformContactData(agenteExt.datosContactoList);

      if(!this.hasError && this.source.generales.reqDistFisica == 2){

        this.hasError = datosContactos.every( contact => isNullOrUndefined(contact.corrElectronico));
      }

      console.log("agente Externo",agenteExt);

      const tipoAgente: AgentDTO = {
        ideAgente: null,
        codTipoRemite: TIPO_REMITENTE_EXTERNO,
        codTipoPers: agenteExt.tipoPersona.codTipoPers,
        nombre: agenteExt.nombre,
        razonSocial: agenteExt.razonSocial || null,
        nit: agenteExt.nit || null,
        codCortesia: null,
        codEnCalidad: isNullOrUndefined(agenteExt.actuaCalidad )? null : agenteExt.actuaCalidad.codigo,
        codTipDocIdent: isNullOrUndefined(agenteExt.tipoDocumento) ? null : agenteExt.tipoDocumento.codigo,
        nroDocuIdentidad: agenteExt.nroDocumentoIdentidad || null,
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

  getComunicacionOficial():ComunicacionOficialDTO{

    let comunicacion = super.getComunicacionOficial();

    comunicacion.esRemitenteReferidoDestinatario = this.source.generales.responderRemitente;

    return comunicacion;
  }


}
