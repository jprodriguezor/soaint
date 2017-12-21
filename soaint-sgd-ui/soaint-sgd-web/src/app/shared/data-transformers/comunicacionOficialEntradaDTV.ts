import { AgentDTO } from '../../domain/agentDTO';
import { ContactoDTO } from '../../domain/contactoDTO';
import { DocumentoDTO } from '../../domain/documentoDTO';
import { RadicacionEntradaFormInterface } from '../interfaces/data-transformers/radicacionEntradaForm.interface';
import {
  COMUNICACION_INTERNA,
  DATOS_CONTACTO_PRINCIPAL, DATOS_CONTACTO_SECUNDARIO,
  TIPO_AGENTE_DESTINATARIO,
  TIPO_AGENTE_REMITENTE, TIPO_REMITENTE_EXTERNO,
  TIPO_REMITENTE_INTERNO
} from '../bussiness-properties/radicacion-properties';
import { ReferidoDTO } from 'app/domain/referidoDTO';
import { AnexoDTO } from '../../domain/anexoDTO';
import { CorrespondenciaDTO } from '../../domain/correspondenciaDTO';
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from '../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import { State as RootState } from '../../infrastructure/redux-store/redux-reducers';
import { Store } from '@ngrx/store';
import { ComunicacionOficialDTO } from '../../domain/comunicacionOficialDTO';

export class ComunicacionOficialEntradaDTV {

  private date: Date;

  constructor(private source: RadicacionEntradaFormInterface, private _store: Store<RootState>) {
    this.date = new Date();
    console.log(source);
  }

