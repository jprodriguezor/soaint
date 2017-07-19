import {ChangeDetectionStrategy, Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
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
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {COMUNICACION_INTERNA} from '../../../shared/bussiness-properties/radicacion-properties';

declare const require: any;
const printStyles = require('app/ui/bussiness-components/ticket-radicado/ticket-radicado.component.css');

@Component({
  selector: 'app-radicar-comunicaciones',
  templateUrl: './radicar-comunicaciones.component.html',
  styleUrls: ['./radicar-comunicaciones.component.scss'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RadicarComunicacionesComponent implements OnInit {

  @ViewChild('datosGenerales') datosGenerales;

  @ViewChild('datosRemitente') datosRemitente;

  @ViewChild('datosDestinatario') datosDestinatario;

  @ViewChild('ticketRadicado') ticketRadicado;

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

  tabIndex = 0;

  formsTabOrder: Array<any> = [];

  tipoDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  sedeDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;

  constructor(private _sandbox: RadicarComunicacionesSandBox, private route: ActivatedRoute, private _taskSandBox: TaskSandBox) {
  }

  ngOnInit() {
    this.route.params.subscribe(values => this.task = values);
    this.formsTabOrder.push(this.datosGenerales);
    this.formsTabOrder.push(this.datosRemitente);
    this.formsTabOrder.push(this.datosDestinatario);

    this.tipoDestinatarioSuggestions$ = this._sandbox.tipoDestinatarioEntradaSelector();
    this.sedeDestinatarioSuggestions$ = this._sandbox.sedeDestinatarioEntradaSelector();
    this.dependenciaGrupoSuggestions$ = this._sandbox.dependenciaGrupoEntradaSelector();

    setTimeout(() => {
      const sedeRemitente = this.datosRemitente.form.get('sedeAdministrativa');
      Observable.combineLatest(
        sedeRemitente.statusChanges,
        sedeRemitente.valueChanges
      )
        .filter(([status, value]) => status === 'VALID' || status === 'DISABLED')
        .distinctUntilChanged()
        .subscribe(([status, value]) => {
          if (status === 'VALID') {
            this.datosDestinatario.deleteDestinatarioIqualRemitente(value);
            this._sandbox.sedeDestinatariEntradaFilterDispatch(value);
          } else if (status === 'DISABLED') {
            this._sandbox.sedeDestinatariEntradaFilterDispatch(null);
          }
        });
    }, 400);
  }

  radicarComunicacion() {

    this.valueRemitente = this.datosRemitente.form.value;
    this.valueDestinatario = this.datosDestinatario.form.value;
    this.valueGeneral = this.datosGenerales.form.value;
    const agentesList = [];
    const isRemitenteInterno = this.valueGeneral.tipoComunicacion.codigo === COMUNICACION_INTERNA;

    if (isRemitenteInterno) {
      agentesList.push(this.getTipoAgenteRemitenteInterno());
    } else {
      agentesList.push(this.getTipoAgenteRemitenteExterno());
    }

    agentesList.push(...this.getAgentesDestinatario());
    this.radicacion = {
      correspondencia: this.getCorrespondencia(),
      agenteList: agentesList,
      ppdDocumentoList: [this.getDocumento()],
      anexoList: this.getListaAnexos(),
      referidoList: this.getListaReferidos(),
      datosContactoList: this.getDatosContactos()
    };
    this._sandbox.radicar(this.radicacion).subscribe((response) => {
      this.barCodeVisible = true;
      this.radicacion = response;
      this.editable = false;
      this.radicacion = response;
      this.datosGenerales.form.get('fechaRadicacion').setValue(moment(this.radicacion.correspondencia.fecRadicado).format('DD/MM/YYYY hh:mm'));
      this.datosGenerales.form.get('nroRadicado').setValue(this.radicacion.correspondencia.nroRadicado);
      console.log(this.valueGeneral);
      const ticketRadicado = {
        anexos: this.valueGeneral.cantidadAnexos,
        folios: this.valueGeneral.numeroFolio,
        noRadicado: this.radicacion.correspondencia.nroRadicado,
        fecha: this.radicacion.correspondencia.fecRadicado,
        destinatarioSede: this.valueDestinatario.destinatarioPrincipal.sedeAdministrativa.nombre,
        destinatarioGrupo: this.valueDestinatario.destinatarioPrincipal.dependenciaGrupo.nombre
      };
      if (isRemitenteInterno) {
        ticketRadicado['remitenteSede'] = this.valueRemitente.sedeAdministrativa.nombre;
        ticketRadicado['remitenteGrupo'] = this.valueRemitente.dependenciaGrupo.nombre;
      } else {
        ticketRadicado['remitente'] = this.valueRemitente.nombreApellidos;
      }

      this.ticketRadicado.setDataTicketRadicado(ticketRadicado);
      this.showTicketRadicado();

      this.disableEditionOnForms();

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

  getTipoAgenteRemitenteInterno(): AgentDTO {

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
      codSede: this.valueRemitente.sedeAdministrativa ? this.valueRemitente.sedeAdministrativa.codigo : null,
      codDependencia: this.valueRemitente.dependenciaGrupo ? this.valueRemitente.dependenciaGrupo.codigo : null,
      fecAsignacion: this.date.toISOString(),
      codTipAgent: 'DES',
      codEstado: null,
      indOriginal: this.valueRemitente.tipoDestinatario ? this.valueRemitente.tipoDestinatario.codigo : null,
    };
    return tipoAgente;
  }

  getTipoAgenteRemitenteExterno() {
    const tipoAgente: AgentDTO = {
      ideAgente: null,
      codTipoRemite: this.valueGeneral.tipoComunicacion.codigo,
      codTipoPers: this.valueRemitente.tipoPersona ? this.valueRemitente.tipoPersona.codigo : null,
      nombre: this.valueRemitente.nombreApellidos || null,
      razonSocial: this.valueRemitente.razonSocial || null,
      nit: this.valueRemitente.nit || null,
      codCortesia: this.valueRemitente.codCortesia || null,
      codEnCalidad: this.valueRemitente.actuaCalidad ? this.valueRemitente.actuaCalidad.codigo : null,
      codTipDocIdent: this.valueRemitente.tipoDocumento ? this.valueRemitente.tipoDocumento.codigo : null,
      nroDocuIdentidad: this.valueRemitente.nroDocumentoIdentidad,
      codSede: null,
      codDependencia: null,
      fecAsignacion: this.date.toISOString(),
      codTipAgent: 'REM',
      indOriginal: null,
      codEstado: null
    };
    return tipoAgente;
  }


  getAgentesDestinatario(): Array<AgentDTO> {
    const agentes = [];
    this.datosDestinatario.agentesDestinatario.forEach(agenteInt => {
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
        fecAsignacion: this.date.toISOString(),
        codTipAgent: 'DES',
        codEstado: null,
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
      asunto: this.valueGeneral.asunto,
      nroFolios: this.valueGeneral.numeroFolio, // 'Numero Folio',
      nroAnexos: this.valueGeneral.cantidadAnexos, // 'Numero anexos',
      codEstDoc: null,
      ideEcm: null
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
    const contactsRemitente = (this.datosRemitente.datosContactos) ? this.datosRemitente.datosContactos.contacts : [];
    contactsRemitente.forEach(contact => {
      contactos.push({
        ideContacto: null,
        nroViaGeneradora: contact.noViaPrincipal || null,
        nroPlaca: contact.nroPlaca || null,
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
        provEstado: null,
        principal: null
      });
    });
    return contactos;
  }


  hideTicketRadicado() {
    this.barCodeVisible = false;
  }

  showTicketRadicado() {
    this.barCodeVisible = true;
  }

  disableEditionOnForms() {
    this.datosDestinatario.form.disable();
    this.datosRemitente.form.disable();
    this.datosGenerales.form.disable();
    this.editable = false;
  }

  navigateBackToWorkspace() {
    this._taskSandBox.navigateToWorkspace();
  }

  openNext() {
    this.tabIndex = (this.tabIndex === 2) ? 0 : this.tabIndex + 1;
  }

  openPrev() {
    this.tabIndex = (this.tabIndex === 0) ? 2 : this.tabIndex - 1;
  }

  updateTabIndex(event) {
    this.tabIndex = event.index;
  }

}

