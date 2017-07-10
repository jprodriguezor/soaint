import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-ticket-radicado',
  templateUrl: './ticket-radicado.component.html',
  styleUrls: ['./ticket-radicado.component.scss']
})
export class TicketRadicadoComponent implements OnInit {


  @Input() anexos: string = '1';

  @Input() folios: string = '2';

  @Input() noRadicado: string = 'COR112EE31342342342';

  @Input() fecha: Date = new Date();

  @Input() remitente: string = 'JHOSEP MORALES';

  @Input() destinatarioSede: string = 'CentralDT';

  @Input() destinatarioGrupo: string = 'GRUPO DE ADMINISTRACION DOCUMENTAL';


  constructor() {
  }

  ngOnInit() {
  }

}
