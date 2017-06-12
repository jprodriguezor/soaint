import {Injectable} from '@angular/core';
import {Effect, Actions, toPayload} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/mergeMap';
import * as login from './login-actions';
import {LoginSandbox} from './login-sandbox';
import {go} from '@ngrx/router-store'

@Injectable()
export class LoginEffects {

  constructor(private actions$: Actions,
              private loginSandbox: LoginSandbox) {
  }

  @Effect()
  login: Observable<Action> = this.actions$
    .ofType(login.ActionTypes.LOGIN)
    .map(toPayload)
    .switchMap(
      (payload) => this.loginSandbox.login({login: payload.username, password: payload.password})
        .mergeMap((response: any) => [
          new login.LoginSuccessAction({token: response.token}),
          go('/home')
        ])
        .catch(error => Observable.of(new login.LoginFailAction({error: error})))
    );

}
