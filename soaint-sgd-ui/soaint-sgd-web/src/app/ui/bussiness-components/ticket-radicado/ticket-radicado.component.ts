import {Component, Input, OnInit} from '@angular/core';

interface TicketRadicado {
  anexos: string;

  folios: string;

  noRadicado: string;

  fecha: string;

  remitente?: string;

  remitenteSede?: string;

  remitenteGrupo?: string;

  destinatario?: string;

  destinatarioSede?: string;

  destinatarioGrupo?: string;
}

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

  setDataTicketRadicado(ticket: TicketRadicado) {
    this.anexos = ticket.anexos;
    this.folios = ticket.folios;
    this.noRadicado = ticket.noRadicado;
    this.fecha = ticket.fecha;
    this.remitente = ticket.remitente;
    this.remitenteSede = ticket.remitenteSede;
    this.remitenteGrupo = ticket.remitenteGrupo;
    this.destinatario = ticket.destinatario;
    this.destinatarioSede = ticket.destinatarioSede;
    this.destinatarioGrupo = ticket.destinatarioGrupo;
  }

  ngOnInit() {
  }

}
