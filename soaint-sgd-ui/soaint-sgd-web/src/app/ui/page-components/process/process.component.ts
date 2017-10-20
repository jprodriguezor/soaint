import {Component, OnInit} from '@angular/core';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {getArrayData as ProcesoDtoArrayData} from '../../../infrastructure/state-management/procesoDTO-state/procesoDTO-selectors';
import {ProcesoDTO} from '../../../domain/procesoDTO';
import {Sandbox as ProcessDtoSandbox} from '../../../infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox';
import {process_info} from '../../../../environments/environment';

@Component({
  selector: 'app-workspace',
  templateUrl: './process.component.html'
})
export class ProcessComponent implements OnInit {

  procesos$: Observable<Array<ProcesoDTO>>;

  selectedProcess: ProcesoDTO;

  constructor(private _store: Store<RootState>, private _processSandbox: ProcessDtoSandbox) {
    this.procesos$ = this._store.select(ProcesoDtoArrayData).map((procesos) => {
      return procesos.filter((proceso) => {
        return process_info[proceso.codigoProceso] && process_info[proceso.codigoProceso].show;
      });
    });
  }

  ngOnInit() {
  }

  iniciarProceso(process) {
    this._processSandbox.initProcessDispatch(process);
  }

  getProcessDisplayName(proceso) {
    console.log(proceso);
    return process_info[proceso.codigoProceso] ? process_info[proceso.codigoProceso].displayValue : proceso.nombreProceso
  }

}

