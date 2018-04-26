import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Sandbox as TaskSandbox} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
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
export class DistribucionComponent implements OnInit,OnDestroy {

  task:TareaDTO;

  activeTaskUnsubscriber:Subscription;

  constructor(private fb:FormBuilder,private _taskSandbox:TaskSandbox,private _store:Store<RootState>) {


  }

  ngOnInit() {

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {

      this.task = activeTask;

    });
  }

  ngOnDestroy(){

    this.activeTaskUnsubscriber.unsubscribe();
  }

  save(){

    this._taskSandbox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: []

    });
  }


}
