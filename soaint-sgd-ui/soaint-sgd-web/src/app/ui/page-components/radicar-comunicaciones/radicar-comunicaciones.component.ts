import {Component, OnInit, ViewChild} from '@angular/core';
import {CorrespondenciaDTO} from '../../../domain/correspondenciaDTO';
import {AgentDTO} from '../../../domain/AgentDTO';
import {DocumentoDTO} from '../../../domain/DocumentoDTO';
import {AnexoDTO} from '../../../domain/AnexoDTO';
import {ReferidoDTO} from '../../../domain/ReferidoDTO';
import {ComunicacionOficialDTO} from '../../../domain/ComunicacionOficialDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {ContactoDTO} from '../../../domain/ContactoDTO';

@Component({
  selector: 'app-radicar-comunicaciones',
  templateUrl: './radicar-comunicaciones.component.html'
})
export class RadicarComunicacionesComponent implements OnInit {

  @ViewChild('datosGenerales') datosGenerales;

  @ViewChild('datosRemitente') datosRemitente;

  @ViewChild('datosDestinatario') datosDestinatario;

  valueRemitente: any;

  valueDestinatario: any;

  valueGeneral: any;

  radicacion: ComunicacionOficialDTO;

  date: Date = new Date();

  constructor(private _radicarComunicacionesSandBox: RadicarComunicacionesSandBox) {
  }

  ngOnInit() {
  }

  printForm() {

    this.valueRemitente = this.datosRemitente.form.value;
    this.valueDestinatario = this.datosDestinatario.form.value;
    this.valueGeneral = this.datosGenerales.form.value;
    console.log(this.datosGenerales.form.value);
    console.log(this.datosRemitente.form.value);
    console.log(this.datosDestinatario.form.value);

    let agentesList = [];
    agentesList.push(this.getTipoAgenteExt());
    agentesList.push(...this.getAgentesInt());

    this.radicacion = {
      correspondencia: this.getCorrespondencia(),
      agenteList: agentesList,
      ppdDocumento: this.getDocumento(),
      anexoList: this.getListaAnexos(),
      referidoList: this.getListaReferidos(),
      datosContactoList: this.getDatosContactos()
    };

    console.log(this.radicacion);

    this._radicarComunicacionesSandBox.radicarDispatch(this.radicacion);
  }

  getTipoAgenteExt(): AgentDTO {
    let tipoAgente: AgentDTO = {
      ideAgente: null,
      codTipoRemite: null,
      codTipoPers: this.valueRemitente.tipoPersona.codigo,
      nombre: this.valueRemitente.nombreApellidos,
      nroDocumentoIden: null,
      razonSocial: this.valueRemitente.razonSocial,
      nit: this.valueRemitente.nit,
      codCortesia: null,
      codCargo: null,
      codEnCalidad: this.valueRemitente.actuaCalidad.codigo,
      codTipDocIdent: this.valueRemitente.tipoDocumento.codigo,
      nroDocuIdentidad: null,
      codSede: null,
      codDependencia: null,
      codFuncRemite: null,
      fecAsignacion: this.date.toISOString(),
      ideContacto: null,
      codTipAgent: 'EXT',
      indOriginal: null
    };
    return tipoAgente;
  }

  getAgentesInt(): Array<AgentDTO> {
    let agentes = [];
    this.datosDestinatario.agentesDestinatario.forEach(agenteInt => {
      let tipoAgente: AgentDTO = {
        ideAgente: null,
        codTipoRemite: null,
        codTipoPers: null,
        nombre: null,
        nroDocumentoIden: null,
        razonSocial: null,
        nit: null,
        codCortesia: null,
        codCargo: null,
        codEnCalidad: null,
        codTipDocIdent: null,
        nroDocuIdentidad: null,
        codSede: agenteInt.sedeAdministrativa.codigo,
        codDependencia: agenteInt.dependenciaGrupo.codigo,
        codFuncRemite: null,
        fecAsignacion: this.date.toISOString(),
        ideContacto: null,
        codTipAgent: 'INT',
        indOriginal: agenteInt.tipoDestinatario.codigo,
      };
      agentes.push(tipoAgente);
    });

    return agentes;
  }

  getListaAnexos(): Array<AnexoDTO> {
    let anexoList = [];
    this.datosGenerales.descripcionAnexos.forEach((anexo) => {
      anexoList.push({
        ideAnexo: null,
        codAnexo: anexo.tipoAnexo.codigo,
        descripcion: anexo.descripcion
      });
    });
    return anexoList;
  }

  getListaReferidos(): Array<ReferidoDTO> {
    let referidosList = [];
    this.datosGenerales.radicadosReferidos.forEach(referido => {
      referidosList.push({
        ideReferido: null,
        nroRadRef: referido.nombre
      });
    });
    return referidosList;
  }

  getDocumento(): DocumentoDTO {
    let documento: DocumentoDTO = {
      idePpdDocumento: null,
      codTipoDoc: null,
      fecDocumento: this.date.toISOString(),
      codAsunto: null,
      nroFolios: this.valueGeneral.numeroFolio,//'Numero Folio',
      nroAnexos: this.valueGeneral.cantidadAnexos,//'Numero anexos',
      codEstDoc: null,
      ideEcm: null,
      codTipoSoporte: null,
      codEstArchivado: null
    };
    return documento;
  }


  getCorrespondencia(): CorrespondenciaDTO {
    let correspondenciaDto: CorrespondenciaDTO = {
      ideDocumento: null,
      descripcion: null,
      tiempoRespuesta: this.valueGeneral.tiempoRespuesta,
      codUnidadTiempo: this.valueGeneral.unidadTiempo.codigo,
      codMedioRecepcion: this.valueGeneral.medioRecepcion.codigo,
      fecRadicado: this.date.toISOString(),
      fecDocumento: this.date.toISOString(),
      nroRadicado: null,
      codTipoDoc: this.valueGeneral.tipologiaDocumental.codigo,
      codTipoCmc: this.valueGeneral.tipoComunicacion.codigo,
      ideInstancia: null,
      reqDistFisica: this.valueGeneral.reqDistFisica,
      codFuncRadica: null,
      codSede: null,
      codDependencia: null,
      reqDigita: this.valueGeneral.reqDigit,
      codEmpMsj: null,
      nroGuia: null,
      fecVenGestion: null,
      codEstado: null
    };
    return correspondenciaDto;
  }

  getDatosContactos(): Array<ContactoDTO> {
    let contactos = [];
    this.datosRemitente.addresses.forEach(address => {
      contactos.push({
        ideContacto: null,
        nroViaGeneradora: address.noViaPrincipal,
        nroPlaca: null,
        codTipoVia: address.tipoVia.codigo,
        codPrefijoCuadrant: address.prefijoCuadrante.codigo,
        codPostal: null,
        direccion: null,
        celular: null,
        telFijo1: null,
        telFijo2: null,
        extension1: null,
        extension2: null,
        corrElectronico: null,
        codPais: this.valueRemitente.pais.codigo,
        codDepartamento: this.valueRemitente.departamento.codigo,
        codMunicipio: this.valueRemitente.municipio.codigo,
        provEstado: null,
        ciudad: null
      });
    });
    return contactos;
  }

}

