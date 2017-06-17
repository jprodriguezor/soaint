import {Component, OnInit, ViewChild} from '@angular/core';
import {CorrespondenciaDTO} from '../../../domain/correspondenciaDTO';
import {AgentDTO} from '../../../domain/AgentDTO';
import {DocumentoDTO} from '../../../domain/DocumentoDTO';
import {AnexoDTO} from '../../../domain/AnexoDTO';
import {ReferidoDTO} from '../../../domain/ReferidoDTO';
import {ComunicacionOficialDTO} from '../../../domain/ComunicacionOficialDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';

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

  radicacion: ComunicacionOficialDTO;

  constructor(private _radicarComunicacionesSandBox: RadicarComunicacionesSandBox) {
  }

  ngOnInit() {
  }

  printForm() {

    this.valueRemitente = this.datosRemitente.form.value;
    this.valueDestinatario = this.datosDestinatario.form.value;
    console.log(this.datosGenerales.form.value);
    console.log(this.datosRemitente.form.value);
    console.log(this.datosDestinatario.form.value);

    let agentesList = [];
    agentesList.push(this.getTipoAgenteExt());
    agentesList.push(...this.getAgentesExt());

    this.radicacion = {
      correspondencia: this.getCorrespondencia(),
      agenteList: agentesList,
      ppdDocumento: this.getDocumento(),
      anexoList: this.getListaAnexos(),
      referidoList: this.getListaReferidos(),
      // datosContactoList: []
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
      fecAsignacion: new Date(),
      ideContacto: null,
      codTipAgent: 'EXT',
      indOriginal: null
    };
    return tipoAgente;
  }

  getAgentesExt(): Array<AgentDTO> {
    let agentes = [];
    this.datosDestinatario.agentesDestinatario.forEach(agenteInt => {
      let tipoAgente: AgentDTO = {
        ideAgente: null,
        codTipoRemite: agenteInt.tipoDestinatario.codigo,
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
        codFuncRemite: '',
        fecAsignacion: new Date(),
        ideContacto: null,
        codTipAgent: 'INT',
        indOriginal: null,
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
      codTipoDoc: '',
      fecDocumento: new Date(),
      codAsunto: '',
      nroFolios: 0,
      nroAnexos: 0,
      codEstDoc: '',
      ideEcm: '',
      codTipoSoporte: '',
      codEstArchivado: ''
    };
    return documento;
  }


  getCorrespondencia(): CorrespondenciaDTO {
    let correspondenciaDto: CorrespondenciaDTO = {
      ideDocumento: null,
      descripcion: '',
      tiempoRespuesta: '',
      codUnidadTiempo: '',
      codMedioRecepcion: '',
      fecRadicado: new Date(),
      fecDocumento: new Date(),
      nroRadicado: '',
      codTipoDoc: '',
      codTipoCmc: '',
      ideInstancia: '',
      reqDistFisica: '',
      codFuncRadica: '',
      codSede: '',
      codDependencia: '',
      reqDigita: '',
      codEmpMsj: '',
      nroGuia: '',
      fecVenGestion: '',
      codEstado: ''
    };
    return correspondenciaDto;
  }

}

