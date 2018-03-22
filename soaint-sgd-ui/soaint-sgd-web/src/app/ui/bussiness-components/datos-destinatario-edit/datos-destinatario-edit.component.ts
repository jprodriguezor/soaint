import { Component, OnInit, Input } from '@angular/core';
import { DatosRemitenteStateService } from '../datos-remitente-edit/datos-remitente-state-service';

@Component({
  selector: 'app-datos-destinatario-edit',
  templateUrl: './datos-destinatario-edit.component.html',
})

export class DatosDestinatarioEditComponent implements OnInit {

  @Input()
  state: DatosRemitenteStateService = null;

  constructor() { }

  ngOnInit() {
  }

}
