import {Injectable} from '@angular/core';
import {Effect, Actions, toPayload} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import {Store} from '@ngrx/store';
import * as fromRoot from 'app/infrastructure/redux-store/redux-reducers';
import * as administration from './admin-layout-actions';
import {AdminLayoutSandbox} from './admin-layout-sandbox';

@Injectable()
export class LoginEffects {

  constructor(private _actions$: Actions,
              private _store$: Store<fromRoot.State>,
              private _sandbox: AdminLayoutSandbox) {
  }

}
