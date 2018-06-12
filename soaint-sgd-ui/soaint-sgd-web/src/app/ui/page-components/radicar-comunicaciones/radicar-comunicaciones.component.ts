import {AfterContentInit, AfterViewInit, ChangeDetectionStrategy, Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {ComunicacionOficialDTO} from 'app/domain/comunicacionOficialDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import * as moment from 'moment';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {sedeDestinatarioEntradaSelector, tipoDestinatarioEntradaSelector} from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors';
import {getArrayData as DependenciaGrupoSelector} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {ScheduleNextTaskAction} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';
import {TareaDTO} from '../../../domain/tareaDTO';
import {TaskForm} from '../../../shared/interfaces/task-form.interface';
import {LoadDatosRemitenteAction} from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-actions';
import {TaskTypes} from '../../../shared/type-cheking-clasess/class-types';
import {getMediosRecepcionVentanillaData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/medios-recepcion-selectors';
import {LoadNextTaskPayload} from '../../../shared/interfaces/start-process-payload,interface';
import {getDestinatarioPrincial} from '../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-destinatario-selectors';
import {RadicarSuccessAction} from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-actions';
import 'rxjs/add/operator/skipWhile';
import {ComunicacionOficialEntradaDTV} from '../../../shared/data-transformers/comunicacionOficialEntradaDTV';
import {Sandbox as ComunicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';
import {isNullOrUndefined} from "util";


declare const require: any;
const printStyles = require('app/ui/bussiness-components/ticket-radicado/ticket-radicado.component.css');

@Component({
  selector: 'app-radicar-comunicaciones',
  templateUrl: './radicar-comunicaciones.component.html',
  styleUrls: ['./radicar-comunicaciones.component.scss'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RadicarComunicacionesComponent implements OnInit, AfterContentInit, AfterViewInit, OnDestroy, TaskForm {

  type = TaskTypes.TASK_FORM;

  @ViewChild('datosGenerales') datosGenerales;

  @ViewChild('datosRemitente') datosRemitente;

  @ViewChild('datosDestinatario') datosDestinatario;

  @ViewChild('ticketRadicado') ticketRadicado;

  formStatusIcon = 'assignment';

  valueRemitente: any;

  valueDestinatario: any;

  valueGeneral: any;

  radicacion: ComunicacionOficialDTO;

  date: Date = new Date();

  barCodeVisible = false;

  editable = true;

  task: TareaDTO;

  printStyle: string = printStyles;

  tabIndex = 0;

  formsTabOrder: Array<any> = [];

  tipoDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  sedeDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;

  mediosRecepcionDefaultSelection$: Observable<ConstanteDTO>;
  tipoDestinatarioDefaultSelection$: Observable<ConstanteDTO>;

  // Unsubscribers
  activeTaskUnsubscriber: Subscription;
  sedeUnsubscriber: Subscription;
  validDatosGeneralesUnsubscriber: Subscription;
  reqDigitInmediataUnsubscriber: Subscription;


  constructor(private _sandbox: RadicarComunicacionesSandBox,
              private _coSandbox: ComunicacionOficialSandbox,
              private _store: Store<RootState>,
              private _taskSandBox: TaskSandBox) {
  }

  ngOnInit() {
    // Default Selection for Children Components bindings
    this.mediosRecepcionDefaultSelection$ = this._store.select(getMediosRecepcionVentanillaData);
    this.tipoDestinatarioDefaultSelection$ = this._store.select(getDestinatarioPrincial);

    // Datalist Load bindings
    this.tipoDestinatarioSuggestions$ = this._store.select(tipoDestinatarioEntradaSelector);
    this.sedeDestinatarioSuggestions$ = this._store.select(sedeDestinatarioEntradaSelector);
    this.dependenciaGrupoSuggestions$ = this._store.select(DependenciaGrupoSelector);
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      this.restore();
    });

  }

  ngAfterContentInit() {
    this.formsTabOrder.push(this.datosGenerales);
    this.formsTabOrder.push(this.datosRemitente);
    this.formsTabOrder.push(this.datosDestinatario);
  }

  ngAfterViewInit() {

    const sedeRemitente = this.datosRemitente.form.get('sedeAdministrativa');
    this.sedeUnsubscriber = Observable.combineLatest(
      sedeRemitente.statusChanges,
      sedeRemitente.valueChanges
    )
      .filter(([status, value]) => status === 'VALID' || status === 'DISABLED')
      .distinctUntilChanged()
      .subscribe(([status, value]) => {
        if (status === 'VALID') {
          this.datosDestinatario.deleteDestinatarioIqualRemitente(value);
          this._sandbox.dispatchSedeDestinatarioEntradaFilter(value);
        } else if (status === 'DISABLED') {
          this._sandbox.dispatchSedeDestinatarioEntradaFilter(null);
        }
      });

    this.validDatosGeneralesUnsubscriber = this.datosGenerales.form.statusChanges.filter(value => value === 'VALID').first()
      .subscribe(() => {
        this._store.dispatch(new LoadDatosRemitenteAction())
      });

    this.reqDigitInmediataUnsubscriber = this.datosGenerales.form.get('reqDigit').valueChanges
      .subscribe(value => {
        console.log(value);
        // Habilitando o desabilitando la tarea que se ejecutará secuencialmente a la actual
        if (value && value === 2) {
          const payload: LoadNextTaskPayload = {
            idProceso: this.task.idProceso,
            idInstanciaProceso: this.task.idInstanciaProceso,
            idDespliegue: this.task.idDespliegue
          };
          this._store.dispatch(new ScheduleNextTaskAction(payload));
        } else {
          this._store.dispatch(new ScheduleNextTaskAction(null));
        }

      });
  }

  radicarComunicacion() {


    this.valueRemitente = this.datosRemitente.form.value;
    this.valueDestinatario = this.datosDestinatario.form.value;
    this.valueGeneral = this.datosGenerales.form.value;

    const radicacionEntradaFormPayload: any = {
      destinatario: this.datosDestinatario.form.value,
      generales: this.datosGenerales.form.value,
      remitente: this.datosRemitente.form.value,
      descripcionAnexos: this.datosGenerales.descripcionAnexos,
      radicadosReferidos: this.datosGenerales.radicadosReferidos,
      agentesDestinatario: this.datosDestinatario.agentesDestinatario,
      task: this.task
    };

    if (this.datosRemitente.datosContactos) {
      radicacionEntradaFormPayload.datosContactos = this.datosRemitente.datosContactos.contacts;

      console.log(radicacionEntradaFormPayload.datosContactos);
    }

    const comunicacionOficialDTV = new ComunicacionOficialEntradaDTV(radicacionEntradaFormPayload, this._store);
    this.radicacion = comunicacionOficialDTV.getComunicacionOficial();

    console.log(this.radicacion);

    this._sandbox.radicar(this.radicacion).subscribe((response) => {
      this.barCodeVisible = true;
      this.radicacion = response;
      this.editable = false;
      this.datosGenerales.form.get('fechaRadicacion').setValue(moment(this.radicacion.correspondencia.fecRadicado).format('DD/MM/YYYY hh:mm'));
      this.datosGenerales.form.get('nroRadicado').setValue(this.radicacion.correspondencia.nroRadicado);

      const ticketRadicado = {
        anexos: this.datosGenerales.descripcionAnexos.length,
        folios: this.valueGeneral.numeroFolio,
        noRadicado: this.radicacion.correspondencia.nroRadicado,
        fecha: this.radicacion.correspondencia.fecRadicado,
        destinatarioSede: this.valueDestinatario.destinatarioPrincipal.sedeAdministrativa.nombre,
        destinatarioGrupo: this.valueDestinatario.destinatarioPrincipal.dependenciaGrupo.nombre
      };
      if (comunicacionOficialDTV.isRemitenteInterno()) {
        ticketRadicado['remitenteSede'] = this.valueRemitente.sedeAdministrativa.nombre;
        ticketRadicado['remitenteGrupo'] = this.valueRemitente.dependenciaGrupo.nombre;
      } else {
        ticketRadicado['remitente'] = this.valueRemitente.nombreApellidos;
      }

      this.ticketRadicado.setDataTicketRadicado(ticketRadicado);
      this.showTicketRadicado();
      this.disableEditionOnForms();

      this._store.dispatch(new RadicarSuccessAction({
        tipoComunicacion: this.valueGeneral.tipoComunicacion,
        numeroRadicado: response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null
      }));

      console.log(this.valueGeneral);
      let requiereDigitalizacion = !isNullOrUndefined(this.valueGeneral.reqDigit)? this.valueGeneral.reqDigit : 0 ;


      this._taskSandBox.completeTaskDispatch({
        idProceso: this.task.idProceso,
        idDespliegue: this.task.idDespliegue,
        idTarea: this.task.idTarea,
        parametros: {
          requiereDigitalizacion: requiereDigitalizacion,
          numeroRadicado: response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null,
        }
      });
    });
  }

  setTipoComunicacion(event) {
    if (this.editable) {
      this.datosRemitente.setTipoComunicacion(event);
    }
  }


  hideTicketRadicado() {
    this.barCodeVisible = false;
  }

  showTicketRadicado() {
    this.barCodeVisible = true;
  }

  disableEditionOnForms() {
    this.editable = false;
    this.datosDestinatario.form.disable();
    this.datosRemitente.form.disable();
    this.datosGenerales.form.disable();
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

  getTask(): TareaDTO {
    return this.task;
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
      destinatario: this.datosDestinatario.form.value,
      generales: this.datosGenerales.form.value,
      remitente: this.datosRemitente.form.value,
      descripcionAnexos: this.datosGenerales.descripcionAnexos,
      radicadosReferidos: this.datosGenerales.radicadosReferidos,
      agentesDestinatario: this.datosDestinatario.agentesDestinatario
    };

    if (this.datosRemitente.datosContactos) {
      payload.datosContactos = this.datosRemitente.datosContactos.contacts;
      // payload.contactInProgress = this.datosRemitente.datosContactos.form.value
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
      this._sandbox.quickRestore(this.task.idInstanciaProceso, this.task.idTarea).take(1).subscribe(response => {
        const results = response.payload;
        if (!results) {
          return;
        }

        // generales
        this.datosGenerales.form.patchValue(results.generales);
        this.datosGenerales.descripcionAnexos = results.descripcionAnexos;
        this.datosGenerales.radicadosReferidos = results.radicadosReferidos;

        // remitente
        this.datosRemitente.form.patchValue(results.remitente);

        // destinatario
        this.datosDestinatario.form.patchValue(results.destinatario);
        this.datosDestinatario.agentesDestinatario = results.agentesDestinatario;

        if (results.datosContactos) {
          const retry = setInterval(() => {
            if (typeof this.datosRemitente.datosContactos !== 'undefined') {
              this.datosRemitente.datosContactos.contacts = [...results.datosContactos];
              clearInterval(retry);
            }
          }, 400);
        }

        // if (results.contactInProgress) {
        //   const retry = setInterval(() => {
        //     if (typeof this.datosRemitente.datosContactos !== 'undefined') {
        //       this.datosRemitente.datosContactos.form.patchValue(results.contactInProgress);
        //       clearInterval(retry);
        //     }
        //   }, 400)
        // }

      });
    }
  }

  testEditRadicado() {
    this._coSandbox.loadData({});
  }

  ngOnDestroy() {
    this.activeTaskUnsubscriber.unsubscribe();
    this.validDatosGeneralesUnsubscriber.unsubscribe();
    this.reqDigitInmediataUnsubscriber.unsubscribe();
    this.sedeUnsubscriber.unsubscribe();
  }

}
