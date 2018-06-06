import { Component, OnInit } from '@angular/core';
import { ComunicacionOficialDTO } from '../../../domain/comunicacionOficialDTO';
import { TareaDTO } from '../../../domain/tareaDTO';
import { VariablesTareaDTO } from '../produccion-documental/models/StatusDTO';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'app-cargar-planilla-salida',
  templateUrl: './cargar-planilla-salida.component.html',
  styleUrls: ['./cargar-planilla-salida.component.css']
})
export class CargarPlanillaSalidaComponent implements OnInit {

  listadoComunicaciones: ComunicacionOficialDTO[] = [];

  selectedComunications: ComunicacionOficialDTO[] = [];

  // tarea
  task: TareaDTO;

  taskVariables: VariablesTareaDTO = {};

  closedTask:  Observable<boolean> ;

  constructor() { }

  ngOnInit() {
    
  }

}
