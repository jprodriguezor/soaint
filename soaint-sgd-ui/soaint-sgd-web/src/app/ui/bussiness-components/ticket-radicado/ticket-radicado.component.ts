import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-ticket-radicado',
  templateUrl: './ticket-radicado.component.html',
  styleUrls: ['./ticket-radicado.component.scss']
})
export class TicketRadicadoComponent implements OnInit {


  @Input() anexos: string = null;

  @Input() folios: string = null;

  @Input() noRadicado: string = null;

  @Input() fecha: string = null;

  @Input() remitente: string = null;

  @Input() remitenteSede: string = null;

  @Input() remitenteGrupo: string = null;

  @Input() destinatario: string = null;

  @Input() destinatarioSede: string = null;

  @Input() destinatarioGrupo: string = null;


  constructor() {
  }

  ngOnInit() {
  }

}
