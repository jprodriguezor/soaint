import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
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
import {DestinatarioDTO} from '../../../domain/destinatarioDTO';
import {AgentDTO} from '../../../domain/agentDTO';
import {MessagingService} from '../../../shared/providers/MessagingService';
import {DocumentDownloaded} from './events/DocumentDownloaded';
import {environment} from '../../../../environments/environment';

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
  status = 1;


  @ViewChild('datosGenerales') datosGenerales;
  @ViewChild('datosContacto') datosContacto;
  @ViewChild('gestionarProduccion') gestionarProduccion;

  tipoComunicacionSelected: ConstanteDTO;
  funcionarioLog: FuncionarioDTO;
  subscription: Subscription;

  documentUrl: string;
  pdfViewer = false;

  tabIndex = 0;

  authPayload: { usuario: string, pass: string } | {};
  authPayloadUnsubscriber: Subscription;
  documentSubscription: Subscription;

  constructor(private _store: Store<RootState>,
              private _taskSandBox: TaskSandBox,
              private route: ActivatedRoute,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private _changeDetectorRef: ChangeDetectorRef,
              private messagingService: MessagingService,
              private pdMessageService: PdMessageService) {
      this.route.params.subscribe( params => {
          this.status = parseInt(params.status, 10) || 0;
      } );
      this.documentSubscription = this.messagingService.of(DocumentDownloaded).subscribe(message => {
          this.tabIndex = 3;
          this.refreshView();
      });
    this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => {
      this.tipoComunicacionSelected = tipoComunicacion;
    });
    this.authPayloadUnsubscriber = this._store.select(createSelector((s: RootState) => s.auth.profile, (profile) => {
      return profile ? {usuario: profile.username, pass: profile.password} : {};
    })).subscribe((value) => {
      this.authPayload = value;
    });
  }

  private initCurrentStatus() {
      this.taskCurrentStatus = {
          datosGenerales: {
              tipoComunicacion: null,
              listaVersionesDocumento: [],
              listaAnexos: []
          },
          datosContacto: {
              distribucion: null,
              responderRemitente: false,
              hasDestinatarioPrincipal: false,
              listaDestinatariosInternos: [],
              listaDestinatariosExternos: []
          },
          gestionarProduccion: {
              startIndex: this.gestionarProduccion.listaProyectores.length,
              listaProyectores: this.gestionarProduccion.listaProyectores
          }
      };
  }

  ngOnInit(): void {
        this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
          this.funcionarioLog = funcionario;
        });
        this._store.select(getActiveTask).take(1).subscribe(activeTask => {
      this.task = activeTask;

        if (activeTask.variables && activeTask.variables.numeroRadicado) {
            console.log('Buscando documento asociado')
            this._produccionDocumentalApi.obtenerDatosDocXnroRadicado({id: activeTask.variables.numeroRadicado}).subscribe(
                res => {
                    if (res.ideEcm) {
                        console.log('Encontrado documento asociado')
                        this.documentUrl = `${environment.pd_gestion_documental.descargarDocumentoPorId}?identificadorDoc=${res.ideEcm}`;
                        this.pdfViewer = true;
                        this.refreshView();
                    } else {
                        this._store.dispatch(new PushNotificationAction({severity: 'error', summary: `No se encontro ningún documento asociado al radicado: ${activeTask.variables.numeroRadicado}`}));
                    }
                },
                error => this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: error}))
            );
        }

          this._produccionDocumentalApi.obtenerEstadoTarea({
            idInstanciaProceso: this.task.idInstanciaProceso,
            idTareaProceso: this.idEstadoTarea
          }).subscribe(
              (status: StatusDTO) => {
              if (status) {
                    this.taskCurrentStatus = status;
                    this.datosGenerales.updateStatus(status);
                    this.datosContacto.updateStatus(status);
                   this.gestionarProduccion.updateStatus(status);
                    console.log(status);
              } else {
                    this.gestionarProduccion.initProyeccionLista(activeTask.variables.listaProyector || '', 'proyector');
                    this.gestionarProduccion.initProyeccionLista(activeTask.variables.listaRevisor || '', 'revisor');
                    this.gestionarProduccion.initProyeccionLista(activeTask.variables.listaAprobador || '', 'aprobador');
                    this.initCurrentStatus();
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
    this._produccionDocumentalApi.guardarEstadoTarea(tareaDTO).subscribe(response => { console.log(response); });
  }

  updateEstadoTarea() {
      const currentStatus = this.getCurrentStatus();
      currentStatus.gestionarProduccion.startIndex = currentStatus.gestionarProduccion.listaProyectores.length;
      this.guardarEstadoTarea(currentStatus);
  }

  getCurrentStatus(): StatusDTO {
      this.taskCurrentStatus.datosGenerales.tipoComunicacion = this.datosGenerales.form.get('tipoComunicacion').value;
      this.taskCurrentStatus.datosGenerales.listaVersionesDocumento = this.datosGenerales.listaVersionesDocumento;
      this.taskCurrentStatus.datosGenerales.listaAnexos = this.datosGenerales.listaAnexos;
      this.taskCurrentStatus.datosContacto.distribucion = this.datosContacto.form.get('distribucion').value;
      this.taskCurrentStatus.datosContacto.responderRemitente = this.datosContacto.form.get('responderRemitente').value;

      this.taskCurrentStatus.datosContacto.hasDestinatarioPrincipal = this.datosContacto.hasDestinatarioPrincipal;
      this.taskCurrentStatus.datosContacto.listaDestinatariosInternos = this.datosContacto.listaDestinatariosInternos;
      this.taskCurrentStatus.datosContacto.listaDestinatariosExternos = this.datosContacto.listaDestinatariosExternos;

      this.taskCurrentStatus.gestionarProduccion.listaProyectores = this.gestionarProduccion.listaProyectores;
      this.taskCurrentStatus.gestionarProduccion.startIndex = this.gestionarProduccion.startIndex;
      return this.taskCurrentStatus;
  }

  construirListas() {
      this.gestionarProduccion.getListaProyectores().forEach(el => {
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

    continuarProceso() {
        if (!this.hasAprobador()) {
            console.log(`No hay aprobador`);
            this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Debe especificar al menos un aprobador'}));
            return false;
        }
        this.taskVariables = {aprobado : 0, listaProyector : [], listaRevisor : [], listaAprobador : [] };
        this.construirListas();
        this.updateEstadoTarea();
        this.terminarTarea();
        return true;
    }

    devolverDocumento() {
        this.taskVariables = {};
        if (this.status === 2) {
            this.taskVariables = { requiereAjustes: 1 };
        } else if (this.status === 3) {
            this.taskVariables = { aprobado: 0 };
        }
        this.updateEstadoTarea();
        this.terminarTarea();
    }

  aprobarDocumento() {
      switch (this.status) {
          case 1 : {
              this.taskVariables = {aprobado : 1, listaProyector : [], listaRevisor : [], listaAprobador : [] };
              this.construirListas();
              break;
          }
          case 2 : {
              this.taskVariables = { requiereAjustes: 0 };
              break;
          }
          case 3 : {
              this.taskVariables = { aprobado: 1 };
              break;
          }
          default : {
              this.taskVariables = {};
              break;
          }
      }
      this.updateEstadoTarea();
      this.terminarTarea();
  }

    cancelarTarea() {
        this._taskSandBox.abortTaskDispatch({
            idProceso: this.task.idProceso,
            idDespliegue: this.task.idDespliegue,
            instanciaProceso: this.task.idInstanciaProceso
        });
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
        const valid = this.status > 1
            || (1 === this.status && 1 === this.gestionarProduccion.listaProyectores.length);
        return valid && this.isValid();
    }

    hasAprobador() {
        return this.gestionarProduccion.getListaProyectores().filter((el: ProyectorDTO) => 'aprobador' === el.rol.rol).length > 0;
    }



    isValid(): boolean {
       let valid = true;
       valid = valid && this.datosGenerales.isValid();

       return valid;
    }

    ngOnDestroy(): void {
        this.authPayloadUnsubscriber.unsubscribe();
        this.documentSubscription.unsubscribe();
    }

    save(): Observable<any> {
        return Observable.of(true).delay(5000);
    }

    refreshView() {
        this._changeDetectorRef.detectChanges();
    }
}
