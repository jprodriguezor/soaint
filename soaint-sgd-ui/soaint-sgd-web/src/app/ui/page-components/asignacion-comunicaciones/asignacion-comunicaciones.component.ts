import {Component, OnInit} from '@angular/core';
import {ComunicacionOficialDTO} from '../../../domain/ComunicacionOficialDTO';
import {ComunicacionApiService} from 'app/infrastructure/__api.include';

@Component({
  selector: 'app-asignacion-comunicaciones',
  templateUrl: './asignacion-comunicaciones.component.html'
})
export class AsignarComunicacionesComponent implements OnInit {

  comunicaciones: Array<ComunicacionOficialDTO> = [];

  constructor(private _comunicacionOficial: ComunicacionApiService) {
  }

  ngOnInit() {

  }

  listarComunicaciones() {
    this._comunicacionOficial.list('', {});
  }
}

