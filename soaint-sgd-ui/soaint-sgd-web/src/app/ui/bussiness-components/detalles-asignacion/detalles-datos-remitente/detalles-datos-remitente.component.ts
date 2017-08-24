import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ComunicacionOficialDTO} from "../../../../domain/comunicacionOficialDTO";
import {ConstanteDTO} from '../../../../domain/constanteDTO';

@Component({
  selector: 'app-detalles-datos-remitente',
  templateUrl: './detalles-datos-remitente.component.html',
  styleUrls: ['./detalles-datos-remitente.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DetallesDatosRemitenteComponent implements OnInit {

  @Input()
  comunicacion: ComunicacionOficialDTO;

  @Input()
  constantesList: ConstanteDTO[];

  constructor() {
  }

  ngOnInit() {
  }

}
