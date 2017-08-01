import {Injectable} from '@angular/core';
import {Effect, Actions, toPayload} from '@ngrx/effects';
import {Action} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/mergeMap';
import * as actions from './notifications-actions';
import {go} from '@ngrx/router-store'
import {Sandbox} from './notifications-sandbox';

@Injectable()
export class Effects {

  constructor(private actions$: Actions,
              private _sandbox: Sandbox) {
  }

  @Effect({dispatch: false})
  notify: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.PUSH_NOTIFICATION)
    .map(toPayload)
    .do(payload => this._sandbox.showNotification(payload));

}
