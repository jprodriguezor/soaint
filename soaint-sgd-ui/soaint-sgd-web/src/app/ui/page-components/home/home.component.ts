import {Component, OnInit} from '@angular/core';
import {Sandbox as ProcessDtoSandbox} from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox';
import {Sandbox as TaskDtoSandbox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {getArrayData as ProcessArrayData} from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-selectors';
import {
  getArrayData,
  getInProgressTasksArrayData as InProgressTasksArrayData,
  getReservedTasksArrayData as ReservedTasksArrayData,
  getCanceledTasksArrayData as CanceledTasksArrayData, getTasksStadistics
} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {TareaDTO} from 'app/domain/tareaDTO';
import 'rxjs/add/operator/withLatestFrom';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  staticProcess$: Observable<any[]>;

  allTasks$: Observable<TareaDTO[]>;

  tasksStadistics$: Observable<any[]>;

  inProgressTasks$: Observable<TareaDTO[]>;

  visibleRadicadoTicket: boolean = false;

  constructor(private _store: Store<RootState>, private _processSandbox: ProcessDtoSandbox, private _taskSandbox: TaskDtoSandbox) {

    this.allTasks$ = this._store.select(getArrayData);
    this.staticProcess$ = this._store.select(ProcessArrayData);

    this.inProgressTasks$ = this._store.select(InProgressTasksArrayData);
    Observable.combineLatest(
      this._store.select(getTasksStadistics),
      this._taskSandbox.getTaskStats()
    ).switchMap(([stats, unresolve]) => {
        debugger;
        console.log(stats, unresolve);
        return Observable.of(stats);
      }).subscribe(res => {
        console.log(res);
    });


    this.tasksStadistics$ = this._store.select(getTasksStadistics);
    // this.tasksStadistics$ = Observable.combineLatest(
    //   this._store.select(getTasksStadistics),
    //   this.completedTasks$
    // ).switchMap(([stats, completed]) => {
    //   const res = [...stats, {name: 'Completadas', value: completed.length}];
    //   return Observable.of(res);
    // });


  }

  ngOnInit() {
    this._taskSandbox.loadDispatch();
    this._processSandbox.loadDispatch();
  }

  showRadicadoTicket(event) {
    event.preventDefault();
    this.visibleRadicadoTicket = true;

  }

}
