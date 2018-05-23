import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ArchivarDocumentoModel} from "./models/archivar-documento.model";
import {SolicitudCreacioUdModel} from "./models/solicitud-creacio-ud.model";
import {SolicitudCreacionUdService} from "../../../../infrastructure/api/solicitud-creacion-ud.service";
import {Sandbox as TaskSandbox} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {TareaDTO} from "../../../../domain/tareaDTO";
import {State as RootState} from "../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {getActiveTask} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {Subscription} from "rxjs/Subscription";
import {SeleccionarUnidadDocumentalComponent} from "./components/seleccionar-unidad-documental/seleccionar-unidad-documental.component";
import {ArchivarDocumentoApiService} from "../../../../infrastructure/api/archivar-documento.api";
import {isNullOrUndefined} from "util";
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {combineLatest} from "rxjs/observable/combineLatest";
import * as moment from "moment";
import {SeleccionarDocumentosComponent} from "./components/seleccionar-documentos/seleccionar-documentos.component";
import {Sandbox as AsigSandbox} from "../../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox";
import {Observable} from "rxjs/Observable";
import {ROUTES_PATH} from "../../../../app.route-names";
import {go} from "@ngrx/router-store";
import {afterTaskComplete} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers";

@Component({
  selector: 'app-archivar-documento',
  templateUrl: './archivar-documento.component.html',
  styleUrls: ['./archivar-documento.component.css']
})
export class ArchivarDocumentoComponent implements OnInit,OnDestroy {

   archivarDocumentoModel:ArchivarDocumentoModel = new ArchivarDocumentoModel();

   solicitudUDModel: SolicitudCreacioUdModel;

   currentPage:number = 1;

   enableButtonNext:boolean = false;

   showSolicitarButton:boolean = false;

   subscriptions:Subscription[] = [];

   task:TareaDTO;

   nroRadicado ;
   selectUd:SeleccionarUnidadDocumentalComponent;

  idEstadoTarea = '0000';

  activeTarea:boolean = true;

  @ViewChild('seleccionarDocumentosComponent')
  seleccionarDocumentosComponent:SeleccionarDocumentosComponent;

  @ViewChild('selectUD') selectUD:SeleccionarUnidadDocumentalComponent;


   constructor( private _solicitudService:SolicitudCreacionUdService,
               private  _taskSandbox:TaskSandbox,
               private _store:Store<RootState>,
               private _archivarDocumentoApi:ArchivarDocumentoApiService
               ) {

    this.solicitudUDModel = new SolicitudCreacioUdModel(this._solicitudService);
  }

  ngOnInit() {

   this.subscriptions.push(
     this._store.select(getActiveTask).subscribe(task => {

       this.task = task;

       if(task){

         this.subscriptions.push(
           this._archivarDocumentoApi.getTaskData(task.idInstanciaProceso,'0000').subscribe( response => {

             if(!isNullOrUndefined(response.payload)){

               const payload = response.payload;
               if(!isNullOrUndefined(payload.data))
                 this.selectUD.form.setValue(payload.data);
               if(!isNullOrUndefined(payload.operation))
                this.selectUD.operation = payload.operation;
               if(!isNullOrUndefined(payload.solicitudes)){
                 this.solicitudUDModel.Solicitudes = payload.solicitudes;
                 if(payload.operation== "solicitarUnidadDocumental")
                  this.selectUD.unidadesDocumentales$ = Observable.of(this.solicitudUDModel.Solicitudes);
               }
             }
           })
         );

         if(!isNullOrUndefined(this.task.variables.numeroRadicado)){

           this.nroRadicado = this.task.variables.numeroRadicado;
         }
       }
     })
  );

   this.subscriptions.push(this._store.select(getSelectedDependencyGroupFuncionario).subscribe( () => {

     if(this.currentPage == 1)
       return;

     this.seleccionarDocumentosComponent.DropSubscriptions();

     this.currentPage = 1;

     this.enableButtonNext = false;

   }));

    this.subscriptions.push(afterTaskComplete.subscribe(() => {

      if(!this.showSolicitarButton)
       this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
    }));
   }

  next(){
    this.currentPage++;
  }

  prev(){

    this.currentPage --;
  }

  save(){

    const tareaDTO = {
      idTareaProceso: this.idEstadoTarea,
      idInstanciaProceso: this.task.idInstanciaProceso,
      payload: {
        data: this.selectUD.form.value,
        operation: this.selectUD.operation,
        solicitudes: this.solicitudUDModel.Solicitudes
      },
    };

     this._archivarDocumentoApi.guardarEstadoTarea(tareaDTO).subscribe();
  }

  solicitarCreacionUd(){

    this.subscriptions.push(this.solicitudUDModel.Solicitar().subscribe( () => {

      this.solicitudUDModel.Solicitudes = [];

      this.subscriptions.push(
        combineLatest(this._store.select(getSelectedDependencyGroupFuncionario)
          ,this._store.select(getAuthenticatedFuncionario)).subscribe(([dependencia,funcionario]) => {
          this._taskSandbox.completeTaskDispatch({
            idProceso: this.task.idProceso,
            idDespliegue: this.task.idDespliegue,
            idTarea: this.task.idTarea,
            parametros: {
              creacionUD: 1,
              codSede: dependencia.codSede,
              codDependencia: dependencia.codigo,
              idSolicitante:funcionario.id,
              fechaSolicitud: moment().format('YYYY-MM-DD')
            }
          });

        })
      );
      this.selectUD.form.reset();
      this.activeTarea = true;

    }));
  }

  toggleEnableButtonNext(section:string){

    this.enableButtonNext = section == "bUnidadDocumental" && !isNullOrUndefined(this.archivarDocumentoModel.UnidadDocumental);

    this.showSolicitarButton = section != "bUnidadDocumental";
  }

  setEnableButtonNext(enable:boolean){
    this.enableButtonNext = !this.showSolicitarButton && enable;
  }


  finalizar(){

     if(!isNullOrUndefined(this.task)){
       this._taskSandbox.completeTaskDispatch({
         idProceso: this.task.idProceso,
         idDespliegue: this.task.idDespliegue,
         idTarea: this.task.idTarea,
         parametros:{
           creacionUD: 0
         }
       });
     }
  }

  ngOnDestroy(): void {

    // this.afterTaskCompleteSubscriptor.unsubscribe();

    this.subscriptions.forEach( subscrption => subscrption.unsubscribe());
  }
}
