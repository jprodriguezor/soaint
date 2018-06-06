import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './tareasDTO-actions';
import {back, go} from '@ngrx/router-store';
import {tassign} from 'tassign';
import {TareaDTO} from '../../../domain/tareaDTO';
import {isArray} from 'rxjs/util/isArray';
import {ApiBase} from '../../api/api-base';
import {
  TASK_APROBAR_DOCUMENTO,
  TASK_CARGAR_PLANILLA_ENTRADA,
  TASK_DIGITALIZAR_DOCUMENTO, TASK_DOCUMENTOS_TRAMITES, TASK_GENERAR_PLANILLA_ENTRADA,
  TASK_GESTION_PRODUCCION_MULTIPLE, TASK_GESTIONAR_UNIDADES_DOCUMENTALES, TASK_PRODUCIR_DOCUMENTO,
  TASK_RADICACION_ENTRADA, TASK_REVISAR_DOCUMENTO, TASK_GESTIONAR_DEVOLUCIONES, TASK_CORREGIR_RADICACION,
  TASK_RADICACION_SALIDA, TASK_RADICACION_DOCUMENTO_SALIDA, TASK_ARCHIVAR_DOCUMENTO, TASK_CREAR_UNIDAD_DOCUMENTAL,
  TASK_APROBAR_DISPOSICION_FINAL, TASK_ADJUNTAR_DOCUMENTO, TASK_COMPLETAR_DATOS_DISTRIBUCION, TASK_VERIFICAR_TRANSFERENCIA_DOCUMENTAL,
  TASK_APROBAR_TRANSFERENCIA_DOCUMENTAL,
  TASK_CARGAR_PLANILLA_SALIDA
} from './task-properties';
import {StartProcessAction} from '../procesoDTO-state/procesoDTO-actions';
import {Subscription} from 'rxjs/Subscription';
import {createSelector} from 'reselect';
import {ROUTES_PATH} from '../../../app.route-names';
import {getSelectedDependencyGroupFuncionario} from '../funcionarioDTO-state/funcionarioDTO-selectors';
import {Observable} from 'rxjs/Observable';
import {getActiveTask} from "./tareasDTO-selectors";

@Injectable()
export class Sandbox {

  routingStartState = false;

