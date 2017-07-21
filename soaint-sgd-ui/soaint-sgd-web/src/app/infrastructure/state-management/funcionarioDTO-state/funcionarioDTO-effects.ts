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
import * as actions from './funcionarioDTO-actions';
import {Sandbox} from './funcionarioDTO-sandbox';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {getSelectedDependencyGroupFuncionario} from './funcionarioDTO-selectors';

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
        .mergeMap((response) => [
            new actions.LoadSuccessAction(response),
            new actions.SelectDependencyGroupAction()
          ])
          .catch((error) => Observable.of(new actions.LoadFailAction({error})))
        );

  @Effect()
  loadAll: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.LOAD_ALL)
    .withLatestFrom(this._store$.select(getSelectedDependencyGroupFuncionario))
    .distinctUntilChanged()
    .switchMap(
      ([payload, state]) => {
        return this._sandbox.loadAllFuncionarios(state.codigo)
          .map((response) => new actions.LoadAllSuccessAction(response))
          .catch((error) => Observable.of(new actions.LoadAllFailAction({error}))
          )
      }
    );


}
