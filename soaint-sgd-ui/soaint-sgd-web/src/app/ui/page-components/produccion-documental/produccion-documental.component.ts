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
import {ActivatedRoute} from '@angular/router';
import {PushNotificationAction} from '../../../infrastructure/state-management/notifications-state/notifications-actions';

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

  status = 1;
  tabIndex = 0;

  authPayload: { usuario: string, pass: string } | {};
  authPayloadUnsubscriber: Subscription;

  constructor(private _store: Store<RootState>,
              private _taskSandBox: TaskSandBox,
              private route: ActivatedRoute,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private pdMessageService: PdMessageService) {
      this.route.params.subscribe( params => {
          this.status = parseInt(params.status, 10) || 0;
      } );
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
                    startIndex: this.gestionarProduccion.listaProyectores.length,
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
    this.taskCurrentStatus.gestionarProduccion.startIndex = this.gestionarProduccion.startIndex;

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
      console.log(`Listas construidas`);
  }

  completarTarea() {
    const currentStatus = this.getCurrentStatus();
    let completar = true;
    switch (this.status) {
        case 1 : {
            console.log(`Status 1`);
            if (!this.hasAprobador()) {
                console.log(`No hay aprobador`);
                completar = false;
                this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Debe agregar al menos un aprobador'}));
                break;
            }
            this.taskVariables = {aprobado : 0, listaProyector : [],
                listaRevisor : [], listaAprobador : [] };
            this.construirListas();
            break;
        }
        case 2 : {
            console.log(`Status 2`);
            this.taskVariables = { requiereAjustes: this.checkRequiereAjustes() ? 1 : 0 };
            if (this.taskVariables.requiereAjustes === 1 && !this.hasAprobador()) {
                console.log(`Requiere ajustes pero no hay aprobador`);
                completar = false;
                this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Debe agregar al menos un aprobador'}));
            }
            break;
        }
        case 3 : {
            console.log(`Status 3`);
            this.taskVariables = { aprobado: 1 };
            break;
        }
        default : {
            this.taskVariables = {};
            break;
        }
    }
    console.log(this.taskVariables);
    if (completar) {
        // this.datosGenerales.form.disable();
        console.log(`Terminar la tarea 1`);
        // this.datosContacto.form.disable();
        console.log(`Terminar la tarea 2`);
        // this.gestionarProduccion.form.disable();
        console.log(`Terminar la tarea 3`);
        currentStatus.gestionarProduccion.startIndex = currentStatus.gestionarProduccion.listaProyectores.length;
        console.log(`Terminar la tarea 4`);
        this.guardarEstadoTarea(currentStatus);
        console.log(`Terminar la tarea 5`);
        this.terminarTarea();
    }
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

    puedeAprobar() {
        return 0 < this.datosGenerales.getListaVersiones().length
            && ((1 === this.gestionarProduccion.listaProyectores.length
            && this.funcionarioLog.loginName === this.gestionarProduccion.getListaProyectores()[0].funcionario.loginName)
            || (this.esFuncionarioAprobador(this.funcionarioLog)))
    }

    checkRequiereAjustes() {
        return 0 < this.datosGenerales.getListaVersiones().length
            && this.gestionarProduccion.getListaProyectores().filter((el: ProyectorDTO) => 'revisor' === el.rol.rol).length > 0;
    }

    hasAprobador() {
        return 0 < this.datosGenerales.getListaVersiones().length
            && this.gestionarProduccion.getListaProyectores().filter((el: ProyectorDTO) => 'aprobador' === el.rol.rol).length > 0;
    }

    esFuncionarioAprobador(funcionario: FuncionarioDTO) {
        return this.gestionarProduccion.getListaProyectores().filter(
            (el: ProyectorDTO) => 'aprobador' === el.rol.rol && el.funcionario.loginName === funcionario.loginName
        ).length > 0;
    }

  ngOnDestroy(): void {
    this.authPayloadUnsubscriber.unsubscribe();
  }

  save(): Observable<any> {
    return Observable.of(true).delay(5000);
  }
}
