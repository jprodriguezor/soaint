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

import * as processActions from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-actions';
import {Sandbox as ProcessSandbox} from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox';
import {LogoutAction} from 'app/ui/page-components/login/redux-state/login-actions';
import {layoutWidth} from 'app/ui/layout-components/container/admin-layout/redux-state/admin-layout-selectors';


@Injectable()
export class AdminLayoutSandbox {

  constructor(private _store: Store<State>, private _processSandbox: ProcessSandbox) {
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

  selectorDeployedProcess(): Observable<any[]> {
    return this._store.select(this._processSandbox.selectorMenuOptions());
  }

  dispatchChangeOnMenu(payload) {
    this._store.dispatch(new actions.ChangeMenuOrientationAction(payload));
  }

  dispatchMenuOptionsLoad() {
    this._store.dispatch(new processActions.LoadAction());
  }

  dispatchLogoutUser() {
    this._store.dispatch(new LogoutAction());
  }

  dispatchWindowResize(payload?: { width: (number | ((el: any) => any)); height: number }) {
    this._store.dispatch(new actions.ResizeWindowAction(payload));
  }

  selectorWindowWidth() {
    return this._store.select(layoutWidth);
  }
}
