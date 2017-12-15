import {Injectable} from '@angular/core';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Observable} from 'rxjs/Observable';
import {BAD_AUTHENTICATION} from '../../shared/lang/es';
import {PushNotificationAction} from '../state-management/notifications-state/notifications-actions';
import {LogoutAction} from '../../ui/page-components/login/redux-state/login-actions';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

@Injectable()
export class ErrorHandlerService {

  private token$: Observable<string>;

  constructor(private _store: Store<State>) {
    this.token$ = _store.select(s => s.auth.token);
  }

  handleAuthExpiredError(error): Observable<any> {
    return this.token$.take(1).switchMap(token => {
      if (token !== null) {
        this._store.dispatch(new LogoutAction());
      } else {
        this._store.dispatch(new PushNotificationAction({
          severity: 'info',
          summary: BAD_AUTHENTICATION
        }));
      }
      return Observable.throw(error);
    });
  }

  handleError(error) {
    this._store.dispatch(new PushNotificationAction({
      summary: error.status
    }));
    return Observable.throw(error);
  }
}
