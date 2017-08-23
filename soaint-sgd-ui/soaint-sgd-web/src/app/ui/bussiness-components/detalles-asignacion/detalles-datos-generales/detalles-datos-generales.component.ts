import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ComunicacionOficialDTO} from "../../../../domain/comunicacionOficialDTO";

@Component({
  selector: 'app-detalles-datos-generales',
  templateUrl: './detalles-datos-generales.component.html',
  styleUrls: ['./detalles-datos-generales.component.scss'],
  encapsulation: ViewEncapsulation.None

})
export class DetallesDatosGeneralesComponent implements OnInit {

  @Input()
  comunicacion: ComunicacionOficialDTO;

  constructor() {
  }

  ngOnInit() {
  }

}
