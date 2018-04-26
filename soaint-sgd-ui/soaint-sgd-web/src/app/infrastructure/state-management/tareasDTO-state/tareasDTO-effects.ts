import {EventEmitter, Injectable,Output} from '@angular/core';
import {Actions, Effect, toPayload} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/observable/of';
import 'rxjs/add/observable/combineLatest';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/withLatestFrom';
import 'rxjs/add/operator/distinctUntilChanged';
import * as actions from './tareasDTO-actions';
import {Sandbox} from './tareasDTO-sandbox';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {LoadTasksInsideProcessAction} from '../procesoDTO-state/procesoDTO-actions';
import {tassign} from 'tassign';
import {ROUTES_PATH} from '../../../app.route-names';
import {go} from '@ngrx/router-store';
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
    .distinctUntilChanged()
    .map(toPayload)
    .withLatestFrom(this._store$.select(getSelectedDependencyGroupFuncionario))
    .switchMap(
      ([payload, dependency]) => this._sandbox.loadData(payload, dependency)
        .map((response) => new actions.LoadSuccessAction(response))
        .catch((error) => Observable.of(new actions.LoadFailAction({error})))
    );

  @Effect()
  getStats: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.GET_TASK_STATS)
    .switchMap(
      () => this._sandbox.getTaskStats()
        .map((response) => new actions.GetTaskStatsSuccessAction(response))
        .catch((error) => Observable.of(new actions.LoadFailAction({error})))
    );

  @Effect()
  startTask: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.START_TASK)
    .map(toPayload)
    .switchMap(
      (payload) => this._sandbox.getTaskVariables(payload)
        .mergeMap((taskVariables) => this._sandbox.startTask(payload)
          .map((res) => Object.assign({}, res, {variables: taskVariables.variables})
          ))
        .map((response: any) => new actions.StartTaskSuccessAction(response))
        .catch((error) => Observable.of(new actions.StartTaskFailAction({error})))
    );

  @Effect()
  reserveTask: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.RESERVE_TASK)
    .map(toPayload)
    .switchMap(
      (payload) => this._sandbox.getTaskVariables(payload)
        .mergeMap((taskVariables) => this._sandbox.reserveTask(payload)
          .map((res) => Object.assign({}, res, {variables: taskVariables.variables})
          ))
        .map((response: any) => new actions.StartTaskSuccessAction(tassign(payload, {
          estado: response.estado,
          variables: response.variables
        })))
        .catch((error) => Observable.of(new actions.StartTaskFailAction({error})))
    );

  @Effect()
  takeInProgressTask: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.START_INPROGRESS_TASK)
    .map(toPayload)
    .switchMap(
      (payload) => this._sandbox.getTaskVariables(payload)
        .mergeMap((taskVariables: any) => [
          new actions.LockActiveTaskAction(Object.assign({}, payload, {variables: taskVariables.variables})),
          new actions.StartTaskSuccessAction(Object.assign({}, payload, {variables: taskVariables.variables}))
        ])
        .catch((error) => Observable.of(new actions.StartTaskFailAction({error})))
    );


  @Effect({dispatch: false})
  upViewRelatedToTask: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.START_TASK_SUCCESS)
    .map(toPayload)
    .do((payload) => {
      this._sandbox.taskRoutingStart();
      this._sandbox.initTaskDispatch(payload)
    });


  @Effect()
  goToNextTask: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.CONTINUE_WITH_NEXT_TASK)
    .withLatestFrom(this._store$.select(s => s.tareas.nextTask))
    .flatMap(([payload, nextTask]) => [new actions.UnlockActiveTaskAction(), new LoadTasksInsideProcessAction(nextTask)]);


  @Effect()
  completeTask: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.COMPLETE_TASK)
    .map(toPayload)
    .switchMap(
      (payload) => this._sandbox.completeTask(payload)
        .map((response: any) => new actions.CompleteTaskSuccessAction(response))
        .catch((error) => Observable.of(new actions.CompleteTaskFailAction({error})))
    );
  // .withLatestFrom(this._store$.select(s => s.tareas.nextTask))
  // .filter(([action, nextTask]) => nextTask !== null )
  // .map(([action, nextTask]) => new actions.ContinueWithNextTaskAction(nextTask))
  @Effect()
  completeBackTask: Observable<Action> = this.actions$
  .ofType(actions.ActionTypes.COMPLETE_BACK_TASK)
  .map(toPayload)
  .switchMap(
    (payload) => this._sandbox.completeTask(payload)
      .mergeMap((response: any) => [new actions.CompleteBackTaskSuccessAction(response), go(['/' + ROUTES_PATH.workspace])])
      .catch((error) => Observable.of(new actions.CompleteBackTaskFailAction({error})))
    );


  @Effect()
  abortTask: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.ABORT_TASK)
    .map(toPayload)
    .switchMap(
      (payload) => this._sandbox.abortTask(payload)
        .mergeMap((response: any) => [new actions.AbortTaskSuccessAction(response), go(['/' + ROUTES_PATH.workspace])])
        .catch((error) => Observable.of(new actions.AbortTaskFailAction({error})))
    );
    


}
