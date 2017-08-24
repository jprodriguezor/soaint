import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ComunicacionOficialDTO} from "../../../../domain/comunicacionOficialDTO";
import {AgentDTO} from "../../../../domain/agentDTO";
import {ConstanteDTO} from '../../../../domain/constanteDTO';

@Component({
  selector: 'app-detalles-datos-destinatario',
  templateUrl: './detalles-datos-destinatario.component.html',
  styleUrls: ['./detalles-datos-destinatario.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DetallesDatosDestinatarioComponent implements OnInit {

  @Input()
  comunicacion: ComunicacionOficialDTO;

  @Input()
  constantesList: ConstanteDTO[];

  constructor() {
  }

  ngOnInit() {
  }

}
