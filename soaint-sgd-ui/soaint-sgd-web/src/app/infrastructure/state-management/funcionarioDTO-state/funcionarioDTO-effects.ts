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
import * as actions from './funcionarioDTO-actions';
import {Sandbox} from './funcionarioDTO-sandbox';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';

@Injectable()
export class Effects {

  constructor(private actions$: Actions,
              private _store$: Store<RootState>,
              private _sandbox: Sandbox) {
  }

  @Effect()
  load_authenticated: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.LOAD)
    .map(toPayload)
    .switchMap(
      (payload) => this._sandbox.loadAuthenticatedFuncionario(payload)
        .map((response) => new actions.LoadSuccessAction(response))
        .catch((error) => Observable.of(new actions.LoadFailAction({error})))
    );

  @Effect()
  loadAll: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.LOAD_ALL)
    // .withLatestFrom(this._store$, (action: Action, state: RootState) => state.proceso.ids)
    // .filter(([action, state]) => {
    //   console.log(action, state);
    //   return state === [];
    // })
    // .distinctUntilChanged()
    // .let(isLoaded())
    .map(toPayload)
    .switchMap(
      (payload) => this._sandbox.loadAllFUncionarios(payload)
        .map((response) => new actions.LoadAllSuccessAction({data: response}))
        .catch((error) => Observable.of(new actions.LoadAllFailAction({error}))
        )
    );



}
