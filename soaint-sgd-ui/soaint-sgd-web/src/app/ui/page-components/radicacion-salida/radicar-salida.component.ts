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
import {
  getArrayData as getFuncionarioArrayData,
  getSelectedDependencyGroupFuncionario
} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
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
import {
  COMUNICACION_EXTERNA, DESTINATARIO_EXTERNO, DESTINATARIO_INTERNO,
  RADICACION_SALIDA
} from "../../../shared/bussiness-properties/radicacion-properties";
import * as moment from "moment";
import {RadicarSuccessAction} from "app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-actions";
import {RsTicketRadicado} from "./components/rs-ticket-radicado/rs-ticket-radicado.component";
import {after} from "selenium-webdriver/testing";
import {afterTaskComplete} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers";
import {go} from "@ngrx/router-store";
import {ROUTES_PATH} from "../../../app.route-names";
import {RadicacionSalidaService} from "../../../infrastructure/api/radicacion-salida.service";
import {DependenciaDTO} from "../../../domain/dependenciaDTO";
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";


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
  @ViewChild('datosRemitente') datosRemitente;

  task: TareaDTO;
  taskFilter?:string;
  radicacion: ComunicacionOficialDTO;
  barCodeVisible = false;

  formsTabOrder: Array<any> = [];
  activeTaskUnsubscriber: Subscription;
  dependencySubscription:Subscription;
  afterCompleteTaskSubscriber:Subscription;
  dependencySelected?:DependenciaDTO;



  formContactDataShown:Subscription;

   readonly tipoRadicacion = RADICACION_SALIDA;

  constructor(
    private _store: Store<RootState>
    ,private _changeDetectorRef: ChangeDetectorRef
    ,private _sandbox:RadicacionSalidaService
    ,private _taskSandbox:TaskSandBox) {

  }

  ngOnInit() {

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;

      if(this.task !== null){

        this.taskFilter = this.task.nombre+'-datos-contactos-show-form';

        ViewFilterHook.addFilter(this.taskFilter,() => false);
      }
      this.restore();
    });


    this.dependencySubscription =  this._store.select(getSelectedDependencyGroupFuncionario).subscribe( dependency => {

      this.dependencySelected = dependency;
    });

    this.formContactDataShown = this.validatorSubscription();

    ViewFilterHook.addFilter('datos-remitente-'+COMUNICACION_EXTERNA, valid => {

      if(this.datosGenerales.form.get('reqDistFisica').value )
        return valid;

        return valid && this.datosContacto.datosRemitentesExterno.destinatariosContactos.length > 0;

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

  radicarSalida() {

    const radicacionEntradaFormPayload: any = {
       generales: this.datosGenerales.form.value,
       descripcionAnexos: this.datosGenerales.descripcionAnexos,
       radicadosReferidos: this.datosGenerales.radicadosReferidos,
       task: this.task,
       destinatarioInterno:this.datosContacto.listaDestinatariosInternos,
       destinatarioExt:this.datosContacto.listaDestinatariosExternos,
       remitente:this.datosRemitente.form.value,
    };


    const comunicacionOficialDTV = new RadicacionSalidaDTV(radicacionEntradaFormPayload, this._store);

    this.radicacion = comunicacionOficialDTV.getComunicacionOficial();

    if(comunicacionOficialDTV.hasError){

      this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Es probable que exista un destinarario externo que no tenga correo. Revise porfavor!'}));
      return false;
    }

    this._sandbox.radicar(this.radicacion).subscribe((response) => {
      this.barCodeVisible = true;
      this.radicacion = response;
      this.editable = false;
      this.datosGenerales.form.get('fechaRadicacion').setValue(moment(this.radicacion.correspondencia.fecRadicado).format('DD/MM/YYYY hh:mm'));
      this.datosGenerales.form.get('nroRadicado').setValue(this.radicacion.correspondencia.nroRadicado);

      const valueGeneral = this.datosGenerales.form.value;

      this.datosContacto.listaDestinatariosInternos.forEach(destinatario => {

        console.log('destinatario');
        console.log(destinatario);

       this.ticketRadicado.setDataTicketRadicado(this.createTicketDestInterno(destinatario));
      });

      this.datosContacto.listaDestinatariosExternos.forEach(destinatario => {

        this.ticketRadicado.setDataTicketRadicado(this.createTicketDestExterno(destinatario));
      });

      this.disableEditionOnForms();

      this._store.dispatch(new RadicarSuccessAction({
        tipoComunicacion: valueGeneral.tipoComunicacion,
        numeroRadicado: response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null
      }));

       let requiereDigitalizacion = valueGeneral.reqDigit;

        this._taskSandbox.completeTaskDispatch({
        idProceso: this.task.idProceso,
        idDespliegue: this.task.idDespliegue,
        idTarea: this.task.idTarea,
        parametros: {
          codDependencia: this.dependencySelected.codigo,
          requiereDigitalizacion: requiereDigitalizacion,
          numeroRadicado: response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null,
        }
      });
    });
  }

  private createTicketDestInterno(destinatario:any):RsTicketRadicado{

    const valueGeneral = this.datosGenerales.form.value;
    const valueRemitente = this.datosRemitente.form.value;




     return new RsTicketRadicado(
      DESTINATARIO_INTERNO,
      this.datosGenerales.descripcionAnexos.length.toString(),
      valueGeneral.numeroFolio.toString(),
      this.radicacion.correspondencia.nroRadicado.toString(),
      this.radicacion.correspondencia.fecRadicado.toString(),
      null,
      valueRemitente.sedeAdministrativa.nombre,
      valueRemitente.dependenciaGrupo.nombre,
      destinatario.funcionario.nombre,
      destinatario.sede.nombre,
      destinatario.dependencia.nombre,
     );
  }

  private createTicketDestExterno(destinatario): RsTicketRadicado{

    const valueGeneral = this.datosGenerales.form.value;
    const valueRemitente = this.datosRemitente.form.value;

    return new RsTicketRadicado(
      DESTINATARIO_EXTERNO,
     this.datosGenerales.descripcionAnexos.length,
     valueGeneral.numeroFolio,
     this.radicacion.correspondencia.nroRadicado,
     this.radicacion.correspondencia.fecRadicado,
     destinatario.nombre,
     valueRemitente.sedeAdministrativa.nombre,
     valueRemitente.dependenciaGrupo.nombre,
     valueRemitente.funcionarioGrupo.nombre
  );
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
    this._taskSandbox.abortTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      instanciaProceso: this.task.idInstanciaProceso
    });
  }

  save(): Observable<any> {
    const payload: any = {
      generales: this.datosGenerales.form.value,
      remitente: this.datosRemitente.form.value,
      descripcionAnexos: this.datosGenerales.descripcionAnexos,
      radicadosReferidos: this.datosGenerales.radicadosReferidos,
      destinatariosInternos:this.datosContacto.listaDestinatariosInternos,
      destinatariosExternos:this.datosContacto.listaDestinatariosExternos,
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

  saveTask(){

    this.save().subscribe();
  }

  restore() {
    console.log('RESTORE...');
    if (this.task) {
      this._sandbox.quickRestore(this.task.idInstanciaProceso, this.task.idTarea).take(1).subscribe(response => {
        const results = response.payload;
        if (!results) {
          return;
        }

        this.restoreByPayload(results);

        // generales


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

  protected restoreByPayload(results){

    this.datosGenerales.form.patchValue(results.generales);
    this.datosGenerales.descripcionAnexos = results.descripcionAnexos;
    this.datosGenerales.radicadosReferidos = results.radicadosReferidos;

    // remitente
    this.datosRemitente.form.patchValue(results.remitente);

    // destinatario
    this.datosContacto.listaDestinatariosExternos = results.destinatariosExternos;
    this.datosContacto.listaDestinatariosInternos= results.destinatariosInternos;

    if (results.datosContactos) {
      const retry = setInterval(() => {
        if (typeof this.datosRemitente.datosContactos !== 'undefined') {
          this.datosRemitente.datosContactos.contacts = [...results.datosContactos];
          clearInterval(retry);
        }
      }, 400);
    }
  }

  ngOnDestroy() {
    console.log('ON DESTROY...');

    ViewFilterHook.removeFilter(this.taskFilter);

    ViewFilterHook.removeFilter('datos-remitente-'+ COMUNICACION_EXTERNA);

    this.activeTaskUnsubscriber.unsubscribe();

    this.afterCompleteTaskSubscriber.unsubscribe();

    this.dependencySubscription.unsubscribe();
  }

  radicacionButtonIsShown():boolean{

      const conditions:boolean[] = [
      this.datosGenerales.form.valid,
      this.datosRemitente.form.valid,
      this.datosContacto.listaDestinatariosExternos.length + this.datosContacto.listaDestinatariosInternos.length > 0
    ];

    return  conditions.every( condition => condition);
  }

  private validatorSubscription():Subscription{

    return this.datosContacto.datosRemitentesExterno.formDataContactShown.subscribe(form =>{

      let validator= (<AbstractControl>form.get('correoEle')).validator;

      let validatorsFn = [Validators.email,Validators.required];

      if(validator !== null){

        validatorsFn.push(validator);
      }

      (<AbstractControl>form.get('correoEle')).setValidators(validatorsFn);
    });
  }

  changeValidationAbility(enable:boolean){

    console.log(enable);

    // const control:AbstractControl = this.datosContacto.datosRemitentesExterno.destinatarioDatosContactos.form.get('correoEle');

    if(!enable){
      this.formContactDataShown = this.validatorSubscription();
    }
    else{

      this.formContactDataShown.unsubscribe();
    }


  }

}
