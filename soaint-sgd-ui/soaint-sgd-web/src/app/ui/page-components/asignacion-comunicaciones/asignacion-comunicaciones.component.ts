import {Component, OnInit} from '@angular/core';
import {ComunicacionOficialDTO} from '../../../domain/ComunicacionOficialDTO';
import {ComunicacionApiService} from 'app/infrastructure/__api.include';

@Component({
  selector: 'app-asignacion-comunicaciones',
  templateUrl: './asignacion-comunicaciones.component.html'
})
export class AsignarComunicacionesComponent implements OnInit {

  comunicaciones: Array<ComunicacionOficialDTO> = [];

  estadosCorrespondencia: [{ label: string, value: string }];

  start_date: Date = new Date();

  end_date: Date = new Date();

  constructor(private _comunicacionOficial: ComunicacionApiService) {
    this.start_date.setHours(this.start_date.getHours() - 24);
  }

  ngOnInit() {
    this.llenarEstadosCorrespondencias();
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
    this._comunicacionOficial.list('', {});
  }
}