  getTipoAgenteRemitenteInterno(): AgentDTO {

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
      codSede: this.source.remitente.sedeAdministrativa ? this.source.remitente.sedeAdministrativa.codigo : null,
      codDependencia: this.source.remitente.dependenciaGrupo ? this.source.remitente.dependenciaGrupo.codigo : null,
      fecAsignacion: null,
      codTipAgent: TIPO_AGENTE_REMITENTE,
      codEstado: null
    };
    return tipoAgente;
  }

  getTipoAgenteRemitenteExterno() {
    const tipoAgente: AgentDTO = {
      ideAgente: null,
      codTipoRemite: TIPO_REMITENTE_EXTERNO,
      codTipoPers: this.source.remitente.tipoPersona ? this.source.remitente.tipoPersona.codigo : null,
      nombre: this.source.remitente.nombreApellidos || null,
      razonSocial: this.source.remitente.razonSocial || null,
      nit: this.source.remitente.nit || null,
      codEnCalidad: this.source.remitente.actuaCalidad ? this.source.remitente.actuaCalidad.codigo : null,
      codTipDocIdent: this.source.remitente.tipoDocumento ? this.source.remitente.tipoDocumento.codigo : null,
      nroDocuIdentidad: this.source.remitente.nroDocumentoIdentidad,
      codSede: null,
      codDependencia: null,
      fecAsignacion: null,
      codTipAgent: TIPO_AGENTE_REMITENTE,
      indOriginal: null,
      codEstado: null
    };
    return tipoAgente;
  }


  getAgentesDestinatario(): Array<AgentDTO> {
    const agentes = [];

    this.source.agentesDestinatario.forEach(agenteInt => {
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

  getListaAnexos(): Array<AnexoDTO> {
    const anexoList = [];
    this.source.descripcionAnexos.forEach((anexo) => {
      anexoList.push({
        ideAnexo: null,
        codAnexo: anexo.tipoAnexo ? anexo.tipoAnexo.codigo : null,
        descripcion: anexo.descripcion
      });
    });
    return anexoList;
  }

  getListaReferidos(): Array<ReferidoDTO> {
    const referidosList = [];
    this.source.radicadosReferidos.forEach(referido => {
      referidosList.push({
        ideReferido: null,
        nroRadRef: referido.nombre
      });
    });
    return referidosList;
  }

  getDocumento(): DocumentoDTO {
    const generales = this.source.generales;
    const documento: DocumentoDTO = {
      idePpdDocumento: null,
      codTipoDoc: generales.tipologiaDocumental ? generales.tipologiaDocumental.codigo : null,
      fecDocumento: this.date.toISOString(),
      asunto: generales.asunto,
      nroFolios: generales.numeroFolio, // 'Numero Folio',
      nroAnexos: this.source.descripcionAnexos.length, // 'Numero anexos',
      codEstDoc: null,
      ideEcm: null
    };
    return documento;
  }


  getCorrespondencia(): CorrespondenciaDTO {
    const generales = this.source.generales;
    const task = this.source.task;

    const correspondenciaDto: CorrespondenciaDTO = {
      ideDocumento: null,
      descripcion: generales.asunto,
      tiempoRespuesta: generales.tiempoRespuesta,
      codUnidadTiempo: generales.unidadTiempo ? generales.unidadTiempo.codigo : null,
      codMedioRecepcion: generales.medioRecepcion ? generales.medioRecepcion.codigo : null,
      fecRadicado: this.date.toISOString(),
      fecDocumento: this.date.toISOString(),
      nroRadicado: null,
      codTipoDoc: generales.tipologiaDocumental ? generales.tipologiaDocumental.codigo : null,
      codTipoCmc: generales.tipoComunicacion ? generales.tipoComunicacion.codigo : null,
      ideInstancia: task.idInstanciaProceso,
      reqDistFisica: generales.reqDistFisica ? '1' : '0',
      codFuncRadica: null,
      codSede: null,
      codDependencia: null,
      reqDigita: generales.reqDigit ? '1' : '0',
      codEmpMsj: generales.empresaMensajeria ? generales.empresaMensajeria : null,
      nroGuia: generales.numeroGuia ? generales.numeroGuia : null,
      fecVenGestion: null,
      codEstado: null,
      inicioConteo: generales.inicioConteo || ''
    };

    this._store.select(getAuthenticatedFuncionario).subscribe(funcionario => {
      correspondenciaDto.codFuncRadica = funcionario.id;
    }).unsubscribe();

    this._store.select(getSelectedDependencyGroupFuncionario).subscribe(dependencia => {
      correspondenciaDto.codSede = dependencia.codSede;
      correspondenciaDto.codDependencia = dependencia.codigo;
    }).unsubscribe();

    return correspondenciaDto;
  }

  getDatosContactos(): Array<ContactoDTO> {
    const contactos = [];
    const contactsRemitente = (this.source.datosContactos) ? this.source.datosContactos : [];
    contactsRemitente.forEach((contact) => {
      contactos.push({
        ideContacto: null,
        nroViaGeneradora: contact.noViaPrincipal || null,
        nroPlaca: contact.placa || null,
        codTipoVia: contact.tipoVia ? contact.tipoVia.codigo : null,
        codPrefijoCuadrant: contact.prefijoCuadrante ? contact.prefijoCuadrante.codigo : null,
        codPostal: null,
        direccion: contact.direccion || null,
        celular: contact.celular || null,
        telFijo: contact.numeroTel || null,
        extension: null,
        corrElectronico: contact.correoEle || null,
        codPais: contact.pais ? contact.pais.codigo : null,
        codDepartamento: contact.departamento ? contact.departamento.codigo : null,
        codMunicipio: contact.municipio ? contact.municipio.codigo : null,
        provEstado: contact.provinciaEstado ? contact.provinciaEstado : null,
        ciudad: contact.ciudad ? contact.ciudad : null,
        principal: contact.principal ? DATOS_CONTACTO_PRINCIPAL : DATOS_CONTACTO_SECUNDARIO
      });
    });

    return contactos;
  }

  getComunicacionOficial(): ComunicacionOficialDTO {
    const agentesList = [];
    const isRemitenteInterno = this.source.generales.tipoComunicacion.codigo === COMUNICACION_INTERNA;

    if (isRemitenteInterno) {
      agentesList.push(this.getTipoAgenteRemitenteInterno());
    } else {
      agentesList.push(this.getTipoAgenteRemitenteExterno());
    }

    agentesList.push(...this.getAgentesDestinatario());

    return {
      correspondencia: this.getCorrespondencia(),
      agenteList: agentesList,
      ppdDocumentoList: [this.getDocumento()],
      anexoList: this.getListaAnexos(),
      referidoList: this.getListaReferidos(),
      datosContactoList: this.getDatosContactos()
    };
  }

  isRemitenteInterno() {
    return this.source.generales.tipoComunicacion.codigo === COMUNICACION_INTERNA;
  }

}
