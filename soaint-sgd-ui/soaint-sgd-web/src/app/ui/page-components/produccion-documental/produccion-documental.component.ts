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
import {ProduccionDocumentalApiService} from '../../../infrastructure/api/produccion-documental.api';
import {StatusDTO, VariablesTareaDTO} from './models/StatusDTO';

@Component({
  selector: 'produccion-documental',
  templateUrl: './produccion-documental.component.html',
  styleUrls: ['produccion-documental.component.css'],
})

export class ProduccionDocumentalComponent implements OnInit, OnDestroy, TaskForm {

  task: TareaDTO;
  taskCurrentStatus: StatusDTO;
  taskVariables: VariablesTareaDTO;
  idEstadoTarea = '0000';

  @ViewChild('datosGenerales') datosGenerales;
  @ViewChild('datosContacto') datosContacto;
  @ViewChild('gestionarProduccion') gestionarProduccion;

  tipoComunicacionSelected: ConstanteDTO;
  subscription: Subscription;

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

  parseIncomingListaProyector(lista: string) {
    const proyectores = lista.match(/\[(.*)\]/)[1];
    return proyectores.split(',');
  }


  ngOnInit(): void {
    this._store.select(getActiveTask).take(1).subscribe(activeTask => {
      this.task = activeTask;
      this.taskVariables = {
        aprobado: activeTask.variables.aprobado || 0,
        listaProyector: activeTask.variables.listaProyector && this.parseIncomingListaProyector(activeTask.variables.listaProyector) || [],
        listaAprobador: activeTask.variables.listaAprobador && this.parseIncomingListaProyector(activeTask.variables.listaAprobador) || [],
        listaRevisor: activeTask.variables.listaRevisor && this.parseIncomingListaProyector(activeTask.variables.listaRevisor) || []
      };
      this._produccionDocumentalApi.obtenerEstadoTarea({
        idInstanciaProceso: this.task.idInstanciaProceso,
        idTareaProceso: this.idEstadoTarea
      }).subscribe(
        status => {
          if (status) {
            this.taskCurrentStatus = status;
            this.datosGenerales.updateStatus(status);
            this.datosContacto.updateStatus(status);
            this.gestionarProduccion.updateStatus(status);
          } else {
            this.taskCurrentStatus = {
              datosGenerales: {
                tipoComunicacion: null,
                listaVersionesDocumento: [],
                listaAnexos: []
              },
              datosContacto: {
                distribucion: null,
                responderRemitente: false,
                listaDestinatarios: []
              },
              gestionarProduccion: {
                listaProyectores: []
              }
            };
          }
        }
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

  getCurrentStatus(): StatusDTO {
    this.taskCurrentStatus.datosGenerales.tipoComunicacion = this.datosGenerales.form.get('tipoComunicacion').value;
    this.taskCurrentStatus.datosGenerales.listaVersionesDocumento = this.datosGenerales.listaVersionesDocumento;
    this.taskCurrentStatus.datosGenerales.listaAnexos = this.datosGenerales.listaAnexos;
    this.taskCurrentStatus.datosContacto.distribucion = this.datosContacto.form.get('distribucion').value;
    this.taskCurrentStatus.datosContacto.responderRemitente = this.datosContacto.form.get('responderRemitente').value;
    if (this.datosGenerales.form.get('tipoComunicacion').value) {
      this.taskCurrentStatus.datosContacto.listaDestinatarios =  this.datosGenerales.form.get('tipoComunicacion').value.codigo === 'SI' ?
        this.datosContacto.destinatarioInterno.listaDestinatarios :
        this.datosContacto.destinatarioExterno.listaDestinatarios;
    } else {
      this.taskCurrentStatus.datosContacto.listaDestinatarios = [];
    }
    this.taskCurrentStatus.gestionarProduccion.listaProyectores = this.gestionarProduccion.listaProyectores;

    return this.taskCurrentStatus;
  }

  completarTarea() {
    const currentStatus = this.getCurrentStatus();
    this.datosGenerales.form.disable();
    this.datosContacto.form.disable();
    this.gestionarProduccion.form.disable();
    this.guardarEstadoTarea(currentStatus);

    this.taskCurrentStatus.gestionarProduccion.listaProyectores.forEach(el => {
      if (el.rol.rol === 'proyector') {
        this.taskVariables.listaProyector.push(el.funcionario.loginName.concat(':').concat(el.dependencia.codigo));
      } else
      if (el.rol.rol === 'revisor') {
        this.taskVariables.listaRevisor.push(el.funcionario.loginName.concat(':').concat(el.dependencia.codigo));
      } else
      if (el.rol.rol === 'aprobador') {
        this.taskVariables.listaAprobador.push(el.funcionario.loginName.concat(':').concat(el.dependencia.codigo));
      }
    });

    this._taskSandBox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: Object.assign(this.taskVariables, {datosPD: currentStatus})
    });
  }

  ngOnDestroy(): void {
    this.authPayloadUnsubscriber.unsubscribe();
  }

  save(): Observable<any> {
    return Observable.of(true).delay(5000);
  }
}
