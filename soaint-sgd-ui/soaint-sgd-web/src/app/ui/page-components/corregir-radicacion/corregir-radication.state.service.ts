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
import { ProduccionDocumentalApiService } from '../../../infrastructure/api/produccion-documental.api';
import { VersionDocumentoDTO } from '../produccion-documental/models/DocumentoDTO';
import { DocumentoEcmDTO } from '../../../domain/documentoEcmDTO';
import { PushNotificationAction } from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import { DocumentUploaded } from '../produccion-documental/events/DocumentUploaded';
import { MessageService } from 'primeng/components/common/messageservice';
import { MessagingService } from '../../../infrastructure/__api.include';


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
      private formBuilder: FormBuilder,
      private _produccionDocumentalApi: ProduccionDocumentalApiService,
      private messagingService: MessagingService
    ) {

  }

  Init() {
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
        this.task = activeTask;
        this.restore();
    });
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

  HasDocuments() {
    if (this.comunicacion && this.comunicacion.ppdDocumentoList) {
      return (this.comunicacion.ppdDocumentoList[0].ideEcm) ? true : false;
    }
    return false;
  }

  uploadVersionDocumento(doc: VersionDocumentoDTO) {
    const formData = new FormData();
    formData.append('documento', doc.file, doc.nombre);
    if (doc.id) {
        formData.append('idDocumento', doc.id);
    }
    formData.append('selector', 'PD');
    formData.append('nombreDocumento', doc.nombre);
    formData.append('tipoDocumento', doc.tipo);
    formData.append('sede', this.task.variables.nombreSede);
    formData.append('dependencia', this.task.variables.nombreDependencia);
    formData.append('nroRadicado', this.task.variables && this.task.variables.numeroRadicado || null);
    this._produccionDocumentalApi.subirVersionDocumento(formData).subscribe(
    resp => {
      if ('0000' === resp.codMensaje) {
        this._store.dispatch(new PushNotificationAction({severity: 'success', summary: resp.mensaje}))
        return resp.documentoDTOList[0];
      } else {
        this._store.dispatch(new PushNotificationAction({severity: 'error', summary: resp.mensaje}));
        return doc
      }
    },
    error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error}))
  );
}

}
