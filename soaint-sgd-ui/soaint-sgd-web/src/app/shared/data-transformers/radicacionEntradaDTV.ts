import {ComunicacionOficialDTO} from '../../domain/comunicacionOficialDTO';
import {Observable} from 'rxjs/Observable';
import {AgentDTO} from '../../domain/agentDTO';
import {ContactoDTO} from '../../domain/contactoDTO';
import {DocumentoDTO} from '../../domain/documentoDTO';
import {RadicacionEntradaFormInterface} from '../interfaces/data-transformers/radicacionEntradaForm.interface';
import {
  DATOS_CONTACTO_PRINCIPAL, TIPO_AGENTE_REMITENTE,
  TIPO_AGENTE_DESTINATARIO, COMUNICACION_INTERNA
} from '../bussiness-properties/radicacion-properties';
import {AnexoDTO} from '../../domain/anexoDTO';
import { DireccionDTO } from '../../domain/DireccionDTO';

export class RadicacionEntradaDTV {

  constructor(private source: ComunicacionOficialDTO) {

  }

  getDatosRemitente(): Observable<AgentDTO> {
    return Observable.of(this.source.agenteList.find(value => value.codTipAgent === 'TP-AGEE'));
  }

  getDatosContactos(): Observable<ContactoDTO[]> {
    return Observable.of(this.getDatosContactoFormList());
  }

  getDatosDocumento(): Observable<DocumentoDTO[]> {
    return Observable.of(this.source.ppdDocumentoList);
  }

  getDatosDestinatarios(): Observable<AgentDTO[]> {
    return Observable.of(this.source.agenteList.filter(value => value.codTipAgent === 'TP-AGEI'));
  }

  getDatosDestinatarioInmediate(): AgentDTO[] {
    return this.source.agenteList.filter(value => value.codTipAgent === 'TP-AGEI');
  }

  getRadicacionEntradaFormData(): RadicacionEntradaFormInterface {

    return {
      generales: {
        fechaRadicacion: this.source.correspondencia.fecRadicado,
        nroRadicado: this.source.correspondencia.nroRadicado,
        tipoComunicacion: this.source.correspondencia.codTipoCmc,
        medioRecepcion: {codigo: this.source.correspondencia.codMedioRecepcion},
        empresaMensajeria: this.source.correspondencia.codEmpMsj,
        numeroGuia: this.source.correspondencia.nroGuia,
        tipologiaDocumental: {codigo: this.source.correspondencia.codTipoDoc},
        unidadTiempo: {codigo: this.source.correspondencia.codUnidadTiempo},
        numeroFolio: this.source.ppdDocumentoList.length > 0 ? this.source.ppdDocumentoList[0].nroFolios : null,
        inicioConteo: this.source.correspondencia.inicioConteo,
        reqDistFisica: this.source.correspondencia.reqDistFisica,
        reqDigit: this.source.correspondencia.reqDigita,
        reqDigitInmediata: null,
        tiempoRespuesta: this.source.correspondencia.tiempoRespuesta,
        asunto: this.source.ppdDocumentoList[0].asunto,
        radicadoReferido: null,
        tipoAnexos: null,
        tipoAnexosDescripcion: null,
        hasAnexos: null,
        ideDocumento: this.source.correspondencia.ideDocumento,
        idePpdDocumento: this.source.ppdDocumentoList[0].idePpdDocumento,
      },
      datosContactos: this.getDatosContactoFormList(),
      radicadosReferidos: this.getRadicadosReferidosFormList(),
      remitente: this.getRemitenteForm(),
      descripcionAnexos: this.getAnexosFormList(),
      agentesDestinatario: this.getDestinatarioFormList()
    }
  }

  getDatosContactoFormList() {
    const contactos = [];
    this.source.datosContactoList.forEach(contacto => {
    const direccion: DireccionDTO = this.GetDireccion(contacto);
    contactos.push({
        tipoVia: {codigo: contacto.codTipoVia},
        noViaPrincipal: contacto.nroViaGeneradora,
        prefijoCuadrante: {codigo: contacto.codPrefijoCuadrant},
        bis: (direccion) ? direccion.bis : null,
        orientacion: (direccion) ? direccion.orientacion : null,
        direccion: this.GetDireccionText(contacto),
        noVia: (direccion) ? direccion.noVia : null,
        prefijoCuadrante_se: (direccion) ? direccion.prefijoCuadrante_se : null,
        placa: contacto.nroPlaca,
        orientacion_se: (direccion) ? direccion.orientacion_se : null,
        complementoTipo: (direccion) ? direccion.complementoTipo : null,
        complementoAdicional: (direccion) ? direccion.complementoAdicional : null,
        celular: contacto.celular,
        numeroTel: contacto.telFijo,
        correoEle: contacto.corrElectronico,
        pais: {codigo: contacto.codPais},
        departamento: {codigo: contacto.codDepartamento},
        municipio: {codigo: contacto.codMunicipio},
        principal: contacto.principal === DATOS_CONTACTO_PRINCIPAL ? true : false,
      });
    });

    return contactos;
  }

  getRadicadosReferidosFormList() {
    const referidos = [];
    this.source.referidoList.forEach(referido => {
      referidos.push({ideReferido: referido.ideReferido, nombre: referido.nroRadRef});
    });

    return referidos;
  }

