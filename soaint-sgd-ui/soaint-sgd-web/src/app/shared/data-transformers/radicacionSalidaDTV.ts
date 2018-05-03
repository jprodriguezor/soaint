import {AgentDTO} from '../../domain/agentDTO';
import {RadicacionBase} from './radicacionBase';
import {TIPO_AGENTE_DESTINATARIO} from '../bussiness-properties/radicacion-properties';
import {RadicacionSalidaFormInterface} from '../interfaces/data-transformers/radicacionSalidaForm.interface';
import {isNullOrUndefined} from "util";

export class RadicacionSalidaDTV extends  RadicacionBase {

  hasError:boolean = false;

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
        codTipoRemite: agenteInt.tipoDestinatario.codigo,
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
        indOriginal: agenteInt.tipoDestinatario ? agenteInt.tipoDestinatario.codigo : null,
      };
      agentes.push(tipoAgente);
    });

    (<RadicacionSalidaFormInterface>this.source).destinatarioExt.forEach(agenteExt => {

      const datosContactos = this.transformContactData(agenteExt.datosContactoList);

      if(!this.hasError && !this.source.generales.reqDistFisica){

        this.hasError = datosContactos.every( contact => isNullOrUndefined(contact.corrElectronico));
      }

      const tipoAgente: AgentDTO = {
        ideAgente: null,
        codTipoRemite: agenteExt.tipoDestinatario.codigo,
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
        indOriginal: agenteExt.tipoDestinatario ? agenteExt.tipoDestinatario.codigo : null,
      };
      agentes.push(tipoAgente);
    });

    return agentes;
  }
}
