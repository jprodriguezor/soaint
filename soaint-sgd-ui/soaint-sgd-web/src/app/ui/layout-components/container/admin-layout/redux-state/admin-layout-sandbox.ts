import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {environment} from 'environments/environment';
import {HttpHandler} from 'app/infrastructure/security/http-handler';
import {Usuario} from 'app/domain/usuario';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as selectors from './admin-layout-selectors';
import * as actions from './admin-layout-actions';
import * as models from '../models/admin-layout.model';

@Injectable()
export class AdminLayoutSandbox {

  constructor(private _store: Store<State>) {
  }

  selectorLayoutMode(): Observable<models.MenuOrientation> {
    return this._store.select(selectors.LayoutMode);
  }

  selectorProfileMode(): Observable<models.ProfileMode> {
    return this._store.select(selectors.ProfileMode);
  }

  selectorIsAutenticated(): Observable<boolean> {
    return this._store.select(selectors.IsAuthenticated);
  }

  selectorDarkMenu(): Observable<boolean> {
    return this._store.select(selectors.DarkMenu);
  }

  dispatchChangeOnMenu(payload) {
    this._store.dispatch(new actions.ChangeMenuOrientationAction(payload));
  }

}
