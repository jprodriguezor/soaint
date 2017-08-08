import {Component, AfterViewInit, ElementRef, Renderer, ViewChild, OnInit, OnDestroy} from '@angular/core';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {LoadAction as FuncionarioLoadAction} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-actions';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {

  constructor(private _store: Store<RootState>) {
  }

  ngOnInit() {
    this._store.select(s => s.auth.isAuthenticated).filter(isAuthenticated => { return isAuthenticated }).subscribe(() => this._store.dispatch(new FuncionarioLoadAction()));
  }

}
