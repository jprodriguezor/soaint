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


  planAgentes:PlanAgenDTO[] = [];

  data:PlanillaDTO | any = {};

  selectedComunications:PlanAgenDTO[] = [];

  start_date:Date = new Date();

  editarPlanillaDialogVisible = false;

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
              private _api:ApiBase) {


  }

  ngOnInit() {
    this.closedTask = afterTaskComplete.map(() => true).startWith(false);
    this.uploadUrl = environment.digitalizar_doc_upload_endpoint;

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      if (this.task && this.task.variables) {
        this.nroPlanilla = this.task.variables.numPlanilla;
      }
    });

    this.start_date.setHours(this.start_date.getHours() - 24);

    this.listarPlanAgentes();
  }

  getDatosRemitente(comunicacion):Observable<AgentDTO> {
    const radicacionEntradaDTV = new RadicacionEntradaDTV(comunicacion);
    return radicacionEntradaDTV.getDatosRemitente();
  }

  ngOnDestroy() {
    this.activeTaskUnsubscriber.unsubscribe();
  }

  getEstadoLabel(estado) {
    return this.popupEditar.estadoEntregaSuggestions.find((element) => element.codigo === estado);
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

  showEditarPlanillaDialog() {
    this.popupEditar.resetData();
    this.editarPlanillaDialogVisible = true;
  }

  hideEditarPlanillaDialog() {
    this.editarPlanillaDialogVisible = false;
  }

  createAgents():PlanAgenDTO[] {
    let agents:PlanAgenDTO[] = [];
    agents = this.selectedComunications.reduce((_listado, _current) => {
      _current.estado= this.popupEditar.form.get('estadoEntrega').value.codigo;
      _current.codNuevaSede= this.popupEditar.form.get('dependenciaDestino').value ? this.popupEditar.form.get('dependenciaDestino').value.codSede : null;
      _current.codNuevaDepen= this.popupEditar.form.get('dependenciaDestino').value ? this.popupEditar.form.get('dependenciaDestino').value.codigo : null;
      _current.observaciones= this.popupEditar.form.get('observaciones').value;
      _current.fecCarguePla= this.popupEditar.form.get('fechaEntrega').value;
      _listado.push(_current);
      return _listado;
    }, []);
    return agents;
  }

  editarPlanilla() {
    const agents: PlanAgenDTO[] = this.createAgents();
    const coms = [...this.planAgentes];
    agents.forEach((element) => {
      const index = coms.findIndex((el) => el.ideAgente === element.agente.ideAgente);
      if (index > -1) {
        coms[index] = element;
      }
    });
    this.planAgentes = [...coms];
    this.selectedComunications = [];
    this.hideEditarPlanillaDialog();
    this.refreshView();
  }

  actualizarPlanilla() {
    // limpiar agente object
    this.planAgentes.forEach((p) => {
      delete p.usuario;
      delete p['_$visited'];
      delete p.desNuevaDepen;
      delete p.desNuevaSede;
      delete p.agente;
      delete p.correspondencia;
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
