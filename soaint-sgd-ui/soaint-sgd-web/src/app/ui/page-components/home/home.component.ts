import {Component, OnInit} from '@angular/core';
import {Sandbox as ProcessDtoSandbox} from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox';
import {Sandbox as TaskDtoSandbox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {getArrayData as ProcessArrayData} from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-selectors';
import {
  getArrayData, getTasksStadistics, getInProgressTasksArrayData
} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {TareaDTO} from 'app/domain/tareaDTO';
import 'rxjs/add/operator/withLatestFrom';
import {GetTaskStatsAction} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  staticProcess$: Observable<any[]>;

  allTasks$: Observable<TareaDTO[]>;

  tasksStadistics$: Observable<any[]>;

  inProgressTasks$: Observable<TareaDTO[]>;

  completedTasks: any = 0;
  reservedTasks: any = 0;
  readyTasks = 0;

  visibleRadicadoTicket = false;

  constructor(private _store: Store<RootState>, private _processSandbox: ProcessDtoSandbox, private _taskSandbox: TaskDtoSandbox) {

    this._store.dispatch(new GetTaskStatsAction());
    this.allTasks$ = this._store.select(getArrayData);
    this.staticProcess$ = this._store.select(ProcessArrayData);

    this.inProgressTasks$ = this._store.select(getInProgressTasksArrayData);
    this.tasksStadistics$ = this._store.select(getTasksStadistics).map(value => {
      this.completedTasks = value.find(status => status.name === 'COMPLETADO');
      this.readyTasks = value.find(status => status.name === 'LISTO');
      this.reservedTasks = value.find(status => status.name === 'RESERVADO');

      return value;
    });

    // this.tasksStadistics$ = this._store.select(getTasksStadistics);
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
  }

  showRadicadoTicket(event) {
    event.preventDefault();
    this.visibleRadicadoTicket = true;

  }

}
