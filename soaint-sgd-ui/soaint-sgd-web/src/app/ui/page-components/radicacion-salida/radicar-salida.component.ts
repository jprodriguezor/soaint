import {
  AfterContentInit, AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit,
  ViewChild
} from '@angular/core';
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
import {getArrayData as getFuncionarioArrayData} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Sandbox as PaisSandbox} from '../../../infrastructure/state-management/paisDTO-state/paisDTO-sandbox';
import {Sandbox as FuncionariosSandbox} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {
  getTipoDocumentoArrayData, getTipoPersonaArrayData, getTipoDestinatarioArrayData
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {ViewFilterHook} from "../../../shared/ViewHooksHelper";
import {ComunicacionOficialEntradaDTV} from "../../../shared/data-transformers/comunicacionOficialEntradaDTV";
import {RadicacionSalidaDTV} from "../../../shared/data-transformers/radicacionSalidaDTV";
import {AbstractControl, FormControl, Validators} from "@angular/forms";
import {ExtendValidators} from "../../../shared/validators/custom-validators";


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
  @ViewChild('datosContacto') datosContacto;
  @ViewChild('ticketRadicado') ticketRadicado;

  task: TareaDTO;
  radicacion: ComunicacionOficialDTO;
  barCodeVisible = false;

  formsTabOrder: Array<any> = [];
  activeTaskUnsubscriber: Subscription;

  tipoDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  sedeDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;
  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;
  funcionariosSuggestions$: Observable<FuncionarioDTO[]>;


  formDatosGenerales: any;

  constructor(private _sandbox: RadicarComunicacionesSandBox,
              private _coSandbox: ComunicacionOficialSandbox,
              private _store: Store<RootState>,
              private _dependenciaSandbox: DependenciaSandbox,
              private _paisSandbox: PaisSandbox,
              private _funcionarioSandbox: FuncionariosSandbox,
              private _changeDetectorRef: ChangeDetectorRef) {
   // this.tipoDestinatarioSuggestions$ = this._store.select(getTipoDestinatarioArrayData);
  //  this.sedeDestinatarioSuggestions$ = this._store.select(sedeDestinatarioEntradaSelector);
   // this.dependenciaGrupoSuggestions$ = this._store.select(DependenciaGrupoSelector);
  //  this.funcionariosSuggestions$ = this._store.select(getFuncionarioArrayData);
    //this._dependenciaSandbox.loadDependencies({});
   // this._paisSandbox.loadDispatch();
   // this._funcionarioSandbox.loadAllFuncionariosDispatch();
  }



  ngOnInit() {
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;

      ViewFilterHook.addFilter(this.task.nombre+'-showFieldShipment',() => true);

      ViewFilterHook.addFilter(this.task.nombre+'-dataContact',value => Object.assign(value,{
        'clase_envio' : [null],
        'modalidad_correo':[null],
      }));

      this.datosContacto.form.setValidators([
        ExtendValidators.requiredIf('distribucion','física','clase_envio'),
        ExtendValidators.requiredIf('distribucion','física','modalidad_correo'),

      ]);



      this.restore();
    });

   this._changeDetectorRef.detectChanges();
  }

  ngAfterContentInit() {
   this.formsTabOrder.push(this.datosGenerales);
   this.formsTabOrder.push(this.datosContacto);
    console.log('AFTER VIEW INIT...');
  }

  ngAfterViewInit() {
    console.log('AFTER VIEW INIT...');
  }

  radicarSalida() {

    const radicacionEntradaFormPayload: any = {
       generales: this.datosGenerales.form.value,
       descripcionAnexos: this.datosGenerales.descripcionAnexos,
       radicadosReferidos: this.datosGenerales.radicadosReferidos,
       task: this.task,
       destinatarioInterno:this.datosContacto.listaDestinatariosInternos,
       destinatarioExt:this.datosContacto.listaDestinatariosExternos,
    };

    const comunicacionOficialDTV = new RadicacionSalidaDTV(radicacionEntradaFormPayload, this._store);

    this.radicacion = comunicacionOficialDTV.getComunicacionOficial();

    console.log(this.radicacion);

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

    ViewFilterHook.removeFilter(this.task.nombre+'-showFieldShipment');

    ViewFilterHook.removeFilter(this.task.nombre+'-dataContact');

    this.activeTaskUnsubscriber.unsubscribe();
  }


  radicacionButtonIsShown():boolean{

      const conditions:boolean[] = [
      this.datosGenerales.form.valid,
      this.datosContacto.form.valid,
      this.datosContacto.listaDestinatariosExternos.length + this.datosContacto.listaDestinatariosInternos.length > 0
    ];

    return  conditions.every( condition => condition);
  }

}
