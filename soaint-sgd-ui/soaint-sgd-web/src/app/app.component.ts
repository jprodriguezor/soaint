import {Component, AfterViewInit, ElementRef, Renderer, ViewChild, OnInit, OnDestroy} from '@angular/core';
import {MessageBridgeService, MessageType} from 'app/infrastructure/web/message-bridge.service';
import {Subscription} from 'rxjs/Subscription';
import {SessionService, WebModel} from 'app/infrastructure/web/session.service';
// import { LoginModel } from 'app/ui/page-components/login/login.model';
import {LoginSandbox} from 'app/ui/page-components/login/__login.include';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';

import {LoadAction as FuncionarioLoadAction} from 'app/infrastructure/state-management/FuncionarioDTO-state/FuncionarioDTO-actions';
import {isAuthenticated} from './ui/page-components/login/redux-state/login-selectors';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {

  constructor(private _store: Store<RootState>) {
  }

  ngOnInit() {
    this._store.select(s => s.auth.isAuthenticated).filter(isAuthenticated => { console.log(isAuthenticated); return isAuthenticated }).subscribe(() => this._store.dispatch(new FuncionarioLoadAction()));
  }

}
