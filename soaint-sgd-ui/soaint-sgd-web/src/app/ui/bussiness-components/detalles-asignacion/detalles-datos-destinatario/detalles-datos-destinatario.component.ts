import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ComunicacionOficialDTO} from "../../../../domain/comunicacionOficialDTO";
import {AgentDTO} from "../../../../domain/agentDTO";

@Component({
  selector: 'app-detalles-datos-destinatario',
  templateUrl: './detalles-datos-destinatario.component.html',
  styleUrls: ['./detalles-datos-destinatario.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DetallesDatosDestinatarioComponent implements OnInit {

  @Input()
  comunicacion: ComunicacionOficialDTO;

  agentList: AgentDTO[];

  constructor() {
  }

  ngOnInit() {
  }

}
