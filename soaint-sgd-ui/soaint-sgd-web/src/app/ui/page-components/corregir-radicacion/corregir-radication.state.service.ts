import {Injectable} from '@angular/core';
import { Component, OnInit, ViewChild } from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormControl} from '@angular/forms';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {Subscription} from 'rxjs/Subscription';
import {Observable} from 'rxjs/Observable';
import {TareaDTO} from '../../../domain/tareaDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Sandbox as AsiganacionDTOSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {RadicacionEntradaDTV} from '../../../shared/data-transformers/radicacionEntradaDTV';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {getArrayData as getFuncionarioArrayData, getAuthenticatedFuncionario, getSelectedDependencyGroupFuncionario} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';
import {ROUTES_PATH} from '../../../app.route-names';
import {go} from '@ngrx/router-store';
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Sandbox as CominicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';


@Injectable()
export class CorregirRadicacionStateService {

task: TareaDTO;
activeTaskUnsubscriber: Subscription;
readonly = false;
tabIndex = 0;

radicacion: ComunicacionOficialDTO;
radicacionEntradaDTV: any;
comunicacion: ComunicacionOficialDTO = {};

destinatario: any = null;
generales: any = null;
remitente: any = null;
descripcionAnexos: any;
radicadosReferidos: any;
agentesDestinatario: any;

  constructor(
      private _store: Store<State>,
      private _sandbox: RadicarComunicacionesSandBox,
      private _asiganacionSandbox: AsiganacionDTOSandbox,
      private _comunicacionOficialApi: CominicacionOficialSandbox,
      private _taskSandBox: TaskSandBox,
      private formBuilder: FormBuilder
    ) {

  }

  Init() {
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
        this.task = activeTask;
        this.restore();
    });
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

  abort() {
    this._taskSandBox.abortTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      instanciaProceso: this.task.idInstanciaProceso
    });
  }

  save(): Observable<any> {
    const payload: any = {
      destinatario: this.destinatario,
      generales: this.generales,
      remitente: this.remitente,
      descripcionAnexos: this.descripcionAnexos,
      radicadosReferidos: this.radicadosReferidos,
      agentesDestinatario: this.agentesDestinatario
    };

    if (this.remitente.datosContactos) {
      payload.datosContactos = this.remitente.datosContactos.contacts;
    }

    const tareaDto: any = {
      idTareaProceso: this.task.idTarea,
      idInstanciaProceso: this.task.idInstanciaProceso,
      payload: payload
    };

    return this._sandbox.quickSave(tareaDto);
  }

  restore() {
    if (this.task) {
      this._asiganacionSandbox.obtenerComunicacionPorNroRadicado(this.task.variables.numeroRadicado)
      .subscribe((result) => {
        this.comunicacion = result;
        this.radicacionEntradaDTV = new RadicacionEntradaDTV(this.comunicacion);
        this.destinatario = this.radicacionEntradaDTV.getDatosDestinatarios();
        this.remitente = this.radicacionEntradaDTV.getDatosRemitente();
        this.generales = this.radicacionEntradaDTV.getRadicacionEntradaFormData();
      });
    }
  }
  actualizarComunicacion() {
    this._comunicacionOficialApi.actualizarComunicacion(this.comunicacion);
    this._taskSandBox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: {
      }
    });
    this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
  }

}
