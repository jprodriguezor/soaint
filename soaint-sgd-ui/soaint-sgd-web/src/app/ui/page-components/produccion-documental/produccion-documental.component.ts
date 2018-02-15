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
import {getAuthenticatedFuncionario} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {ProyectorDTO} from '../../../domain/ProyectorDTO';

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
  funcionarioLog: FuncionarioDTO;
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
      this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
          this.funcionarioLog = funcionario;
      });
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
                  status : 0,
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
                    listaProyectores: this.gestionarProduccion.listaProyectores
                  }
                };
          }
        }
      );
    });
  }

  guardarEstadoTarea(currentStatus?: StatusDTO) {
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

  construirListas() {
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
  }

  completarTarea() {
    const currentStatus = this.getCurrentStatus();
    this.datosGenerales.form.disable();
    this.datosContacto.form.disable();
    this.gestionarProduccion.form.disable();
    if (this.checkAprobar()) {
        currentStatus.status = 1;
    } else if (this.checkRequiereAjustes()) {
        currentStatus.status = 2;
    } else if (this.checkAprobador()) {
        currentStatus.status = 3;
    }
    this.guardarEstadoTarea(currentStatus);
    this.taskVariables = {};
    switch (currentStatus.status) {
        case 0 : {
            this.taskVariables.aprobado = 0;
            this.taskVariables.listaProyector = [];
            this.taskVariables.listaAprobador = [];
            this.taskVariables.listaAprobador = [];
            this.construirListas();
            break;
        }
        case 1 : {
            this.taskVariables.aprobado = 1;
            break;
        }
        case 2 : {
            this.taskVariables.requiereAjustes = 1;
            break;
        }
        case 3 : {
            this.taskVariables.aprobado = 0;
            break;
        }
        default : break;
    }
    this.terminarTarea();
  }

  aprobarDocumento() {
      this.taskVariables = { aprobado: 1 };
      this.terminarTarea();
  }


  terminarTarea() {

      this._taskSandBox.completeTaskDispatch({
          idProceso: this.task.idProceso,
          idDespliegue: this.task.idDespliegue,
          idTarea: this.task.idTarea,
          parametros: this.taskVariables
      });
  }

    checkAprobar() {
        return 0 < this.datosGenerales.getListaVersiones().length
            && 1 === this.gestionarProduccion.listaProyectores.length
            && this.funcionarioLog.loginName === this.gestionarProduccion.getListaProyectores()[0].funcionario.loginName;
    }

    checkRequiereAjustes() {
        return 0 < this.datosGenerales.getListaVersiones().length
            && this.gestionarProduccion.getListaProyectores().filter((el: ProyectorDTO) => 'revisor' === el.rol.rol).length > 0;
    }

    checkAprobador() {
        return 0 < this.datosGenerales.getListaVersiones().length
            && this.gestionarProduccion.getListaProyectores().filter((el: ProyectorDTO) => 'aprobador' === el.rol.rol).length > 0;
    }

  ngOnDestroy(): void {
    this.authPayloadUnsubscriber.unsubscribe();
  }

  save(): Observable<any> {
    return Observable.of(true).delay(5000);
  }
}