  getAnexosFormList() {
    const anexos = [];
    this.source.anexoList.forEach((anexo: AnexoDTO) => {
      anexos.push({
        ideAnexo: anexo.ideAnexo,
        tipoAnexo: {codigo: anexo.codAnexo},
        soporteAnexo: {codigo: anexo.codTipoSoporte},
        descripcion: anexo.descripcion
      });
    });

    return anexos;
  }

  getRemitenteInternoForm(remitente: AgentDTO) {
    return {
      tipoPersona: null,
      nit: null,
      actuaCalidad: null,
      tipoDocumento: null,
      razonSocial: null,
      nombreApellidos: null,
      nroDocumentoIdentidad: null,
      sedeAdministrativa: {codigo: remitente.codSede},
      dependenciaGrupo: {codigo: remitente.codDependencia}
    }
  }

  getRemitenteExternoForm(remitente: AgentDTO) {
    return {
      tipoPersona: {codigo: remitente.codTipoPers},
      nit: remitente.nit,
      actuaCalidad: {codigo: remitente.codEnCalidad},
      tipoDocumento: {codigo: remitente.codTipDocIdent},
      razonSocial: remitente.razonSocial,
      nombreApellidos: remitente.nombre,
      nroDocumentoIdentidad: remitente.nroDocuIdentidad,
      sedeAdministrativa: null,
      dependenciaGrupo: null
    }
  }

  getRemitenteForm() {
    const isRemitenteInterno = this.source.correspondencia.codTipoCmc === COMUNICACION_INTERNA;
    const agente = this.source.agenteList.find((agente: AgentDTO) => agente.codTipAgent === TIPO_AGENTE_REMITENTE);
    if (isRemitenteInterno) {
      return this.getRemitenteInternoForm(agente);
    } else {
      return this.getRemitenteExternoForm(agente);
    }

  }

  getDestinatarioFormList() {
    const destinatarios = [];
    this.source.agenteList.filter((agente: AgentDTO) => agente.codTipAgent === TIPO_AGENTE_DESTINATARIO).forEach((destinatario: AgentDTO) => {
      destinatarios.push({
        tipoDestinatario: {codigo: destinatario.indOriginal},
        sedeAdministrativa: {codigo: destinatario.codSede},
        dependenciaGrupo: {codigo: destinatario.codDependencia}
      });
    });
    return destinatarios;
  }

  GetDireccion(contact): DireccionDTO {
    let direccion: DireccionDTO  = {};
    try {
      direccion =  JSON.parse(contact.direccion);
    } catch (e) {
      direccion = null;
    }
    if (direccion) {
      direccion.tipoVia = (direccion.tipoVia) ? direccion.tipoVia : null;
      direccion.noViaPrincipal = (direccion.noViaPrincipal) ? direccion.noViaPrincipal : null;
      direccion.prefijoCuadrante = (direccion.prefijoCuadrante) ? direccion.prefijoCuadrante : null;
      direccion.bis = (direccion.bis) ? direccion.bis : null;
      direccion.orientacion = (direccion.orientacion) ? direccion.orientacion : null;
      direccion.noVia = (direccion.noVia) ? direccion.noVia : null;
      direccion.prefijoCuadrante_se = (direccion.prefijoCuadrante_se) ? direccion.prefijoCuadrante_se : null;
      direccion.placa = (direccion.placa) ? direccion.placa : null;
      direccion.orientacion_se = (direccion.orientacion_se) ? direccion.orientacion_se : null;
      direccion.complementoTipo = (direccion.complementoTipo) ? direccion.complementoTipo : null;
      direccion.complementoAdicional = (direccion.complementoAdicional) ? direccion.complementoAdicional : null;
    }
    return direccion;
  }

  GetDireccionText(contact): string {
    let direccion: DireccionDTO  = {};
    let direccionText = '';
    try {
      direccion =  JSON.parse(contact.direccion);
    } catch (e) {
      return direccionText;
    }
    if (direccion) {
      if (direccion.tipoVia) {
        direccionText += direccion.tipoVia.nombre;
      }
      if (direccion.noViaPrincipal) {
        direccionText += ' ' + direccion.noViaPrincipal;
      }
      if (direccion.prefijoCuadrante) {
        direccionText += ' ' + direccion.prefijoCuadrante.nombre;
      }
      if (direccion.bis) {
        direccionText += ' ' + direccion.bis.nombre;
      }
      if (direccion.orientacion) {
        direccionText += ' ' + direccion.orientacion.nombre;
      }
      if (direccion.noVia) {
        direccionText += ' ' + direccion.noVia;
      }
      if (direccion.prefijoCuadrante_se) {
        direccionText += ' ' + direccion.prefijoCuadrante_se.nombre;
      }
      if (direccion.placa) {
        direccionText += ' ' + direccion.placa;
      }
      if (direccion.orientacion_se) {
        direccionText += ' ' + direccion.orientacion_se.nombre;
      }
      if (direccion.complementoTipo) {
        direccionText += ' ' + direccion.complementoTipo.nombre;
      }
      if (direccion.complementoAdicional) {
        direccionText += ' ' + direccion.complementoAdicional;
      }

    }
    return direccionText;
  }

}
