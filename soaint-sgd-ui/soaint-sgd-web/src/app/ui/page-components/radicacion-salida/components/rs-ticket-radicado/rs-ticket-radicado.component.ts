import {Component, Input, OnInit} from '@angular/core';
import {TicketRadicado} from '../../../../bussiness-components/ticket-radicado/ticket-radicado.component';
import {
  DESTINATARIO_EXTERNO,
  DESTINATARIO_INTERNO,
  DESTINATARIO_PRINCIPAL
} from '../../../../../shared/bussiness-properties/radicacion-properties';
import {Guid} from "../../../../../infrastructure/utils/guid-generator";

export class RsTicketRadicado implements  TicketRadicado {

  constructor(
    public tipoDestinatario: string
  , public  anexos: string
  , public folios: string
  , public noRadicado: string
  , public fecha: string
  , public remitente?: string
  , public remitenteSede?: string
  , public  remitenteGrupo?: string
  , public destinatario?: string
  , public  destinatarioSede?: string
  , public  destinatarioGrupo?: string) {


  }

  isInterno(): boolean {

    return this.tipoDestinatario === DESTINATARIO_INTERNO;
  }

  isExterno(): boolean {

    return this.tipoDestinatario === DESTINATARIO_EXTERNO;
  }

}

@Component({
  selector: 'rs-ticket-radicado',
  templateUrl: './rs-ticket-radicado.component.html',
})
export class RsTicketRadicadoComponent  {

  ticket?: RsTicketRadicado;

   @Input() idTicket= Guid.next();

  constructor() { }

  ngOnInit() {
  }

  setDataTicketRadicado(ticket: RsTicketRadicado) {

     this.ticket = ticket;
  }

}
