import {
  AfterContentInit, AfterViewInit, ChangeDetectorRef, Component, OnDestroy, OnInit,
  ViewChild
} from '@angular/core';
import {ComunicacionOficialDTO} from 'app/domain/comunicacionOficialDTO';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {TareaDTO} from '../../../domain/tareaDTO';
import {TaskForm} from '../../../shared/interfaces/task-form.interface';
import {TaskTypes} from '../../../shared/type-cheking-clasess/class-types';
import 'rxjs/add/operator/skipWhile';
import {
    getSelectedDependencyGroupFuncionario
} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {ViewFilterHook} from "../../../shared/ViewHooksHelper";
import {RadicacionSalidaDTV} from "../../../shared/data-transformers/radicacionSalidaDTV";
import {AbstractControl, Validators} from "@angular/forms";
import {
  COMUNICACION_EXTERNA, DESTINATARIO_EXTERNO, DESTINATARIO_INTERNO,
  RADICACION_SALIDA
} from "../../../shared/bussiness-properties/radicacion-properties";
import * as moment from "moment";
import {RadicarSuccessAction} from "app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-actions";
import {RsTicketRadicado} from "./components/rs-ticket-radicado/rs-ticket-radicado.component";
import {RadicacionSalidaService} from "../../../infrastructure/api/radicacion-salida.service";
import {DependenciaDTO} from "../../../domain/dependenciaDTO";
import {LoadNextTaskPayload} from "../../../shared/interfaces/start-process-payload,interface";
import {ScheduleNextTaskAction} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions";
import {TASK_RADICACION_DOCUMENTO_SALIDA} from "../../../infrastructure/state-management/tareasDTO-state/task-properties";
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";
import {isNullOrUndefined} from "util";
import * as domtoimage from 'dom-to-image';
declare const require: any;
const printStyles = require('app/ui/bussiness-components/ticket-radicado/ticket-radicado.component.css');

@Component({
  selector: 'app-radicar-salida',
  templateUrl: './radicar-salida.component.html',
  styleUrls: ['./radicar-salida.component.css']
})
export class RadicarSalidaComponent implements OnInit, AfterContentInit, AfterViewInit, OnDestroy, TaskForm {

  type = TaskTypes.TASK_FORM;

  tabIndex = 0;
  editable = true;
  printStyle: string = printStyles;

  @ViewChild('datosGenerales') datosGenerales;
  @ViewChild('datosContacto') datosContacto;
  @ViewChild('ticketRadicado') ticketRadicado;
  @ViewChild('datosRemitente') datosRemitente;
  @ViewChild('datosEnvio') datosEnvio;

  task: TareaDTO;
  taskFilter?:string;
  radicacion: ComunicacionOficialDTO;
  barCodeVisible = false;

  formsTabOrder: Array<any> = [];
  activeTaskUnsubscriber: Subscription;
  dependencySubscription:Subscription;
  reqDigitInmediataUnsubscriber:Subscription;
  dependencySelected?:DependenciaDTO;

  formContactDataShown:Subscription;

   readonly tipoRadicacion = RADICACION_SALIDA;

  constructor(
    protected _store: Store<RootState>
    ,protected _changeDetectorRef: ChangeDetectorRef
    ,protected _sandbox:RadicacionSalidaService
    ,protected _taskSandbox:TaskSandBox) {

  }

