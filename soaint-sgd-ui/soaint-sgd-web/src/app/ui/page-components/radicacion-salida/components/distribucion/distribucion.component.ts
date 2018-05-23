import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Sandbox as TaskSandbox} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {Sandbox as AsignacionSandbox} from "../../../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox";
import {Sandbox as ComunicacionSandbox} from "../../../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox";
import {TareaDTO} from "../../../../../domain/tareaDTO";
import {Subscription} from "rxjs/Subscription";
import {getActiveTask} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {Observable} from "rxjs/Observable";
import {afterTaskComplete} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers";
import {LoadNextTaskPayload} from "../../../../../shared/interfaces/start-process-payload,interface";
import {ScheduleNextTaskAction} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions";

@Component({
  selector: 'app-distribucion',
  templateUrl: './distribucion.component.html',
  styleUrls: ['./distribucion.component.css']
})
export class DistribucionComponent implements OnInit {

  task:TareaDTO;

  activeTaskUnsubscriber:Subscription;

  @ViewChild('formEnvio') formEnvio;

  showButtonSave$:Observable<boolean>;



  constructor(
    private fb:FormBuilder,
    private _taskSandbox:TaskSandbox,
    private _store:Store<RootState>,
    private _asignacionSandbox:AsignacionSandbox,
    private _comunicacionSandbox:ComunicacionSandbox,
    private _changeDetector: ChangeDetectorRef
    ) { }

  ngOnInit() {

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {

      this.task = activeTask;     


      this._store.dispatch(new ScheduleNextTaskAction(null));

    });

    this.showButtonSave$ = afterTaskComplete.mapTo(false).startWith(true);
  }

  save(){

    const noRadicado = this.task.variables.numeroRadicado;

    this._asignacionSandbox
      .obtenerComunicacionPorNroRadicado(noRadicado)
      .switchMap( comunicacion => {

        comunicacion.correspondencia.codModalidadEnvio =  this.formEnvio.form.get('modalidad_correo').value.codigo;
        comunicacion.correspondencia.codClaseEnvio =  this.formEnvio.form.get('clase_envio').value.codigo;


       return this._comunicacionSandbox.actualizarComunicacion(comunicacion);
      })
      .subscribe(() => {



         this._taskSandbox.completeTaskDispatch({
          idProceso: this.task.idProceso,
          idDespliegue: this.task.idDespliegue,
          idTarea: this.task.idTarea,
          parametros: {
            numeroRadicado:this.task.variables.numeroRadicado
          }
        });

         this._changeDetector.detectChanges();
      });



  }


}
