import {AfterContentInit, AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ComunicacionOficialDTO} from 'app/domain/comunicacionOficialDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {
  sedeDestinatarioEntradaSelector,
  tipoDestinatarioEntradaSelector
} from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors';
import {getArrayData as DependenciaGrupoSelector} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {TareaDTO} from '../../../domain/tareaDTO';
import {TaskForm} from '../../../shared/interfaces/task-form.interface';
import {TaskTypes} from '../../../shared/type-cheking-clasess/class-types';
import {getMediosRecepcionVentanillaData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/medios-recepcion-selectors';
import {getDestinatarioPrincial} from '../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-destinatario-selectors';
import 'rxjs/add/operator/skipWhile';
import {Sandbox as ComunicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';


declare const require: any;
const printStyles = require('app/ui/bussiness-components/ticket-radicado/ticket-radicado.component.css');

@Component({
  selector: 'app-radicar-salida',
  templateUrl: './radicar-salida.component.html',
  styleUrls: ['./radicar-salida.component.css']
})
export class RadicarSalidaComponent implements OnInit, AfterContentInit, AfterViewInit, OnDestroy, TaskForm {

  type = TaskTypes.TASK_FORM;

  formStatusIcon = 'assignment';
  tabIndex = 0;
  editable = true;
  printStyle: string = printStyles;

  @ViewChild('datosGenerales') datosGenerales;
  @ViewChild('datosRemitente') datosRemitente;
  @ViewChild('datosDestinatario') datosDestinatario;

  @ViewChild('ticketRadicado') ticketRadicado;

  task: TareaDTO;
  radicacion: ComunicacionOficialDTO;
  barCodeVisible = false;

  formsTabOrder: Array<any> = [];

  // Unsubscribers
  activeTaskUnsubscriber: Subscription;

  tipoDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  sedeDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;

  mediosRecepcionDefaultSelection$: Observable<ConstanteDTO>;
  tipoDestinatarioDefaultSelection$: Observable<ConstanteDTO>;


  formDatosGenerales: any;
  formDatosRemitentes: any;
  formDestinatarioInterno: any;
  formDestinatarioExterno: any;

  destinatariosInternos: any;
  destinatariosExternos: any;

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
    console.log('AFTER VIEW INIT...');
  }

  radicarSalida() {
    this.formDatosGenerales = this.datosGenerales.form.value;
    this.formDatosRemitentes = this.datosRemitente.form.value;

    this.formDestinatarioExterno =
      this.datosDestinatario.destinatarioExterno.form.value;
    this.destinatariosExternos =
      this.datosDestinatario.destinatarioExterno.listaDestinatarios;

    this.formDestinatarioInterno =
      this.datosDestinatario.destinatarioInterno.form.value;
    this.destinatariosInternos =
      this.datosDestinatario.destinatarioInterno.listaDestinatarios;

  }


  hideTicketRadicado() {
    this.barCodeVisible = false;
  }

  showTicketRadicado() {
    this.barCodeVisible = true;
  }

  disableEditionOnForms() {
    this.editable = false;
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
    console.log('ABORT...');
    /*this._taskSandBox.abortTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      instanciaProceso: this.task.idInstanciaProceso
    });*/
  }

  save(): Observable<any> {
    console.log('SAVE...');
    return Observable.of({id: 'ID'});
  }

  restore() {
    console.log('RESTORE...')
  }

  ngOnDestroy() {
    console.log('ON DESTROY...');
    this.activeTaskUnsubscriber.unsubscribe();
  }

}
