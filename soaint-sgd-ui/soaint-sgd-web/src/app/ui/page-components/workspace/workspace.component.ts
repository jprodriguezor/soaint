import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {TareaDTO} from 'app/domain/tareaDTO';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {getArrayData} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Sandbox as TaskDtoSandbox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';

@Component({
  selector: 'app-workspace',
  templateUrl: './workspace.component.html'
})
export class WorkspaceComponent implements OnInit {

  tasks$: Observable<TareaDTO[]>;

  selectedTask: any;

  constructor(private _store: Store<RootState>, private _taskSandbox: TaskDtoSandbox) {
    this.tasks$ = this._store.select(getArrayData);
  }

  ngOnInit() {
    this._taskSandbox.loadDispatch();
  }

  iniciarTarea(task) {
    this._taskSandbox.startTaskDispatch(task);
  }

}

