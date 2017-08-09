import {Injectable} from '@angular/core';
import {Effect, Actions, toPayload} from '@ngrx/effects';
import {Action} from '@ngrx/store';
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

import {Store} from '@ngrx/store';
import * as actions from './tareasDTO-actions';
import {Sandbox} from './tareasDTO-sandbox';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {go} from '@ngrx/router-store';
import * as login from 'app/ui/page-components/login/redux-state/login-actions';
import {TareaDTO} from 'app/domain/tareaDTO';
import {getNextTask} from './tareasDTO-selectors';
import {StartProcessAction} from '../procesoDTO-state/procesoDTO-actions';
import {StartProcessPayload} from '../../../shared/interfaces/start-process-payload,interface';

function isLoaded() {
  return (source) =>
    source.filter(values => {
      console.log(values);
      return true
    })
}

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
    .switchMap(
      (payload) => this._sandbox.loadData(payload)
        .map((response) => new actions.LoadSuccessAction(response))
        .catch((error) => Observable.of(new actions.LoadFailAction({error})))
    );

  @Effect()
  takeReservedTask: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.START_TASK)
    .map(toPayload)
    .switchMap(
      (payload) => this._sandbox.startTask(payload)
        .map((response: any) => new actions.StartTaskSuccessAction(response))
        .catch((error) => Observable.of(new actions.StartTaskFailAction({error})))
    );

  @Effect({dispatch: false})
  upViewRelatedToTask: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.START_TASK_SUCCESS)
    .map(toPayload)
    .do((payload) => this._sandbox.initTaskDispatch(payload));

  @Effect()
  goToNextTask: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.CONTINUE_WITH_NEXT_TASK)
    .withLatestFrom(this._store$.select(s => s.tareas.nextTask))
    .map(([payload, nextTask]) => { console.log(payload, nextTask); return new StartProcessAction(nextTask)});
    // .do(([jobs, nextTask]: [any, any]) => {        this.store.dispatch( new StartProcessAction(nextTask) );     })


  @Effect()
  completeTask: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.COMPLETE_TASK)
    .map(toPayload)
    .switchMap(
      (payload) => this._sandbox.completeTask(payload)
        .map((response: any) => new actions.CompleteTaskSuccessAction(response))
        .catch((error) => Observable.of(new actions.CompleteTaskFailAction({error})))
    )


}
