import {Component, Input, ViewEncapsulation} from '@angular/core';
import {ConstanteDTO} from '../../../../domain/constanteDTO';
import {AgentDTO} from '../../../../domain/agentDTO';
import {ContactoDTO} from '../../../../domain/contactoDTO';

@Component({
  selector: 'app-detalles-datos-remitente',
  templateUrl: './detalles-datos-remitente.component.html',
  styleUrls: ['./detalles-datos-remitente.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DetallesDatosRemitenteComponent {

  @Input()
  constantesList: ConstanteDTO[];

  @Input()
  remitente: AgentDTO;

  @Input()
  contactos: ContactoDTO[];


  constructor() {
  }

}
