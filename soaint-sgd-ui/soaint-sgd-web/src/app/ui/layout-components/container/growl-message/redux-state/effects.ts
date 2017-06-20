import {Injectable} from '@angular/core';
import {Effect, Actions, toPayload} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/mergeMap';
import * as actions from './actions';
import {go} from '@ngrx/router-store'

@Injectable()
export class LoginEffects {

  constructor(private actions$: Actions,
              ) {
  }

  // @Effect()
  // login: Observable<Action> = this.actions$
  //   .ofType(actions.ActionTypes.SHOW_GROWL_ERROR)
  //   .map(toPayload)
  //   .switchMap(
  //       payload => this._sandbox.login({login: payload.username, password: payload.password})
  //       .mergeMap((token) => [
  //         new actions.LoginSuccessAction({token: token}),
  //         go('/home')
  //       ])
  //       .catch(error => Observable.of(new login.LoginFailAction({error: error})))
  //   );

}
