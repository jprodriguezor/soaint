import {Component, OnInit} from '@angular/core';
import {Sandbox as ProcessDtoSandbox} from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {getArrayData as ProcessArrayData} from '../../../infrastructure/state-management/procesoDTO-state/procesoDTO-selectors';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  allProcess$: Observable<any[]>;

  constructor(private _store: Store<RootState>, private _processSandbox: ProcessDtoSandbox) {

    this.allProcess$ = this._store.select(ProcessArrayData);
  }

  ngOnInit() {

  }

}
