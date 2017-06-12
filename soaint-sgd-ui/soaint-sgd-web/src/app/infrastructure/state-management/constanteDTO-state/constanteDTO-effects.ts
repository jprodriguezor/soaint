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
import {Store} from '@ngrx/store';
import * as actions from './constanteDTO-actions';
import {Sandbox} from './constanteDTO-sandbox';
import {go} from '@ngrx/router-store'

@Injectable()
export class Effects {

  constructor(private actions$: Actions,
              private _sandbox: Sandbox) {
  }

  @Effect()
  login: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.LOAD)
    .map(toPayload)
    .switchMap(
        (payload) => this._sandbox.loadData(payload)
          .map((response) => new actions.LoadSuccessAction({key: payload.key, data: response}))
          .catch((error) => Observable.of(new actions.LoadFailAction({error}))
      )
    );

}
