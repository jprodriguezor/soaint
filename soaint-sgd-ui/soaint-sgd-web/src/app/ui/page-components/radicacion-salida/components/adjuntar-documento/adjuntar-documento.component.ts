import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {TareaDTO} from "../../../../../domain/tareaDTO";
import {Sandbox as TaskSandbox} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {Subscription} from "rxjs/Subscription";
import {getActiveTask} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import  {Store} from "@ngrx/store";
import {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";

@Component({
  selector: 'app-adjuntar-documento',
  templateUrl: './adjuntar-documento.component.html',
})
export class AdjuntarDocumentoComponent implements OnInit {

  @ViewChild('documentList') documentList;

  task:TareaDTO;

  activeTaskUnsubscriber:Subscription;

  constructor( private _taskSandbox:TaskSandbox,private _store:Store<RootState>) { }

  ngOnInit() {

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {

      this.task = activeTask;

    });
  }

  finalizar(){

  }

}
