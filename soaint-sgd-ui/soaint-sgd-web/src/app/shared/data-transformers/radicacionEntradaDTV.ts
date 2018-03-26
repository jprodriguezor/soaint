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

export class RadicacionEntradaDTV {

  constructor(private source: ComunicacionOficialDTO) {

  }

  getDatosRemitente(): Observable<AgentDTO> {
    return Observable.of(this.source.agenteList.find(value => value.codTipAgent === 'TP-AGEE'));
  }

  getDatosContactos(): Observable<ContactoDTO[]> {
    return Observable.of(this.source.datosContactoList);
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
        hasAnexos: null
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
      contactos.push({
        tipoVia: {codigo: contacto.codTipoVia},
        noViaPrincipal: contacto.nroViaGeneradora,
        prefijoCuadrante: {codigo: contacto.codPrefijoCuadrant},
        bis: null,
        orientacion: null,
        noVia: contacto.codTipoVia,
        prefijoCuadrante_se: null,
        placa: contacto.nroPlaca,
        orientacion_se: null,
        complementoTipo: null,
        complementoAdicional: null,
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
      referidos.push({nombre: referido.nroRadRef});
    });

    return referidos;
  }

  getAnexosFormList() {
    const anexos = [];
    this.source.anexoList.forEach((anexo: AnexoDTO) => {
      anexos.push({
        tipoAnexo: {nombre: anexo.codAnexo},
        soporteAnexo: {nombre: anexo.codTipoSoporte},
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


}