  ngOnInit() {

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;

      if(this.task !== null){

        console.log(this.task);

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
        // Habilitando o desabilitando la tarea que se ejecutar� secuencialmente a la actual
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

    const radicacionEntradaFormPayload: any =  this.buildPayload();

    const comunicacionOficialDTV = new RadicacionSalidaDTV(radicacionEntradaFormPayload, this._store);

    const  radicacion = comunicacionOficialDTV.getComunicacionOficial();

    if(comunicacionOficialDTV.hasError){

      console.log(this.datosContacto.listaDestinatariosInternos);

      this.hideTicketRadicado();

      this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Es probable que exista un destinarario externo que no tenga correo. Revise porfavor!'}));

      return false;
    }

    this.radicacion = radicacion;

    this._sandbox.radicar(this.radicacion).subscribe((response) => {
      this.barCodeVisible = true;
      this.radicacion = response;
      this.radicacion.ppdDocumentoList= [{
        ideEcm:comunicacionOficialDTV.getDocumento().ideEcm,
        asunto:"",
        nroFolios:0,
        nroAnexos:0

      }];
      this.editable = false;
      this.datosGenerales.form.get('fechaRadicacion').setValue(moment(this.radicacion.correspondencia.fecRadicado).format('DD/MM/YYYY hh:mm'));
      this.datosGenerales.form.get('nroRadicado').setValue(this.radicacion.correspondencia.nroRadicado);

      const valueGeneral = this.datosGenerales.form.value;

      let predicate = (destinatario) => destinatario.tipoDestinatario.nombre == "Principal";

      let destinatarioPrincipal = this.datosContacto.listaDestinatariosInternos.find(predicate);

      if(destinatarioPrincipal === undefined){
        destinatarioPrincipal = this.datosContacto.listaDestinatariosExternos.find(predicate);

        if(destinatarioPrincipal !== undefined)
          this.ticketRadicado.setDataTicketRadicado(this.createTicketDestExterno(destinatarioPrincipal));
      }
      else{

        this.ticketRadicado.setDataTicketRadicado(this.createTicketDestInterno(destinatarioPrincipal));
      }

      this._changeDetectorRef.detectChanges();

      const self = this;

      if(this.mustSendImage(valueGeneral))
      setTimeout( () =>{ self.uploadTemplate(
        self.radicacion.correspondencia.codDependencia,
        self.radicacion.correspondencia.nroRadicado,
        comunicacionOficialDTV.getDocumento().ideEcm
      )},1000);


      this.disableEditionOnForms();

      this._store.dispatch(new RadicarSuccessAction({
        tipoComunicacion: valueGeneral.tipoComunicacion,
        numeroRadicado: response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null
      }));

        this._taskSandbox.completeTaskDispatch({
        idProceso: this.task.idProceso,
        idDespliegue: this.task.idDespliegue,
        idTarea: this.task.idTarea,
        parametros: this.buildTaskCompleteParameters(valueGeneral,response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null)

      });
    },() => {

      this.radicacion = null;
      this.hideTicketRadicado();
      this._changeDetectorRef.detectChanges();
    });
  }

  protected mustSendImage(general:any):boolean{

    return general.reqDistFisica == 2 && general.reqDigit == 2
  }

  protected uploadTemplate(codDependencia,nroRadicado,ideEcm){

    const node = document.getElementById("ticket-rad");

      if(!isNullOrUndefined(node)) {

      domtoimage.toBlob(node).then((blob) => {

        let formData = new FormData();

        formData.append("documento", blob, "etiqueta.png");
        if(!isNullOrUndefined(ideEcm))
         formData.append("idDocumento", ideEcm);
        formData.append("nroRadicado", nroRadicado);
        formData.append("codigoDependencia", codDependencia);

        this._sandbox.uploadTemplate(formData).subscribe(null,() => {
          this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Etiqueta no subida!'}));
        });
      });
    }

  }

  protected  buildPayload(): any{

    return {
      generales: this.datosGenerales.form.value,
      descripcionAnexos: this.datosGenerales.descripcionAnexos,
      radicadosReferidos: this.datosGenerales.radicadosReferidos,
      task: this.task,
      destinatarioInterno:this.datosContacto.listaDestinatariosInternos,
      destinatarioExt:this.datosContacto.listaDestinatariosExternos,
      remitente:this.datosRemitente.form.value,
      datosEnvio:this.datosEnvio !== undefined ? this.datosEnvio.form.value: undefined
    };
  }

  protected buildTaskCompleteParameters(generales:any,noRadicado:any):any{

    return {
      codDependencia:this.dependencySelected.codigo,
      numeroRadicado:noRadicado,
      requiereDigitalizacion:generales.reqDigit,
      requiereDistribucionDemanda:generales.reqDistFisica
    }
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
     this.datosGenerales.descripcionAnexos.length.toString(),
     valueGeneral.numeroFolio.toString(),
     this.radicacion.correspondencia.nroRadicado.toString(),
     this.radicacion.correspondencia.fecRadicado.toString(),
      valueRemitente.funcionarioGrupo.nombre,
      valueRemitente.sedeAdministrativa.nombre,
      valueRemitente.dependenciaGrupo.nombre,
      destinatario.nombre
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

    if(!isNullOrUndefined(this.reqDigitInmediataUnsubscriber))
     this.reqDigitInmediataUnsubscriber.unsubscribe();

  }

  radicacionButtonIsShown():boolean{

      const conditions:boolean[] = [
      this.datosGenerales.form.valid,
      this.datosRemitente.form.valid,
      this.datosGenerales.form.get("reqDistFisica").value != 1 || ( this.datosEnvio !== undefined && this.datosEnvio.form.valid),
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
