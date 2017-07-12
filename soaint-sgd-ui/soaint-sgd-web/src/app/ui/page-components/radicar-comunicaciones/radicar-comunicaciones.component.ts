import {Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {CorrespondenciaDTO} from '../../../domain/correspondenciaDTO';
import {AgentDTO} from 'app/domain/agentDTO';
import {DocumentoDTO} from 'app/domain/documentoDTO';
import {AnexoDTO} from 'app/domain/anexoDTO';
import {ReferidoDTO} from 'app/domain/referidoDTO';
import {ComunicacionOficialDTO} from 'app/domain/comunicacionOficialDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {ContactoDTO} from 'app/domain/contactoDTO';
import {ActivatedRoute} from '@angular/router';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import * as moment from 'moment';

declare const require: any;
const printStyles = require('app/ui/bussiness-components/ticket-radicado/ticket-radicado.component.css');

@Component({
  selector: 'app-radicar-comunicaciones',
  templateUrl: './radicar-comunicaciones.component.html',
  styleUrls: ['./radicar-comunicaciones.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RadicarComunicacionesComponent implements OnInit {

  @ViewChild('datosGenerales') datosGenerales;

  @ViewChild('datosRemitente') datosRemitente;

  @ViewChild('datosDestinatario') datosDestinatario;

  formStatusIcon = 'assignment';

  valueRemitente: any;

  valueDestinatario: any;

  valueGeneral: any;

  radicacion: ComunicacionOficialDTO;

  date: Date = new Date();

  barCodeVisible = false;

  editable = true;

  task: any;

  printStyle: string = printStyles;

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
    const agentesList = [];
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
      this.barCodeVisible = true;
      this.radicacion = response;
      this.editable = false;
      this.radicacion = response;
      this.datosGenerales.form.get('fechaRadicacion').setValue(moment(this.radicacion.correspondencia.fecRadicado).format('DD/MM/YYYY hh:mm'));
      this.datosGenerales.form.get('nroRadicado').setValue(this.radicacion.correspondencia.nroRadicado);
      this.barCodeVisible = true;
      this.editable = false;

      this._taskSandBox.completeTask({
        idProceso: this.task.idProceso,
        idDespliegue: this.task.idDespliegue,
        idTarea: this.task.idTarea,
        parametros: {
          requiereDigitalizacion: this.valueGeneral.reqDigit ? 1 : 0,
          numeroRadicacion: response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null
        }
      }).subscribe();
    });
  }

  getTipoAgenteExt(): AgentDTO {
    const tipoAgente: AgentDTO = {
      ideAgente: null,
      codTipoRemite: this.valueGeneral.tipoComunicacion.codigo,
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
      codSede: this.valueRemitente.sedeAdministrativa ? this.valueRemitente.sedeAdministrativa.codigo : null,
      codDependencia: this.valueRemitente.dependenciaGrupo ? this.valueRemitente.dependenciaGrupo.codigo : null,
      codFuncRemite: null,
      fecAsignacion: this.date.toISOString(),
      ideContacto: null,
      codTipAgent: 'REM',
      indOriginal: null
    };
    return tipoAgente;
  }

  getAgentesInt(): Array<AgentDTO> {
    const agentes = [];
    this.datosDestinatario.agentesDestinatario.forEach(agenteInt => {
      const tipoAgente: AgentDTO = {
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
        codTipAgent: 'DES',
        indOriginal: agenteInt.tipoDestinatario ? agenteInt.tipoDestinatario.codigo : null,
      };
      agentes.push(tipoAgente);
    });

    return agentes;
  }

  getListaAnexos(): Array<AnexoDTO> {
    const anexoList = [];
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
    const referidosList = [];
    this.datosGenerales.radicadosReferidos.forEach(referido => {
      referidosList.push({
        ideReferido: null,
        nroRadRef: referido.nombre
      });
    });
    return referidosList;
  }

  getDocumento(): DocumentoDTO {
    const documento: DocumentoDTO = {
      idePpdDocumento: null,
      codTipoDoc: null,
      fecDocumento: this.date.toISOString(),
      codAsunto: 'CA',
      nroFolios: this.valueGeneral.numeroFolio, // 'Numero Folio',
      nroAnexos: this.valueGeneral.cantidadAnexos, // 'Numero anexos',
      codEstDoc: null,
      ideEcm: null,
      codTipoSoporte: null,
      codEstArchivado: null
    };
    return documento;
  }


  getCorrespondencia(): CorrespondenciaDTO {
    const correspondenciaDto: CorrespondenciaDTO = {
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
      reqDistFisica: this.valueGeneral.reqDistFisica ? '1' : '0',
      codFuncRadica: null,
      codSede: null,
      codDependencia: null,
      reqDigita: this.valueGeneral.reqDigit ? '1' : '0',
      codEmpMsj: null,
      nroGuia: null,
      fecVenGestion: null,
      codEstado: null
    };
    return correspondenciaDto;
  }

  getDatosContactos(): Array<ContactoDTO> {
    const contactos = [];
    console.log(this.datosRemitente.addresses);
    this.datosRemitente.addresses.forEach(address => {
      contactos.push({
        ideContacto: null,
        nroViaGeneradora: address.noViaPrincipal,
        nroPlaca: null,
        codTipoVia: address.tipoVia ? address.tipoVia.codigo : null,
        codPrefijoCuadrant: address.prefijoCuadrante ? address.prefijoCuadrante.codigo : null,
        codPostal: null,
        direccion: address.direccion,
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

  navigateBackToWorkspace() {
    console.info(this.datosGenerales.form.valid);
    console.info(this.datosRemitente.form.valid);
    console.info(this.datosDestinatario.form.valid);
    // this._taskSandBox.navigateToWorkspace();
  }

  getFormStatusIcon(form) {
    form.statusChanges.subscribe(value => {
      if (value !== 'VALID') {

      }
    })
  }

}

