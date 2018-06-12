import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {PlanAgenDTO} from '../../../domain/PlanAgenDTO';
import {RadicacionEntradaDTV} from '../../../shared/data-transformers/radicacionEntradaDTV';
import {AgentDTO} from '../../../domain/agentDTO';
import {Observable} from 'rxjs/Observable';
import {getTipologiaDocumentalArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/tipologia-documental-selectors';
import {FormBuilder, FormGroup} from '@angular/forms';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';
import {Subscription} from 'rxjs/Subscription';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {SUCCESS_ADJUNTAR_DOCUMENTO} from 'app/shared/lang/es';
import {ERROR_ADJUNTAR_DOCUMENTO} from 'app/shared/lang/es';
import {Store} from '@ngrx/store';
import {PlanillasApiService} from '../../../infrastructure/api/planillas.api';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {
  getArrayData as getFuncionarioArrayData,
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {
  getAgragarObservacionesDialogVisible,
  getDetailsDialogVisible,
  getJustificationDialogVisible,
  getRejectDialogVisible
} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-selectors';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as FuncionarioSandbox} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Sandbox as taskSandbox} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {getArrayData as PlanillasArrayData} from '../../../infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-selectors';
import {Sandbox as CargarPlanillasSandbox} from '../../../infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-sandbox';
import {PlanillaDTO} from '../../../domain/PlanillaDTO';
import {PlanAgentesDTO} from '../../../domain/PlanAgentesDTO';
import {environment} from '../../../../environments/environment';
import {correspondenciaEntrada} from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors';
import {ApiBase} from '../../../infrastructure/api/api-base';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {CompleteTaskAction} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";
import { afterTaskComplete } from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers';


@Component({
  selector: 'app-cargar-planilla-salida',
  templateUrl: './cargar-planilla-salida.component.html',
  styleUrls: ['./cargar-planilla-salida.component.css']
})
export class CargarPlanillaSalidaComponent implements OnInit {

  form:FormGroup;

  planAgentes:PlanAgenDTO[] = [];

  data:PlanillaDTO | any = {};

  selectedComunications:PlanAgenDTO[] = [];

  start_date:Date = new Date();

  editarPlanillaDialogVisible = false;

  dependencia:any;

  funcionariosSuggestions$:Observable<FuncionarioDTO[]>;

  justificationDialogVisible$:Observable<boolean>;

  detailsDialogVisible$:Observable<boolean>;

  agregarObservacionesDialogVisible$:Observable<boolean>;

  rejectDialogVisible$:Observable<boolean>;

  dependenciaSelected$:Observable<DependenciaDTO>;

  dependenciaSelected:DependenciaDTO;

  funcionarioLog:FuncionarioDTO;

  funcionarioSubcription:Subscription;

  comunicacionesSubcription:Subscription;

  tipologiaDocumentalSuggestions$:Observable<ConstanteDTO[]>;

  tipologiasDocumentales:ConstanteDTO[];

  dependencias:DependenciaDTO[] = [];

  activeTaskUnsubscriber:Subscription;

  @ViewChild('popupEditar') popupEditar;

  uploadUrl:string;

  task:any;

  closedTask:any;

  nroPlanilla:string;

  constructor(private _store:Store<RootState>,
              private _cargarPlanillasApi:CargarPlanillasSandbox,
              private _funcionarioSandbox:FuncionarioSandbox,
              private _constSandbox:ConstanteSandbox,
              private _dependenciaSandbox:DependenciaSandbox,
              private _planillaService:PlanillasApiService,
              private _changeDetectorRef:ChangeDetectorRef,
              private _taskSandbox: taskSandbox,
              private _api:ApiBase,
              private formBuilder:FormBuilder) {
    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
    this.dependenciaSelected$.subscribe((result) => {
      this.dependenciaSelected = result;
    });
    this.funcionariosSuggestions$ = this._store.select(getFuncionarioArrayData);
    this.justificationDialogVisible$ = this._store.select(getJustificationDialogVisible);
    this.detailsDialogVisible$ = this._store.select(getDetailsDialogVisible);
    this.agregarObservacionesDialogVisible$ = this._store.select(getAgragarObservacionesDialogVisible);
    this.rejectDialogVisible$ = this._store.select(getRejectDialogVisible);
    this.start_date.setHours(this.start_date.getHours() - 24);
    this.funcionarioSubcription = this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });
    this.comunicacionesSubcription = this._store.select(PlanillasArrayData).subscribe((result) => {
      this.selectedComunications = [];
    });

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      if (this.task && this.task.variables) {
        this.nroPlanilla = this.task.variables.numPlanilla;
      }
    });

    this.initForm();
  }

  ngOnInit() {
    this.closedTask = afterTaskComplete.map(() => true).startWith(false);
    this.uploadUrl = environment.digitalizar_doc_upload_endpoint;
    this.tipologiaDocumentalSuggestions$ = this._store.select(getTipologiaDocumentalArrayData);
    this.tipologiaDocumentalSuggestions$.subscribe((results) => {
      this.tipologiasDocumentales = results;
    });

    this._funcionarioSandbox.loadAllFuncionariosDispatch();
    this._constSandbox.loadDatosGeneralesDispatch();
    this.listarDependencias();
  }

  getDatosRemitente(comunicacion):Observable<AgentDTO> {
    const radicacionEntradaDTV = new RadicacionEntradaDTV(comunicacion);
    return radicacionEntradaDTV.getDatosRemitente();
  }

  ngOnDestroy() {
    this.funcionarioSubcription.unsubscribe();
    this.comunicacionesSubcription.unsubscribe();
    this.activeTaskUnsubscriber.unsubscribe();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'numeroPlanilla': [null]
    });
  }

  getEstadoLabel(estado) {
    return this.popupEditar.estadoEntregaSuggestions.find((element) => element.codigo === estado);
  }

  listarDependencias() {
    this._dependenciaSandbox.loadDependencies({}).subscribe((results) => {
      this.dependencias = results.dependencias;
      this.listarPlanAgentes();
    });
  }

  findDependency(code):string {
    const result = this.dependencias.find((element) => element.codigo === code);
    return result ? result.nombre : '';
  }

  findSede(code):string {
    const result = this.dependencias.find((element) => element.codSede === code);
    return result ? result.nomSede : '';
  }

  listarPlanAgentes() {
    this._cargarPlanillasApi.loadPlanillasSalida({
      nro_planilla: this.nroPlanilla,
    }).subscribe((result) => {
      this.data = result;
      this.planAgentes = [...result.pagentes.pagente];
      this.refreshView();
    });
  }

  findNombrePais(agente: PlanAgenDTO): string {
    return 'Colombia';
  }

  findNombreMunicipio(agente: PlanAgenDTO): string {
    return 'Bogota';
  }

  findNombreDepartamento(agente: PlanAgenDTO): string {
    return 'CO';
  }
  findDireccion(agente: PlanAgenDTO): string  {
    return '1 y 2'
  }


  showEditarPlanillaDialog() {
    this.popupEditar.resetData();
    this.editarPlanillaDialogVisible = true;
  }

  hideEditarPlanillaDialog() {
    this.editarPlanillaDialogVisible = false;
  }

  createAgents():PlanAgenDTO[] {
    const agents:PlanAgenDTO[] = [];
    this.selectedComunications.forEach((element) => {
      const agent:PlanAgenDTO = {
        idePlanAgen: element.idePlanAgen,
        estado: this.popupEditar.form.get('estadoEntrega').value.codigo,
        varPeso: element.varPeso,
        varValor: element.varValor,
        varNumeroGuia: element.varNumeroGuia,
        fecObservacion: element.fecObservacion,
        usuario: this.funcionarioLog.nombre,
        codNuevaSede: this.popupEditar.form.get('dependenciaDestino').value ? this.popupEditar.form.get('dependenciaDestino').value.codSede : null,
        codNuevaDepen: this.popupEditar.form.get('dependenciaDestino').value ? this.popupEditar.form.get('dependenciaDestino').value.codigo : null,
        observaciones: this.popupEditar.form.get('observaciones').value,
        codCauDevo: element.codCauDevo,
        fecCarguePla: this.popupEditar.form.get('fechaEntrega').value,
        ideAgente: element.ideAgente,
        ideDocumento: element.ideDocumento,
        nroRadicado: element.nroRadicado,
        tipoPersona: element.tipoPersona,
        tipologiaDocumental: null,
        nit: element.nit,
        nroDocuIdentidad: element.nroDocuIdentidad,
        nombre: element.nombre,
        razonSocial: element.razonSocial,
        folios: element.folios,
        anexos: element.anexos,
      };
      agents.push(agent);
    });

    return agents;
  }

  editarPlanilla() {
    const agents: PlanAgenDTO[] = this.createAgents();
    const coms = [...this.planAgentes];
    agents.forEach((element) => {
      const index = coms.findIndex((el) => el.ideAgente === element.ideAgente);
      if (index > -1) {
        coms[index] = element;
      }
    });
    this.planAgentes = [...coms];
    this.selectedComunications = [];
    this.hideEditarPlanillaDialog();
    this.refreshView();

  }

  onDocUploaded(event):void {
    console.log(event);
  }

  onClear(event) {
    console.log(event);
  }

  customUploader(event) {

    const formData = new FormData();


    event.files.forEach((file) => {

      formData.append('file[]', file, file.name);

      this._store.select(correspondenciaEntrada).take(1).subscribe((value) => {
        formData.append('tipoComunicacion', '1');
        formData.append('fileName', '2');
        this._api.sendFile(this.uploadUrl, formData, []).subscribe(response => {
          let objresponse = JSON.parse(response.ecmIds[0]);
          console.log(objresponse);
          if (objresponse.codMensaje == "1111") {
            this._store.dispatch(new PushNotificationAction({
              severity: 'error',
              summary: ERROR_ADJUNTAR_DOCUMENTO + objresponse.mensaje + ' ' + file.name
            }));
          } else {
            this._store.dispatch(new PushNotificationAction({
              severity: 'success',
              summary: SUCCESS_ADJUNTAR_DOCUMENTO + file.name
            }));
          }
        });
      });
    });
  }

  actualizarPlanilla() {
    this.planAgentes.forEach((p) => {
      delete p.usuario;
      delete p['_$visited'];
    });
    const agentes: PlanAgentesDTO = {
      pagente: this.planAgentes
    };
    const planilla:PlanillaDTO = {
      idePlanilla: null,
      nroPlanilla: null,
      fecGeneracion: null,
      codTipoPlanilla: null,
      codFuncGenera: null,
      codSedeOrigen: null,
      codDependenciaOrigen: null,
      codSedeDestino: null,
      codDependenciaDestino: null,
      codClaseEnvio: null,
      codModalidadEnvio: null,
      pagentes: agentes,
      ideEcm: null
    };

    this._planillaService.cargarPlanillas(planilla).subscribe(() => {
      this._store.dispatch(new CompleteTaskAction(this.getTaskToCompletePayload()));
      this.listarPlanAgentes();
    });

  }

  getTaskToCompletePayload() {
    return {
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea
    }
  }

  canUpdatePlanilla():boolean {
    const valid = this.planAgentes.length > 0 && this.planAgentes.every((e) => e.estado && e.estado !== '');
    return valid
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

  abort() {
    this._taskSandbox.abortTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      instanciaProceso: this.task.idInstanciaProceso
    });
  }

}