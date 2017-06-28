import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {environment} from 'environments/environment';
import {HttpHandler} from 'app/infrastructure/security/http-handler';
import {Usuario} from 'app/domain/usuario';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as selectors from './selectors';
import * as actions from './actions';

import {go} from '@ngrx/router-store';

@Injectable()
export class NotificationSandbox {

  constructor(private _store: Store<State>) {
  }


  // selectorAuthenticated(): any {
  //   return this._store.select(selectors.);
  // }
  //
  // loginDispatch(payload: UserCredentials) {
  //   this._store.dispatch(new actions.LoginAction(payload));
  // }


}
