import {
  ChangeDetectionStrategy, Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation, AfterViewInit,
  AfterContentInit
} from '@angular/core';
import {CorrespondenciaDTO} from '../../../domain/correspondenciaDTO';
import {AgentDTO} from 'app/domain/agentDTO';
import {DocumentoDTO} from 'app/domain/documentoDTO';
import {AnexoDTO} from 'app/domain/anexoDTO';
import {ReferidoDTO} from 'app/domain/referidoDTO';
import {ComunicacionOficialDTO} from 'app/domain/comunicacionOficialDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {ContactoDTO} from 'app/domain/contactoDTO';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import * as moment from 'moment';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {
  COMUNICACION_INTERNA, DATOS_CONTACTO_PRINCIPAL, DATOS_CONTACTO_SECUNDARIO, TIPO_AGENTE_DESTINATARIO,
  TIPO_AGENTE_REMITENTE, TIPO_REMITENTE_EXTERNO, TIPO_REMITENTE_INTERNO
} from '../../../shared/bussiness-properties/radicacion-properties';
import {Store} from '@ngrx/store';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {
  sedeDestinatarioEntradaSelector,
  tipoDestinatarioEntradaSelector
} from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors';
import {getArrayData as DependenciaGrupoSelector} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {ScheduleNextTaskAction} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';
import {TareaDTO} from '../../../domain/tareaDTO';
import {TaskForm} from '../../../shared/interfaces/task-form.interface';
import {LoadDatosRemitenteAction} from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-actions';
import {TaskTypes} from '../../../shared/type-cheking-clasess/class-types';
import {
  getMediosRecepcionVentanillaData
} from '../../../infrastructure/state-management/constanteDTO-state/selectors/medios-recepcion-selectors';
import {LoadNextTaskPayload} from '../../../shared/interfaces/start-process-payload,interface';
import {getDestinatarioPrincial} from '../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-destinatario-selectors';
import {RadicarSuccessAction} from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-actions';
declare const require: any;
const printStyles = require('app/ui/bussiness-components/ticket-radicado/ticket-radicado.component.css');

