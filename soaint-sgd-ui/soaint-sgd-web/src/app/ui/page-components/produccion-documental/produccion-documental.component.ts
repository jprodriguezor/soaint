import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {createSelector} from 'reselect';
import {getActiveTask} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {PdMessageService} from './providers/PdMessageService';
import {Subscription} from 'rxjs/Subscription';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {TaskForm} from 'app/shared/interfaces/task-form.interface';
import {Observable} from 'rxjs/Observable';
import {TareaDTO} from 'app/domain/tareaDTO';
import {ProduccionDocumentalApiService} from "../../../infrastructure/api/produccion-documental.api";
import {StatusDTO} from "./models/StatusDTO";

@Component({
  selector: 'produccion-documental',
  templateUrl: './produccion-documental.component.html',
  styleUrls: ['produccion-documental.component.css'],
})

export class ProduccionDocumentalComponent implements OnInit, OnDestroy, TaskForm {

  task: TareaDTO;
  taskCurrentStatus: StatusDTO;
  idEstadoTarea = '0000';

  @ViewChild('datosGenerales') datosGenerales;
  @ViewChild('datosContacto') datosContacto;
  @ViewChild('gestionarProduccion') gestionarProduccion;

  tipoComunicacionSelected: ConstanteDTO;
  subscription: Subscription;

  revisar = false;
  aprobar = false;
  tabIndex = 0;

  authPayload: { usuario: string, pass: string } | {};
  authPayloadUnsubscriber: Subscription;

  constructor(private _store: Store<RootState>,
              private _taskSandBox: TaskSandBox,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private pdMessageService: PdMessageService) {
    this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => {
      this.tipoComunicacionSelected = tipoComunicacion;
    });
    this.authPayloadUnsubscriber = this._store.select(createSelector((s: RootState) => s.auth.profile, (profile) => {
      return profile ? {usuario: profile.username, pass: profile.password} : {};
    })).subscribe((value) => {
      this.authPayload = value;
    });
  }

  ngOnInit(): void {
    this._store.select(getActiveTask).take(1).subscribe(activeTask => {
      this.task = activeTask;
      this._produccionDocumentalApi.obtenerEstadoTarea({
        idInstanciaProceso: this.task.idInstanciaProceso,
        idTareaProceso: this.idEstadoTarea
      }).subscribe(
        status => {
            this.datosGenerales.updateStatus(status);
            this.datosContacto.updateStatus(status);
        },
        error => console.log("No se pudo cargar la tarea")
      );
    });
  }

  guardarEstadoTarea(currentStatus: StatusDTO) {
    const tareaDTO = {
      idTareaProceso: this.idEstadoTarea,
      idInstanciaProceso: this.task.idInstanciaProceso,
      payload: currentStatus || this.getCurrentStatus(),
    };

    this._produccionDocumentalApi.guardarEstadoTarea(tareaDTO).subscribe(response => {
        console.log(response);
    });
  }

  getCurrentStatus() : StatusDTO {
    return {
      datosGenerales: {
        tipoComunicacion: this.datosGenerales.form.get('tipoComunicacion').value,
        listaVersionesDocumento: this.datosGenerales.listaVersionesDocumento,
        listaAnexos: this.datosGenerales.listaAnexos
      },
      datosContacto: {
        distribucion: this.datosContacto.form.get('distribucion').value,
        responderRemitente: this.datosContacto.form.get('responderRemitente').value,
        listaDestinatarios: this.datosGenerales.form.get('tipoComunicacion').value.codigo === 'SI'?
          this.datosContacto.destinatarioInterno.listaDestinatarios :
          this.datosContacto.destinatarioExterno.listaDestinatarios
      },
      gestionarProduccion: {
        listaProyectores: this.gestionarProduccion.listaProyectores
      }
    };
  }

  completarTarea() {
    const currentStatus = this.getCurrentStatus();
    this.datosGenerales.form.disable();
    this.datosContacto.form.disable();
    this.gestionarProduccion.form.disable();
    this.guardarEstadoTarea(currentStatus);

    this._taskSandBox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: currentStatus
    });
  }

  ngOnDestroy(): void {
    this.authPayloadUnsubscriber.unsubscribe();
  }

  save(): Observable<any> {
    return Observable.of(true).delay(5000);
  }
}
