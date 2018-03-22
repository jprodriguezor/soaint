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

export  abstract class RadicacionBase {

  protected  date:Date;

  constructor(protected source: RadicacionFormInterface,private _store: Store<RootState>) {

    this.date = new Date();
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

  getDocumento(): DocumentoDTO {
    const generales = this.source.generales;
    return {
      idePpdDocumento: null,
      codTipoDoc: generales.tipologiaDocumental ? generales.tipologiaDocumental.codigo : null,
      fecDocumento: this.date.toISOString(),
      asunto: generales.asunto,
      nroFolios: generales.numeroFolio, // 'Numero Folio',
      nroAnexos: this.source.descripcionAnexos.length, // 'Numero anexos',
      codEstDoc: null,
      ideEcm: null
    };
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

  getListaAnexos(): AnexoDTO[] {
    const anexoList = [];
    this.source.descripcionAnexos.forEach((anexo) => {
      console.log(anexo);
      anexoList.push({
        ideAnexo: null,
        codAnexo: anexo.tipoAnexo ? anexo.tipoAnexo.codigo : null,
        descripcion: anexo.descripcion,
        codTipoSoporte: anexo.soporteAnexo.codigo
      });
    });
    return anexoList;
  }

  abstract  getAgentesDestinatario(): Array<AgentDTO>;



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

  getDatosContactos(): Array<ContactoDTO>{

    return null;
  }

}