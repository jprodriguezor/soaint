import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {environment} from 'environments/environment';
import {HttpHandler} from 'app/infrastructure/security/http-handler';
import {Usuario} from 'app/domain/usuario';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {State} from 'app/redux-store/redux-store';
import * as selectors from './login-selectors';
import * as actions from './login-actions';
import {UserCredentials} from '../models/user-credentials.model';
import {go} from '@ngrx/router-store';

@Injectable()
export class LoginSandbox {

  constructor(private _router: Router,
              private _http: HttpHandler,
              private _store: Store<State>) {
  }

  getStore(): Store<State> {
    return this._store;
  }

  login(user: Usuario): Observable<Response> {
    return this._http.post(environment.security_endpoint + '/login', user);
  }

  routeToHome(): void {
    // this._router.navigate(['/home']);
    this._store.dispatch(go('/home'));
  }

  routeToLogin(): void {
    this._router.navigate(['/login']);
  }

  selectorLoading(): Observable<boolean> {
    return this._store.select(selectors.isLoading);
  }

  selectorError(): Observable<string> {
    return this._store.select(selectors.getError)
  }

  selectorToken(): Observable<string> {
    return this._store.select(selectors.getToken);
  }

  selectorAuthenticated(): any {
    return this._store.select(selectors.isAuthenticated);
  }

  loginDispatch(payload: UserCredentials) {
    this._store.dispatch(new actions.LoginAction(payload));
  }

  logoutDispatch() {
    this._store.dispatch(new actions.LogoutAction());
  }


}