  authPayload: { usuario: string, pass: string } | any;
  authPayloadUnsubscriber: Subscription;

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
    this.authPayloadUnsubscriber = this._store.select(createSelector((s: State) => s.auth.profile, (profile) => {
      return profile ? {usuario: profile.username, pass: profile.password} : {};
    })).subscribe((value) => {
      this.authPayload = value;
    });
  }

  loadData(payload: any, dependency?: any) {
    const clonePayload = tassign(payload, {
      estados: [
        'RESERVADO',
        'ENPROGRESO',
        'LISTO'
      ],
      parametros: {
        codDependencia: (dependency)? dependency.codigo: null
      }
    });
    return this._api.post(environment.tasksForStatus_endpoint, Object.assign({}, clonePayload, this.authPayload));
  }

  getTaskStats() {
    const payload = {
      parametros: {
        usuario: this.authPayload.usuario
      }
    };

    return this._api.post(environment.tasksStats_endpoint,
      Object.assign({}, payload, this.authPayload));
  }


  getTaskVariables(payload: any) {
    const overPayload = this.extractProcessVariablesPayload(payload);
    return this._api.post(environment.obtenerVariablesTarea,
      Object.assign({}, overPayload, this.authPayload));
  }

  isTaskRoutingStarted() {
    return this.routingStartState;
  }

  taskRoutingStart() {
    this.routingStartState = true;
  }

  taskRoutingEnd() {
    this.routingStartState = false;
  }

  startTask(payload: any) {
    const overPayload = this.extractInitTaskPayload(payload);
    return this._api.post(environment.tasksStartProcess,
      Object.assign({}, overPayload, this.authPayload));
  }

  reserveTask(payload: any) {
    const overPayload = this.extractInitTaskPayload(payload);
    return this._api.post(environment.tasksReserveProcess,
      Object.assign({}, overPayload, this.authPayload));
  }

  extractProcessVariablesPayload(payload) {
    let task = payload;
    if (isArray(payload) && payload.length > 0) {
      task = payload[0];
    }

    return {
      'idProceso': task.idProceso,
      'idDespliegue': task.idDespliegue,
      'instanciaProceso': task.idInstanciaProceso
    }
  }

  extractInitTaskPayload(payload) {
    let task = payload;
    if (isArray(payload) && payload.length > 0) {
      task = payload[0];
    }

    return {
      'idProceso': task.idProceso,
      'idDespliegue': task.idDespliegue,
      'idTarea': task.idTarea
    }
  }

  completeTask(payload: any) {
    console.log(payload);
    return this._api.post(environment.tasksCompleteProcess,
      Object.assign({}, payload, this.authPayload));
  }

  abortTask(payload: any) {
    return this._api.post(environment.tasksAbortProcess,
      Object.assign({}, payload, this.authPayload));
  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  initTaskDispatch(task: TareaDTO): any {
    switch (task.nombre) {
      case TASK_RADICACION_ENTRADA:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.radicarCofEntrada, task]));
        break;
      case TASK_RADICACION_SALIDA:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.radicarCofSalida, task]));
        break;
      case TASK_RADICACION_DOCUMENTO_SALIDA:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.radicarDocumentoSalida, task]));
        break;
      case TASK_DIGITALIZAR_DOCUMENTO:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.digitalizarDocumento, task]));
        break;
      case TASK_ADJUNTAR_DOCUMENTO:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.adjuntarDocumento, task]));
        break;
      case TASK_GESTIONAR_DEVOLUCIONES:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.gestionarDevoluciones, task]));
        break;
      case TASK_CORREGIR_RADICACION:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.corregirRadicacion, task]));
        break;
      case TASK_APROBAR_DISPOSICION_FINAL:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.disposicionFinal, task]));
        break;
      case TASK_DOCUMENTOS_TRAMITES:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.documentosTramite, task]));
        break;
      case TASK_CARGAR_PLANILLA_ENTRADA:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.cargarPlanillas, task]));
        break;
      case TASK_GESTION_PRODUCCION_MULTIPLE:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.produccionDocumentalMultiple, task]));
        break;
      case TASK_GESTIONAR_UNIDADES_DOCUMENTALES:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.gestionUnidadDocumental, task]));
        break;
      case TASK_PRODUCIR_DOCUMENTO:
          this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.produccionDocumental}/1`, task]));
          break;
      case TASK_REVISAR_DOCUMENTO:
          this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.produccionDocumental}/2`, task]));
          break;
      case TASK_APROBAR_DOCUMENTO:
          this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.produccionDocumental}/3`, task]));
          break;
      case TASK_ARCHIVAR_DOCUMENTO :
        this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.archivarDocumento}`, task]));
        break;

      case TASK_CREAR_UNIDAD_DOCUMENTAL :
        this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.crearUnidadDocumental}`, task]));
        break;

      case TASK_COMPLETAR_DATOS_DISTRIBUCION:
        this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.completarDatosDistribucion}`, task]));
        break;

       case TASK_APROBAR_TRANSFERENCIA_DOCUMENTAL:
        this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.transferenciasDocumentales}/1`, task]));
        break;

        case TASK_VERIFICAR_TRANSFERENCIA_DOCUMENTAL:
        this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.transferenciasDocumentales}/2`, task]));
        break;

        case TASK_CARGAR_PLANILLA_SALIDA:
        this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.cargarPlanillaSalida}`, task]));
        break;


      default:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.workspace, task]));
    }
  }

  completeTaskDispatch(payload: any) {
    this._store.dispatch(new actions.CompleteTaskAction(payload));
  }

   completeBackTaskDispatch(payload: any) {
    this._store.dispatch(new actions.CompleteBackTaskAction(payload));
  }

  abortTaskDispatch(payload: any) {
    this._store.dispatch(new actions.AbortTaskAction(payload));
  }

  dispatchNextTask(payload) {
    this._store.dispatch(new StartProcessAction(payload))
  }

  navigateToWorkspace() {
    this._store.dispatch(back());
  }

  startTaskDispatch(task?: TareaDTO) {
    if (task.estado === 'ENPROGRESO') {
      this._store.dispatch(new actions.StartInProgressTaskAction(task));
    } else if (task.estado === 'RESERVADO') {
      this._store.dispatch(new actions.StartTaskAction(task));
    }
  }

  reserveTaskDispatch(task?: TareaDTO) {
    this._store.dispatch(new actions.ReserveTaskAction(task));
  }

  loadDispatch(payload?) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

  getTareaPersisted(idProceso,idTarea){

    return this._api.list(environment.taskStatus_endpoint+'/'+idProceso+'/'+idTarea);

  }

  guardarEstadoTarea(task: TareaDTO){    
      return this._api.post(environment.taskStatus_endpoint, task);    
  }


}

