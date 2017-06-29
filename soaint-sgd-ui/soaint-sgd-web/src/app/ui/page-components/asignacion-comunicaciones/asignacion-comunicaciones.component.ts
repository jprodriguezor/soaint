import {Component, OnInit} from '@angular/core';
import {ComunicacionOficialDTO} from '../../../domain/ComunicacionOficialDTO';
import {Sandbox as CominicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';

@Component({
  selector: 'app-asignacion-comunicaciones',
  templateUrl: './asignacion-comunicaciones.component.html'
})
export class AsignarComunicacionesComponent implements OnInit {

  comunicaciones: Array<ComunicacionOficialDTO> = [];

  estadosCorrespondencia: [{ label: string, value: string }];

  start_date: Date = new Date();

  end_date: Date = new Date();

  estadoCorrespondencia: any;

  constructor(private _comunicacionOficialApi: CominicacionOficialSandbox) {
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
        value: 'SIN_ASIGNAR'
      }
    ]
  }

  listarComunicaciones() {
    this._comunicacionOficialApi.loadDispatch({
      fecha_ini: this.start_date,
      fecha_fin: this.end_date,
      cod_estado: this.estadoCorrespondencia,
    });
  }
}

