import {Component, OnInit, ViewChild} from '@angular/core';
import {CorrespondenciaDTO} from '../../../domain/correspondenciaDTO';
import {AgentDTO} from '../../../domain/AgentDTO';
import {DocumentoDTO} from '../../../domain/DocumentoDTO';
import {AnexoDTO} from '../../../domain/AnexoDTO';
import {ReferidoDTO} from '../../../domain/ReferidoDTO';
import {ComunicacionOficialDTO} from '../../../domain/ComunicacionOficialDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {ContactoDTO} from '../../../domain/ContactoDTO';
import {ActivatedRoute} from '@angular/router';
import {Sandbox as TaskSandBox} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';

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

  barCodeVisible: boolean = false;

  editable: boolean = true;

  task: any;

  constructor(private _radicarComunicacionesSandBox: RadicarComunicacionesSandBox, private route: ActivatedRoute, private _taskSandBox: TaskSandBox) {
  }

  ngOnInit() {
    this.route.params.subscribe(values => this.task = values);
  }

  hideDialog() {
    this.barCodeVisible = false;
  }

  radicarComunicacion() {
    this.valueRemitente = this.datosRemitente.form.value;
    this.valueDestinatario = this.datosDestinatario.form.value;
    this.valueGeneral = this.datosGenerales.form.value;
    let agentesList = [];
    agentesList.push(this.getTipoAgenteExt());
    agentesList.push(...this.getAgentesInt());
    this.radicacion = {
      correspondencia: this.getCorrespondencia(),
      agenteList: agentesList,
      ppdDocumentoList: [this.getDocumento()],
      anexoList: this.getListaAnexos(),
      referidoList: this.getListaReferidos(),
      datosContactoList: this.getDatosContactos()
    };
    this._radicarComunicacionesSandBox.radicar(this.radicacion).subscribe((response) => {
      this._taskSandBox.completeTask({
        idProceso: this.task.idProceso,
        idDespliegue: this.task.idDespliegue,
        idTarea: this.task.idTarea
      }).subscribe(() => {
        this.barCodeVisible = true;
        this.radicacion = response;
        this.editable = false;
      });
    });
  }

  getTipoAgenteExt(): AgentDTO {
    let tipoAgente: AgentDTO = {
      ideAgente: null,
      codTipoRemite: null,
      codTipoPers: this.valueRemitente.tipoPersona ? this.valueRemitente.tipoPersona.codigo : null,
      nombre: this.valueRemitente.nombreApellidos,
      nroDocumentoIden: this.valueRemitente.nroDocumentoIdentidad,
      razonSocial: this.valueRemitente.razonSocial,
      nit: this.valueRemitente.nit,
      codCortesia: null,
      codCargo: null,
      codEnCalidad: this.valueRemitente.actuaCalidad ? this.valueRemitente.actuaCalidad.codigo : null,
      codTipDocIdent: this.valueRemitente.tipoDocumento ? this.valueRemitente.tipoDocumento.codigo : null,
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
        codSede: agenteInt.sedeAdministrativa ? agenteInt.sedeAdministrativa.codigo : null,
        codDependencia: agenteInt.dependenciaGrupo ? agenteInt.dependenciaGrupo.codigo : null,
        codFuncRemite: null,
        fecAsignacion: this.date.toISOString(),
        ideContacto: null,
        codTipAgent: 'INT',
        indOriginal: agenteInt.tipoDestinatario ? agenteInt.tipoDestinatario.codigo : null,
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
        codAnexo: anexo.tipoAnexo ? anexo.tipoAnexo.codigo : null,
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
      codAsunto: 'CA',
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
      descripcion: this.valueGeneral.asunto,
      tiempoRespuesta: this.valueGeneral.tiempoRespuesta,
      codUnidadTiempo: this.valueGeneral.unidadTiempo ? this.valueGeneral.unidadTiempo.codigo : null,
      codMedioRecepcion: this.valueGeneral.medioRecepcion ? this.valueGeneral.medioRecepcion.codigo : null,
      fecRadicado: this.date.toISOString(),
      fecDocumento: this.date.toISOString(),
      nroRadicado: null,
      codTipoDoc: this.valueGeneral.tipologiaDocumental ? this.valueGeneral.tipologiaDocumental.codigo : null,
      codTipoCmc: this.valueGeneral.tipoComunicacion ? this.valueGeneral.tipoComunicacion.codigo : null,
      ideInstancia: null,
      reqDistFisica: this.valueGeneral.reqDistFisica ? "1" : "0",
      codFuncRadica: null,
      codSede: null,
      codDependencia: null,
      reqDigita: this.valueGeneral.reqDigit ? "1" : "0",
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
        codTipoVia: address.tipoVia ? address.tipoVia.codigo : null,
        codPrefijoCuadrant: address.prefijoCuadrante ? address.prefijoCuadrante.codigo : null,
        codPostal: null,
        direccion: null,
        celular: null,
        telFijo1: null,
        telFijo2: null,
        extension1: null,
        extension2: null,
        corrElectronico: null,
        codPais: this.valueRemitente.pais ? this.valueRemitente.pais.codigo : null,
        codDepartamento: this.valueRemitente.departamento ? this.valueRemitente.departamento.codigo : null,
        codMunicipio: this.valueRemitente.municipio ? this.valueRemitente.municipio.codigo : null,
        provEstado: null,
        ciudad: null
      });
    });
    return contactos;
  }

}

