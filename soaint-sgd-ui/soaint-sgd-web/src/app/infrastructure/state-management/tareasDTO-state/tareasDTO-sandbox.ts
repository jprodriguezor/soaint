import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {ListForSelectionApiService} from '../../api/list-for-selection.api.service';
import * as actions from './tareasDTO-actions';
import {go} from '@ngrx/router-store';
import {tassign} from 'tassign';
import {TareaDTO} from '../../../domain/tareaDTO';
import {isArray} from 'rxjs/util/isArray';
import {Observable} from 'rxjs/Observable';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _listSelectionService: ListForSelectionApiService) {
  }

  loadData(payload: any) {
    const clonePayload = tassign(payload, {
      estados: [
        'RESERVADO',
        'ENPROGRESO',
        'LISTO'
      ]
    });
    return this._listSelectionService.post(environment.tasksForStatus_endpoint, clonePayload);
    // return Observable.of(this.getMockData());
  }

  startTask(payload: any) {
    let overPayload = payload;
    if (isArray(payload) && payload.length > 0) {
      const task = payload[0];
      overPayload = {
        'idProceso': task.idProceso,
        'idDespliegue': task.idDespliegue,
        'idTarea': task.idTarea
      }
    }
    return this._listSelectionService.post(environment.tasksStartProcess, overPayload);
  }

  completeTask(payload: any) {
    return this._listSelectionService.post(environment.tasksCompleteProcess, payload);
  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  initTaskDispatch(payload?): any {
    this._store.dispatch(go(['/radicar-comunicaciones', payload]));
  }

  navigateToWorkspace() {
    this._store.dispatch(go('workspace'));
  }

  startTaskDispatch(task?: TareaDTO) {

    if (task.estado === 'ENPROGRESO') {
      this.initTaskDispatch(task);

    } else if (task.estado === 'RESERVADO') {

      this._store.dispatch(new actions.StartTaskAction({
        'idProceso': task.idProceso,
        'idDespliegue': task.idDespliegue,
        'idTarea': task.idTarea,
        'usuario': 'krisv',
        'pass': 'krisv'
      }));

    }
  }

  loadDispatch(payload?) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

  getMockData() {
    return [
      {
        'idTarea': '430',
        'nombre': 'entrardata',
        'estado': 'Completed',
        'prioridad': '0',
        'skipable': 'false',
        'idResponsable': 'krisv',
        'fechaCreada': '2017-05-26T16:37:41.723-04:00',
        'expiration-time': '2017-05-27T17:00:00-04:00',
        'idInstanciaProceso': '2133',
        'idProceso': 'proceso.correspondencia-entrada',
        'idDespliegue': 'co.com.soaint.sgd.process:proceso-correspondencia-entrada:1.0.0-SNAPSHOT',
        'idParent': '-1'
      },
      {
        'idTarea': '2139',
        'nombre': 'Radicar COF Entrada',
        'estado': 'InProgress',
        'prioridad': '1',
        'skipable': 'true',
        'idResponsable': 'krisv',
        'idCreador': 'krisv',
        'fechaCreada': '2017-06-16T11:10:53.769-04:00',
        'tiempoActivacion': '2017-06-16T11:10:53.769-04:00',
        'idInstanciaProceso': '2133',
        'idProceso': 'proceso.correspondencia-entrada',
        'idDespliegue': 'co.com.soaint.sgd.process:proceso-correspondencia-entrada:1.0.0-SNAPSHOT',
        'idParent': '-1'
      },
      {
        'idTarea': '2158',
        'nombre': 'Radicar COF Entrada',
        'estado': 'Reserved',
        'prioridad': '1',
        'skipable': 'true',
        'idResponsable': 'krisv',
        'idCreador': 'krisv',
        'fechaCreada': '2017-06-16T12:42:07.488-04:00',
        'tiempoActivacion': '2017-06-16T12:42:07.488-04:00',
        'idInstanciaProceso': '2152',
        'idProceso': 'proceso.correspondencia-entrada',
        'idDespliegue': 'co.com.soaint.sgd.process:proceso-correspondencia-entrada:1.0.0-SNAPSHOT',
        'idParent': '-1'
      },
      {
        'idTarea': '2177',
        'nombre': 'Radicar COF Entrada',
        'estado': 'Reserved',
        'prioridad': '1',
        'skipable': 'true',
        'idResponsable': 'krisv',
        'idCreador': 'krisv',
        'fechaCreada': '2017-06-16T12:42:44.448-04:00',
        'tiempoActivacion': '2017-06-16T12:42:44.448-04:00',
        'idInstanciaProceso': '2171',
        'idProceso': 'proceso.correspondencia-entrada',
        'idDespliegue': 'co.com.soaint.sgd.process:proceso-correspondencia-entrada:1.0.0-SNAPSHOT',
        'idParent': '-1'
      },
      {
        'idTarea': '2196',
        'nombre': 'Radicar COF Entrada',
        'estado': 'Ready',
        'prioridad': '1',
        'skipable': 'true',
        'idCreador': 'krisv',
        'fechaCreada': '2017-06-16T14:39:39.628-04:00',
        'tiempoActivacion': '2017-06-16T14:39:39.628-04:00',
        'idInstanciaProceso': '2190',
        'idProceso': 'proceso.correspondencia-entrada',
        'idDespliegue': 'co.com.soaint.sgd.process:proceso-correspondencia-entrada:1.0.0-SNAPSHOT',
        'idParent': '-1'
      },
      {
        'idTarea': '2215',
        'nombre': 'Radicar COF Entrada',
        'estado': 'InProgress',
        'prioridad': '1',
        'skipable': 'true',
        'idResponsable': 'krisv',
        'idCreador': 'krisv',
        'fechaCreada': '2017-06-16T15:05:47.687-04:00',
        'tiempoActivacion': '2017-06-16T15:05:47.687-04:00',
        'idInstanciaProceso': '2209',
        'idProceso': 'proceso.correspondencia-entrada',
        'idDespliegue': 'co.com.soaint.sgd.process:proceso-correspondencia-entrada:1.0.0-SNAPSHOT',
        'idParent': '-1'
      },
      {
        'idTarea': '2234',
        'nombre': 'Radicar COF Entrada',
        'estado': 'InProgress',
        'prioridad': '1',
        'skipable': 'true',
        'idResponsable': 'krisv',
        'idCreador': 'krisv',
        'fechaCreada': '2017-06-16T15:50:46.753-04:00',
        'tiempoActivacion': '2017-06-16T15:50:46.753-04:00',
        'idInstanciaProceso': '2228',
        'idProceso': 'proceso.correspondencia-entrada',
        'idDespliegue': 'co.com.soaint.sgd.process:proceso-correspondencia-entrada:1.0.0-SNAPSHOT',
        'idParent': '-1'
      },
      {
        'idTarea': '2270',
        'nombre': 'Radicar COF Entrada',
        'estado': 'Reserved',
        'prioridad': '1',
        'skipable': 'true',
        'idResponsable': 'krisv',
        'idCreador': 'krisv',
        'fechaCreada': '2017-06-17T10:23:07.436-04:00',
        'tiempoActivacion': '2017-06-17T10:23:07.436-04:00',
        'idInstanciaProceso': '2264',
        'idProceso': 'proceso.correspondencia-entrada',
        'idDespliegue': 'co.com.soaint.sgd.process:proceso-correspondencia-entrada:1.0.0-SNAPSHOT',
        'idParent': '-1'
      }
    ]
  }

}

