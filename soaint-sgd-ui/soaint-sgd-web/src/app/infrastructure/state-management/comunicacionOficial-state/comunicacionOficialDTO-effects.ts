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
import * as actions from './comunicacionOficialDTO-actions';
import {Sandbox} from './comunicacionOficialDTO-sandbox';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';

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
    .map(toPayload)
    .distinctUntilChanged()
    .withLatestFrom(this._store$)
    .distinctUntilChanged()
    // .let(isLoaded())

    .switchMap(
      ([payload, state]) => {
        console.log(state);
        return this._sandbox.loadData(payload)
          .map((response) => new actions.LoadSuccessAction(response))
          .catch((error) => Observable.of(new actions.LoadFailAction({error}))
          )
      }
    );


}
