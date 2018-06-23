import {CorrespondenciaDTO} from "../../domain/correspondenciaDTO";
import {RadicacionEntradaFormInterface} from "../interfaces/data-transformers/radicacionEntradaForm.interface";
import {Store} from "@ngrx/store";
import {State as RootState} from "../../infrastructure/redux-store/redux-reducers";
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {DocumentoDTO} from "../../domain/documentoDTO";
import {ComunicacionOficialDTO} from "../../domain/comunicacionOficialDTO";
import {ReferidoDTO} from "../../domain/referidoDTO";
import {AnexoDTO} from "../../domain/anexoDTO";
import {AgentDTO} from "../../domain/agentDTO";
import {ContactoDTO} from "../../domain/contactoDTO";
import {RadicacionFormInterface} from "../interfaces/data-transformers/radicacionForm.interface";
import {AgenteFactoryDTV} from "./agentesDTV";
import {DATOS_CONTACTO_PRINCIPAL, DATOS_CONTACTO_SECUNDARIO} from "../bussiness-properties/radicacion-properties";
import {isNullOrUndefined} from "util";
import {DireccionDTO} from "../../domain/DireccionDTO";

export  abstract class RadicacionBase {

  protected  date:Date;

  constructor(protected source: RadicacionFormInterface,private _store: Store<RootState>) {

    this.date = new Date();
  }

  getCorrespondencia(): CorrespondenciaDTO {
    const generales = this.source.generales;
    const task = this.source.task;

    const correspondenciaDto: CorrespondenciaDTO = {
      ideDocumento: generales.ideDocumento,
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
      reqDigita: generales.reqDigit,
      codEmpMsj: generales.empresaMensajeria ? generales.empresaMensajeria : null,
      nroGuia: generales.numeroGuia ? generales.numeroGuia : null,
      fecVenGestion: null,
      codEstado: null,
      inicioConteo: generales.inicioConteo || '',
      codClaseEnvio:null,
      codModalidadEnvio:null,
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

  getDocumento(): DocumentoDTO {
    const generales = this.source.generales;
    return {
      idePpdDocumento: this.source.generales.idePpdDocumento,
      codTipoDoc: generales.tipologiaDocumental ? generales.tipologiaDocumental.codigo : null,
      fecDocumento: this.date.toISOString(),
      asunto: generales.asunto,
      nroFolios: generales.numeroFolio, // 'Numero Folio',
      nroAnexos: this.source.descripcionAnexos.length, // 'Numero anexos',
      codEstDoc: null,
      ideEcm: !isNullOrUndefined(generales.ideEcm) ? generales.ideEcm : null
    };
  }

  getListaReferidos(): Array<ReferidoDTO> {
    const referidosList = [];
    if(!isNullOrUndefined(this.source.radicadosReferidos))
     this.source.radicadosReferidos.forEach(referido => {
      referidosList.push({
        ideReferido: referido.ideReferido,
        nroRadRef: referido.nombre
      });
    });
    return referidosList;
  }

  getListaAnexos(): AnexoDTO[] {
    const anexoList = [];
    this.source.descripcionAnexos.forEach((anexo) => {
      console.log(anexo);
      anexoList.push({
        ideAnexo: anexo.ideAnexo,
        codAnexo: anexo.tipoAnexo ? anexo.tipoAnexo.codigo : null,
        descripcion: anexo.descripcion,
        codTipoSoporte: anexo.soporteAnexo.codigo
      });
    });
    return anexoList;
  }

  abstract  getAgentesDestinatario(): Array<AgentDTO>;

   getRemitente():AgentDTO{

     const tipoComunicacion = this.source.generales.tipoComunicacion.codigo ;

     return AgenteFactoryDTV.getAgente(tipoComunicacion).getRemitente(this.source.remitente);
   }

  getComunicacionOficial(): ComunicacionOficialDTO {

    return {
      correspondencia: this.getCorrespondencia(),
      agenteList: this.getAgentesDestinatario(),
      ppdDocumentoList: [this.getDocumento()],
      anexoList: this.getListaAnexos(),
      referidoList: this.getListaReferidos(),
      datosContactoList: this.getDatosContactos()
    };
  }

  transformContactData(contactOrigin): Array<ContactoDTO> {
    const contactos = [];
    contactOrigin.forEach((contact) => {
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

  getDatosContactos(): Array<ContactoDTO> {

    return null;
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
