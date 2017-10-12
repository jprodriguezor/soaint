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
  TASK_DIGITALIZAR_DOCUMENTO, TASK_DOCUMENTOS_TRAMITES, TASK_GENERAR_PLANILLA_ENTRADA,
  TASK_RADICACION_ENTRADA
} from './task-properties';
import {StartProcessAction} from '../procesoDTO-state/procesoDTO-actions';
import {Subscription} from 'rxjs/Subscription';
import {createSelector} from 'reselect';
import {ROUTES_PATH} from '../../../app.route-names';
import {getActiveTask} from "./tareasDTO-selectors";

@Injectable()
export class Sandbox {

  routingStartState = false;

  authPayload: { usuario: string, pass: string } | {};
  authPayloadUnsubscriber: Subscription;

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
    this.authPayloadUnsubscriber = this._store.select(createSelector((s: State) => s.auth.profile, (profile) => {
      return profile ? {usuario: profile.username, pass: profile.password} : {};
    })).subscribe((value) => {
      this.authPayload = value;
    });
  }

  loadData(payload: any) {
    const clonePayload = tassign(payload, {
      estados: [
        'RESERVADO',
        'ENPROGRESO',
        'LISTO'
      ]
    });
    return this._api.post(environment.tasksForStatus_endpoint,
      Object.assign({}, clonePayload, this.authPayload));
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
    return this._api.post(environment.tasksCompleteProcess,
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
      case TASK_DIGITALIZAR_DOCUMENTO:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.digitalizarDocumento, task]));
        break;
      case TASK_DOCUMENTOS_TRAMITES:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.documentosTramite, task]));
        break;
      case TASK_GENERAR_PLANILLA_ENTRADA:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.cargarPlanillas, task]));
        break;
      default:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.workspace, task]));
    }
  }

  completeTaskDispatch(payload: any) {
    this._store.dispatch(new actions.CompleteTaskAction(payload));
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


}

