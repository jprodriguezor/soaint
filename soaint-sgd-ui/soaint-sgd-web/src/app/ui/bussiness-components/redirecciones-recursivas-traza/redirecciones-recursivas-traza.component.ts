import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RedireccionDTO} from '../../../domain/redireccionDTO';

@Component({
  selector: 'app-redirecciones-recursivas-traza',
  templateUrl: './redirecciones-recursivas-traza.component.html'
})
export class RedireccionesRecursivasTrazaComponent {

  @Input() redireccionesFallidas: Array<RedireccionDTO>;
  @Output() onEjecutarRedireccionOrigen = new EventEmitter<RedireccionDTO>();

  editable: boolean;

  constructor() {
  }

  ejecutarRedireccionOrigen(index) {
    this.onEjecutarRedireccionOrigen.emit(this.redireccionesFallidas[index]);
  }

}
