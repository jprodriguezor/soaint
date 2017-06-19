import {Component, OnInit} from '@angular/core';
import {Sandbox as ProcessDtoSandbox} from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox';
import {Sandbox as TaskDtoSandbox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {getArrayData as ProcessArrayData} from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-selectors';
import {
  getArrayData,
  getCompletedTasksArrayData as CompletedTasksArrayData,
  getInProgressTasksArrayData as InProgressTasksArrayData,
  getReservedTasksArrayData as ReservedTasksArrayData,
  getCanceledTasksArrayData as CanceledTasksArrayData
} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {TareaDTO} from 'app/domain/tareaDTO';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  staticProcess$: Observable<any[]>;

  allTasks$: Observable<TareaDTO[]>;

  completedTasks$: Observable<TareaDTO[]>;

  canceledTasks$: Observable<TareaDTO[]>;

  reservedTasks$: Observable<TareaDTO[]>;

  inProgressTasks$: Observable<TareaDTO[]>;

  constructor(private _store: Store<RootState>, private _processSandbox: ProcessDtoSandbox, private _taskSandbox: TaskDtoSandbox) {

    this.allTasks$ = this._store.select(getArrayData);
    this.staticProcess$ = this._store.select(ProcessArrayData);
    this.completedTasks$ = this._store.select(CompletedTasksArrayData);
    this.reservedTasks$ = this._store.select(ReservedTasksArrayData);
    this.inProgressTasks$ = this._store.select(InProgressTasksArrayData);
    this.canceledTasks$ = this._store.select(CanceledTasksArrayData);

  }

  ngOnInit() {
    this._taskSandbox.loadDispatch();
    this._processSandbox.loadDispatch();
  }

}
