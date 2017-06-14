import {Injectable} from '@angular/core';
import {Effect, Actions, toPayload} from '@ngrx/effects';
import {Store, Action} from '@ngrx/store';
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
import 'rxjs/add/operator/withLatestFrom';
import 'rxjs/add/operator/distinctUntilChanged';

import * as actions from './constanteDTO-actions';
import {Sandbox} from './constanteDTO-sandbox';
import {go} from '@ngrx/router-store'
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';

function isEmptyObject(obj) {
  return (obj && (Object.keys(obj).length === 0));
}
function isLoaded() {
  return (source) =>
    source.filter(([action, state]) => {
      console.log(action);
      return state.constantes[action.payload.key].ids !== [];
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
    // .withLatestFrom(this._store$, (action, state) => state.constantes[action.payload.key].ids)
    // .let(isLoaded())
    .map(toPayload)
    .switchMap(
        (payload) => this._sandbox.loadData(payload)
          .map((response) => new actions.LoadSuccessAction({key: payload.key, data: response}))
          .catch((error) => Observable.of(new actions.LoadFailAction({error}))
      )
    );

}
