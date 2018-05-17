import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CreateUDActionType, EventChangeActionArgs} from "./crear-unidad-documental";
import {SolicitudCreacionUDDto} from "../../../../domain/solicitudCreacionUDDto";
import {UnidadDocumentalApiService} from "../../../../infrastructure/api/unidad-documental.api";
import {State as RootState} from "../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {Sandbox as TaskSandbox} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {TareaDTO} from "../../../../domain/tareaDTO";
import {getActiveTask} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {SolicitudCreacionUdService} from "../../../../infrastructure/api/solicitud-creacion-ud.service";
import {Observable} from "rxjs/Observable";
import {getArrayData as getFuncionarioArrayData} from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {FormBuilder, FormGroup} from "@angular/forms";
import {SolicitudCreacioUdModel} from "../archivar-documento/models/solicitud-creacio-ud.model";
import {FuncionariosService} from "../../../../infrastructure/api/funcionarios.service";
import {Subscription} from "rxjs/Subscription";
import {ListaSolicitudCrearUdComponent} from "./lista-solicitud-crear-ud/lista-solicitud-crear-ud.component";
import {isNullOrUndefined} from "util";
import {afterTaskComplete} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers";
import {ROUTES_PATH} from "../../../../app.route-names";
import {go} from "@ngrx/router-store";

@Component({
  selector: 'app-crear-unidad-documental',
  templateUrl: './crear-unidad-documental.component.html',
})
export class CrearUnidadDocumentalComponent implements OnInit,OnDestroy {

  currentAction:CreateUDActionType;

  solicitudModel:SolicitudCreacioUdModel;

  solicitudSelected:SolicitudCreacionUDDto;

  task:TareaDTO;

   visiblePopup:boolean = false;

   solicitudesProcesadas$:Observable<any[]>;

   form:FormGroup;

   subscriptions:Subscription[] = [];




  constructor(private _store:Store<RootState>,
              private _taskSandbox:TaskSandbox,
              private _solicitudService:SolicitudCreacionUdService,
              private fb:FormBuilder,
              private _funcionarioService:FuncionariosService,
              private changeDetector: ChangeDetectorRef
              ) {

    this.formInit();

    this.solicitudModel = new SolicitudCreacioUdModel(this._solicitudService);
  }

  ngOnInit() {

    this.subscriptions.push(
      this._store.select(getActiveTask).subscribe( task =>{

        if(isNullOrUndefined(task))
          return;
        this.task = task;

        this.form.get('sede').setValue(task.variables.codSede);
        this.form.get('dependencia').setValue(task.variables.codDependencia);
        this.form.get('fechaSolicitud').setValue(task.variables.fechaSolicitud);

        this._funcionarioService.getFuncionarioById(task.variables.idSolicitante)
          .subscribe(funcionario => this.form.get('solicitante').setValue(funcionario.nombre));

        this.actualizarSolicitudesTramitadas();
      })
    );

   this.subscriptions.push(afterTaskComplete.subscribe(() => {
     this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
   }));

    }

  private formInit(){
    this.form =  this.fb.group({
      "sede"        : [{value:null,disabled:true}],
      "dependencia" : [{value:null,disabled:true}],
      "solicitante" : [{value:null,disabled:true}],
      "fechaSolicitud"  : [{value:null,disabled:true}]
    });
  }

  selectAction(evt:EventChangeActionArgs){

    this.currentAction = evt.action;

    this.solicitudSelected = evt.solicitud;
  }

  save(){

  }

   closePopup(){}

   actualizarSolicitudesTramitadas(event?){

    this.solicitudesProcesadas$ =  this._solicitudService.listarSolicitudesTramitadas({
                                        codSede:this.task.variables.codSede,
                                        codDependencia:this.task.variables.codDependencia,
                                        idSolicitante: this.task.variables.idSolicitante
                                      });

    this.currentAction = null;
  }



   finalizar(){

     this._taskSandbox.completeTaskDispatch({
       idProceso: this.task.idProceso,
       idDespliegue: this.task.idDespliegue,
       idTarea: this.task.idTarea
     });


   }

   ngOnDestroy(){
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }
}
