import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Sandbox as TaskSandbox} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {Sandbox as AsignacionSandbox} from "../../../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox";
import {Sandbox as ComunicacionSandbox} from "../../../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox";
import {TareaDTO} from "../../../../../domain/tareaDTO";
import {Subscription} from "rxjs/Subscription";
import {getActiveTask} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";

@Component({
  selector: 'app-distribucion',
  templateUrl: './distribucion.component.html',
  styleUrls: ['./distribucion.component.css']
})
export class DistribucionComponent implements OnInit {

  validForm:boolean = false;

  task:TareaDTO;

  activeTaskUnsubscriber:Subscription;

  constructor(
    private fb:FormBuilder,
    private _taskSandbox:TaskSandbox,
    private _store:Store<RootState>,
    private _asignacionSandbox:AsignacionSandbox,
    private _comunicacionSandbox:ComunicacionSandbox
    ) { }

  ngOnInit() {

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {

      this.task = activeTask;

    });
  }

  save(){

    const noRadicado = this.task.variables.numeroRadicado;

    this._asignacionSandbox
      .obtenerComunicacionPorNroRadicado(noRadicado)
      .switchMap( comunicacion => this._comunicacionSandbox.actualizarComunicacion(comunicacion))
      .subscribe(() => {

        this._taskSandbox.completeTaskDispatch({
          idProceso: this.task.idProceso,
          idDespliegue: this.task.idDespliegue,
          idTarea: this.task.idTarea,
          parametros: {
            numeroRadicado:this.task.variables.numeroRadicado
          }
        });
      });

  }

  checkFormValid(isValid:boolean){

    console.log(isValid);

    this.validForm = isValid;
  }


}
