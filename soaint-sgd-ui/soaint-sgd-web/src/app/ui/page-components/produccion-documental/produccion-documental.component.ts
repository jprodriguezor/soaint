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
          if (status) {
            this.taskCurrentStatus = status;
            this.datosGenerales.updateStatus(status);
            this.datosContacto.updateStatus(status);
            this.gestionarProduccion.updateStatus(status);
          } else {
            this.taskCurrentStatus = {
              aprobado:0,
              listaProyector:[this.task.variables.usuarioProyector.concat(":").concat(this.task.variables.codDependenciaProyector)],
              listaAprobador:[],
              listaRevisor:[],
              usuarioProyector:this.task.variables.usuarioProyector,
              usuarioRevisor:this.task.variables.usuarioRevisor,
              usuarioAprobador:this.task.variables.usuarioAprobador,
              requiereAjustes:this.task.variables.requiereAjustes || false,
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

  getCurrentStatus() : StatusDTO {
    this.taskCurrentStatus.datosGenerales.tipoComunicacion = this.datosGenerales.form.get('tipoComunicacion').value;
    this.taskCurrentStatus.datosGenerales.listaVersionesDocumento = this.datosGenerales.listaVersionesDocumento;
    this.taskCurrentStatus.datosGenerales.listaAnexos = this.datosGenerales.listaAnexos;
    this.taskCurrentStatus.datosContacto.distribucion = this.datosContacto.form.get('distribucion').value;
    this.taskCurrentStatus.datosContacto.responderRemitente = this.datosContacto.form.get('responderRemitente').value;
    this.taskCurrentStatus.datosContacto.listaDestinatarios = this.datosGenerales.form.get('tipoComunicacion').value.codigo === 'SI'?
      this.datosContacto.destinatarioInterno.listaDestinatarios :
      this.datosContacto.destinatarioExterno.listaDestinatarios;
    this.taskCurrentStatus.gestionarProduccion.listaProyectores = this.gestionarProduccion.listaProyectores;
    this.taskCurrentStatus.gestionarProduccion.listaProyectores.forEach(el => {
      console.log(el);
      if (el.rol.rol === 'proyector') {
        this.taskCurrentStatus.listaProyector.push(el.funcionario.loginName.concat(":").concat(el.dependencia.codigo));
      } else
      if (el.rol.rol === 'revisor') {
        this.taskCurrentStatus.listaRevisor.push(el.funcionario.loginName.concat(":").concat(el.dependencia.codigo));
      } else
      if (el.rol.rol === 'aprobador') {
        this.taskCurrentStatus.listaAprobador.push(el.funcionario.loginName.concat(":").concat(el.dependencia.codigo));
      }
    });

    return this.taskCurrentStatus;
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
