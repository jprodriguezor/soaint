import {Component, OnInit} from '@angular/core';
import {Sandbox as ProcessDtoSandbox} from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox';
import {Sandbox as TaskDtoSandbox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {getArrayData as ProcessArrayData} from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-selectors';
import {getArrayData as TaskArrayData} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  bpmProcess$: Observable<any[]>;

  bpmTasks$: Observable<any[]>;

  constructor(private _store: Store<RootState>, private _processSandbox: ProcessDtoSandbox, private _taskSandbox: TaskDtoSandbox) {

    this.bpmProcess$ = this._store.select(ProcessArrayData);
    this.bpmTasks$ = this._store.select(TaskArrayData);

  }

  ngOnInit() {

  }

}
