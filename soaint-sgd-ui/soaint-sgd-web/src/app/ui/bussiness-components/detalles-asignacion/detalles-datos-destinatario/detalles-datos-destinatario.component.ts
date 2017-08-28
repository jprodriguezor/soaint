import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {AgentDTO} from '../../../../domain/agentDTO';
import {ConstanteDTO} from '../../../../domain/constanteDTO';

@Component({
  selector: 'app-detalles-datos-destinatario',
  templateUrl: './detalles-datos-destinatario.component.html',
  styleUrls: ['./detalles-datos-destinatario.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DetallesDatosDestinatarioComponent implements OnInit {

  @Input()
  constantesList: ConstanteDTO[];

  @Input()
  destinatarios: AgentDTO[];

  constructor() {
  }

  ngOnInit() {
  }

}
