import {AgentDTO} from '../../domain/agentDTO';
import {ContactoDTO} from '../../domain/contactoDTO';
import {
  COMUNICACION_INTERNA,
  DATOS_CONTACTO_PRINCIPAL, DATOS_CONTACTO_SECUNDARIO,
  TIPO_AGENTE_DESTINATARIO,
  TIPO_AGENTE_REMITENTE, TIPO_REMITENTE_EXTERNO,
  TIPO_REMITENTE_INTERNO
} from '../bussiness-properties/radicacion-properties';

import {RadicacionBase} from "./radicacionBase";
import {RadicacionEntradaFormInterface} from "../interfaces/data-transformers/radicacionEntradaForm.interface";
import { DireccionDTO } from '../../domain/DireccionDTO';

export class ComunicacionOficialEntradaDTV  extends  RadicacionBase{

 getAgentesDestinatario(): Array<AgentDTO> {

    const agentes = [];

    agentes.push(this.getRemitente());

    (<RadicacionEntradaFormInterface>this.source).agentesDestinatario.forEach(agenteInt => {
      const tipoAgente: AgentDTO = {
        ideAgente: null,
        codTipoRemite: null,
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

    return agentes;
  }


  getDatosContactos(): Array<ContactoDTO> {
    const contactos = [];
    const contactsRemitente = ((<RadicacionEntradaFormInterface>this.source).datosContactos) ? (<RadicacionEntradaFormInterface>this.source).datosContactos : [];
    contactsRemitente.forEach((contact) => {
      contactos.push({
        ideContacto: null,
        nroViaGeneradora: contact.noViaPrincipal || null,
        nroPlaca: contact.placa || null,
        codTipoVia: contact.tipoVia ? contact.tipoVia.codigo : null,
        codPrefijoCuadrant: contact.prefijoCuadrante ? contact.prefijoCuadrante.codigo : null,
        /*codBis:contact.bis ? contact.bis : null,
        codOrientacion : contact.orientacion ? contact.orientacion : null,
        noVia:contact.noVia,
        codPrefijoCuadrantSe: contact.prefijoCuadrante_se ? contact.prefijoCuadrante_se : null,
        codOrientacionSe: contact.orientacion_se ? contact.orientacion_se : null,
        codTipoComplemento: contact.complementoTipo ? contact.complementoTipo : null,
        codTipoComplementoAdicional: contact.complementoAdicional ? contact.complementoAdicional : null,*/
        codPostal: null,
        direccion: this.getDireccion(contact),
        celular: contact.celular || null,
        telFijo: contact.numeroTel || null,
        extension: null,
        corrElectronico: contact.correoEle || null,
        codPais: contact.pais ? contact.pais.codigo : null,
        codDepartamento: contact.departamento ? contact.departamento.codigo : null,
        codMunicipio: contact.municipio ? contact.municipio.codigo : null,
        provEstado: contact.provinciaEstado ? contact.provinciaEstado : null,
        ciudad: contact.ciudad ? contact.ciudad : null,
        principal: contact.principal ? DATOS_CONTACTO_PRINCIPAL : DATOS_CONTACTO_SECUNDARIO,
      });
    });

    return contactos;
  }

  getDireccion(contact): string {
    const direccion: DireccionDTO = {};
    if (contact.tipoVia) {
      direccion.tipoVia = contact.tipoVia;
    }
    if (contact.noViaPrincipal) {
      direccion.noViaPrincipal = contact.noViaPrincipal;
    }
    if (contact.prefijoCuadrante) {
      direccion.prefijoCuadrante = contact.prefijoCuadrante;
    }
    if (contact.bis) {
      direccion.bis = contact.bis;
    }
    if (contact.orientacion) {
      direccion.orientacion = contact.orientacion;
    }
    if (contact.noVia) {
      direccion.noVia = contact.noVia;
    }
    if (contact.prefijoCuadrante_se) {
      direccion.prefijoCuadrante_se = contact.prefijoCuadrante_se;
    }
    if (contact.placa) {
      direccion.placa = contact.placa;
    }
    if (contact.orientacion_se) {
      direccion.orientacion_se = contact.orientacion_se;
    }
    if (contact.tipoComplemento) {
      direccion.complementoTipo = contact.tipoComplemento;
    }
    if (contact.complementoAdicional) {
      direccion.complementoAdicional = contact.complementoAdicional;
    }

    return JSON.stringify(direccion);
  }

  isRemitenteInterno() {
    return this.source.generales.tipoComunicacion.codigo === COMUNICACION_INTERNA;
  }

}