@Component({
  selector: 'app-radicar-comunicaciones',
  templateUrl: './radicar-comunicaciones.component.html',
  styleUrls: ['./radicar-comunicaciones.component.scss'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RadicarComunicacionesComponent implements OnInit, AfterContentInit, AfterViewInit, OnDestroy, TaskForm {

  type = TaskTypes.TASK_FORM;

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

  mediosRecepcionDefaultSelection$: Observable<ConstanteDTO>;
  tipoDestinatarioDefaultSelection$: Observable<ConstanteDTO>;

  // Unsubscribers
  activeTaskUnsubscriber: Subscription;
  radicacionUnsubscriber: Subscription;
  sedeUnsubscriber: Subscription;
  validDatosGeneralesUnsubscriber: Subscription;
  reqDigitInmediataUnsubscriber: Subscription;

  constructor(private _sandbox: RadicarComunicacionesSandBox,
              private _store: Store<RootState>,
              private _taskSandBox: TaskSandBox) {
  }

  ngOnInit() {
    // Default Selection for Children Components bindings
    this.mediosRecepcionDefaultSelection$ = this._store.select(getMediosRecepcionVentanillaData);
    this.tipoDestinatarioDefaultSelection$ = this._store.select(getDestinatarioPrincial);

    // Datalist Load bindings
    this.tipoDestinatarioSuggestions$ = this._store.select(tipoDestinatarioEntradaSelector);
    this.sedeDestinatarioSuggestions$ = this._store.select(sedeDestinatarioEntradaSelector);
    this.dependenciaGrupoSuggestions$ = this._store.select(DependenciaGrupoSelector);
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
    });
  }

  ngAfterContentInit() {
    this.formsTabOrder.push(this.datosGenerales);
    this.formsTabOrder.push(this.datosRemitente);
    this.formsTabOrder.push(this.datosDestinatario);
  }

  ngAfterViewInit() {

    const sedeRemitente = this.datosRemitente.form.get('sedeAdministrativa');
    this.sedeUnsubscriber = Observable.combineLatest(
      sedeRemitente.statusChanges,
      sedeRemitente.valueChanges
    )
      .filter(([status, value]) => status === 'VALID' || status === 'DISABLED')
      .distinctUntilChanged()
      .subscribe(([status, value]) => {
        if (status === 'VALID') {
          this.datosDestinatario.deleteDestinatarioIqualRemitente(value);
          this._sandbox.dispatchSedeDestinatarioEntradaFilter(value);
        } else if (status === 'DISABLED') {
          this._sandbox.dispatchSedeDestinatarioEntradaFilter(null);
        }
      });

    this.validDatosGeneralesUnsubscriber = this.datosGenerales.form.statusChanges.filter(value => value === 'VALID').first()
      .subscribe(() => {
        this._store.dispatch(new LoadDatosRemitenteAction())
      });

    this.reqDigitInmediataUnsubscriber = this.datosGenerales.form.get('reqDigitInmediata').valueChanges
      .subscribe(value => {
        const payload: LoadNextTaskPayload = {
          idProceso: this.task.idProceso,
          idInstanciaProceso: this.task.idInstanciaProceso,
          idDespliegue: this.task.idDespliegue
        };
        this._store.dispatch(new ScheduleNextTaskAction(payload));
      });
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

    this.radicacionUnsubscriber = this._sandbox.radicar(this.radicacion).subscribe((response) => {
      this.barCodeVisible = true;
      this.radicacion = response;
      this.editable = false;
      this.datosGenerales.form.get('fechaRadicacion').setValue(moment(this.radicacion.correspondencia.fecRadicado).format('DD/MM/YYYY hh:mm'));
      this.datosGenerales.form.get('nroRadicado').setValue(this.radicacion.correspondencia.nroRadicado);

      const ticketRadicado = {
        anexos: this.datosGenerales.descripcionAnexos.length,
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

      this._store.dispatch(new RadicarSuccessAction({
        tipoComunicacion: this.valueGeneral.tipoComunicacion,
        numeroRadicado: response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null
      }));

      this._taskSandBox.completeTaskDispatch({
        idProceso: this.task.idProceso,
        idDespliegue: this.task.idDespliegue,
        idTarea: this.task.idTarea,
        parametros: {
          requiereDigitalizacion: this.valueGeneral.reqDigit ? 1 : 0,
          digitalizacionInmediata: this.valueGeneral.reqDigitInmediata ? 1 : 0,
          numeroRadicado: response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null,

        }
      });
    });
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
      codSede: this.valueRemitente.sedeAdministrativa ? this.valueRemitente.sedeAdministrativa.codigo : null,
      codDependencia: this.valueRemitente.dependenciaGrupo ? this.valueRemitente.dependenciaGrupo.codigo : null,
      fecAsignacion: null,
      codTipAgent: TIPO_AGENTE_REMITENTE,
      codEstado: null,
      indOriginal: this.valueRemitente.tipoDestinatario ? this.valueRemitente.tipoDestinatario.codigo : null,
    };
    return tipoAgente;
  }

  getTipoAgenteRemitenteExterno() {
    const tipoAgente: AgentDTO = {
      ideAgente: null,
      codTipoRemite: TIPO_REMITENTE_EXTERNO,
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
      fecAsignacion: null,
      codTipAgent: TIPO_AGENTE_REMITENTE,
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
      nroAnexos: this.datosGenerales.descripcionAnexos.length, // 'Numero anexos',
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
      ideInstancia: this.task.idInstanciaProceso,
      reqDistFisica: this.valueGeneral.reqDistFisica ? '1' : '0',
      codFuncRadica: null,
      codSede: null,
      codDependencia: null,
      reqDigita: this.valueGeneral.reqDigit ? '1' : '0',
      codEmpMsj: null,
      nroGuia: null,
      fecVenGestion: null,
      codEstado: null,
      inicioConteo: this.valueGeneral.inicioConteo || ''
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
        principal: contact.principal ? DATOS_CONTACTO_PRINCIPAL : DATOS_CONTACTO_SECUNDARIO
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

  openNext() {
    this.tabIndex = (this.tabIndex === 2) ? 0 : this.tabIndex + 1;
  }

  openPrev() {
    this.tabIndex = (this.tabIndex === 0) ? 2 : this.tabIndex - 1;
  }

  updateTabIndex(event) {
    this.tabIndex = event.index;
  }

  getTask(): TareaDTO {
    return this.task;
  }

  save(): Observable<any> {
    return Observable.of(true).delay(5000);
  }

  ngOnDestroy() {
    this.activeTaskUnsubscriber.unsubscribe();
    this.validDatosGeneralesUnsubscriber.unsubscribe();
    this.reqDigitInmediataUnsubscriber.unsubscribe();
    this.sedeUnsubscriber.unsubscribe();
    this.radicacionUnsubscriber.unsubscribe();
  }

}

