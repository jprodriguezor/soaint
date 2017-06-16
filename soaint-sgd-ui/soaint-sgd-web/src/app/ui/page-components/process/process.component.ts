import {Component, OnInit} from '@angular/core';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {getArrayData as ProcesoDtoArrayData} from '../../../infrastructure/state-management/procesoDTO-state/procesoDTO-selectors';
import {ProcesoDTO} from '../../../domain/procesoDTO';

@Component({
  selector: 'app-workspace',
  templateUrl: './process.component.html'
})
export class ProcessComponent implements OnInit {

  procesos$: Observable<Array<ProcesoDTO>>;

  selectedProcess: ProcesoDTO;

  constructor(private _store: Store<RootState>) {
    this.procesos$ = this._store.select(ProcesoDtoArrayData);
  }

  ngOnInit() {
  }

  iniciarProceso() {
    // this._store.dispatch()
  }

}

