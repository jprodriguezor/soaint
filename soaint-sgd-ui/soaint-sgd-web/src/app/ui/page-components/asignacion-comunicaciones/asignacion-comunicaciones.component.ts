import {Component, OnInit} from '@angular/core';
import {Sandbox as CominicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {getArrayData} from 'app/infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-selectors';
import {CorrespondenciaDTO} from '../../../domain/correspondenciaDTO';

@Component({
  selector: 'app-asignacion-comunicaciones',
  templateUrl: './asignacion-comunicaciones.component.html'
})
export class AsignarComunicacionesComponent implements OnInit {

  comunicaciones$: Observable<CorrespondenciaDTO[]>;

  estadosCorrespondencia: [{ label: string, value: string }];

  start_date: Date = new Date();

  end_date: Date = new Date();

  estadoCorrespondencia: any;

  constructor(private _store: Store<RootState>, private _comunicacionOficialApi: CominicacionOficialSandbox) {
    this.comunicaciones$ = this._store.select(getArrayData);
    this.start_date.setHours(this.start_date.getHours() - 24);
  }

  ngOnInit() {
    this.llenarEstadosCorrespondencias();
    this.listarComunicaciones();
  }

  llenarEstadosCorrespondencias() {
    this.estadosCorrespondencia = [
      {
        label: 'SIN ASIGNAR',
        value: 'A'
      }
    ];
    this.estadoCorrespondencia = this.estadosCorrespondencia[0];
  }

  convertDate(inputFormat) {
    function pad(s) {
      return (s < 10) ? '0' + s : s;
    }

    let d = new Date(inputFormat);
    return [pad(d.getFullYear()), pad(d.getMonth() + 1), d.getDate()].join('-');
  }

  listarComunicaciones() {
    this._comunicacionOficialApi.loadDispatch({
      fecha_ini: this.convertDate(this.start_date),
      fecha_fin: this.convertDate(this.end_date),
      cod_estado: this.estadoCorrespondencia.value
    });
  }
}

