import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ComunicacionOficialDTO} from "../../../../domain/comunicacionOficialDTO";

@Component({
  selector: 'app-detalles-datos-remitente',
  templateUrl: './detalles-datos-remitente.component.html',
  styleUrls: ['./detalles-datos-remitente.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DetallesDatosRemitenteComponent implements OnInit {

  @Input()
  comunicacion: ComunicacionOficialDTO;

  constructor() {
  }

  ngOnInit() {
  }

}
