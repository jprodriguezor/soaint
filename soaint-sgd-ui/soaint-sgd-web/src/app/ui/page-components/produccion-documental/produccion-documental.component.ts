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

  aprobado = 0;
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
    return lista.match(/([a-z.]+):[0-9]+/g);
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
                this.gestionarProduccion.initProyeccionLista(activeTask.variables.listaProyector || '', 'proyector');
                this.gestionarProduccion.initProyeccionLista(activeTask.variables.listaRevisor || '', 'revisor');
                this.gestionarProduccion.initProyeccionLista(activeTask.variables.listaAprobador || '', 'aprobador');
                this.taskCurrentStatus = {
                  datosGenerales: {
                    tipoComunicacion: null,
                    listaVersionesDocumento: [],
                    listaAnexos: []
                  },
                  datosContacto: {
                    distribucion: null,
                    responderRemitente: false,
                    listaDestinatarios: [],
                    remitenteExterno: null,
                  },
                  gestionarProduccion: {
                    listaProyectores: this.gestionarProduccion.listaProyectores
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

    console.log("entro al guardar");
    console.log(tareaDTO);

    this._produccionDocumentalApi.guardarEstadoTarea(tareaDTO).subscribe(response => {
        console.log(response);
    });
  }

  getCurrentStatus(): StatusDTO {

    console.log('current');
    console.log(this.datosContacto);

    this.taskCurrentStatus.datosGenerales.tipoComunicacion = this.datosGenerales.form.get('tipoComunicacion').value;
    this.taskCurrentStatus.datosGenerales.listaVersionesDocumento = this.datosGenerales.listaVersionesDocumento;
    this.taskCurrentStatus.datosGenerales.listaAnexos = this.datosGenerales.listaAnexos;
    this.taskCurrentStatus.datosContacto.distribucion = this.datosContacto.form.get('distribucion').value;
    this.taskCurrentStatus.datosContacto.responderRemitente = this.datosContacto.form.get('responderRemitente').value;

    if (this.datosGenerales.form.get('tipoComunicacion').value) {

      if(this.datosGenerales.form.get('tipoComunicacion').value.codigo === 'SI'){
        this.taskCurrentStatus.datosContacto.listaDestinatarios = this.datosContacto.destinatarioInterno.listaDestinatarios;

      }else{
        this.taskCurrentStatus.datosContacto.remitenteExterno = this.datosContacto.remitenteExterno;
      }


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
    this.taskVariables = {
        aprobado: 0,
        requiereAjustes: 0,
        listaProyector: [],
        listaAprobador: [],
        listaRevisor: []
    };
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
    console.log('finalizar tarea de producion documental');

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
