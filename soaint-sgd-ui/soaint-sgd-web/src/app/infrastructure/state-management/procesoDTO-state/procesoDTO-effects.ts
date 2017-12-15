import {Injectable} from '@angular/core';
import {Actions, Effect, toPayload} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/pairwise';
import 'rxjs/add/observable/combineLatest';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/let';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/withLatestFrom';
import 'rxjs/add/operator/distinctUntilChanged';
import * as actions from './procesoDTO-actions';
import {StartTaskAction} from '../tareasDTO-state/tareasDTO-actions';
import {Sandbox} from './procesoDTO-sandbox';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {getSelectedDependencyGroupFuncionario} from '../funcionarioDTO-state/funcionarioDTO-selectors';

@Injectable()
export class Effects {

  constructor(private actions$: Actions,
              private _store$: Store<RootState>,
              private _sandbox: Sandbox) {
  }

  @Effect()
  load: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.LOAD)
    .map(toPayload)
    .switchMap(
      (payload) => this._sandbox.loadData(payload)
        .map((response) => new actions.LoadSuccessAction({data: response}))
        .catch((error) => Observable.of(new actions.LoadFailAction({error}))
        )
    );

  @Effect()
  startProcess: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.START_PROCESS)
    .map(toPayload)
    .withLatestFrom(this._store$.select(getSelectedDependencyGroupFuncionario))
    .switchMap(
      ([payload, dependency]) => this._sandbox.startProcess(payload, dependency)
        .map((response) => new actions.LoadTasksInsideProcessAction({data: response}))
        .catch((error) => Observable.of(new actions.LoadFailAction({error}))
        )
    );

  @Effect()
  LoadTasksInside: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.LOAD_TASKS_INSIDE_PROCESS)
    .map(toPayload)
    .switchMap(
      (payload) => this._sandbox.loadTasksInsideProcess(payload)
        .mergeMap((response) => {
            return [
              new actions.LoadTaskSuccessAction({data: response}),
              new StartTaskAction(response)
            ];
        })
        .catch((error) => Observable.of(new actions.LoadFailAction({error}))
        )
    );


}
