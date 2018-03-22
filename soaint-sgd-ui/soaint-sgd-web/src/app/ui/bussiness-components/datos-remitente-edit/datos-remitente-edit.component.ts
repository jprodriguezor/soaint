import { Component, OnInit, Input } from '@angular/core';
import { DatosRemitenteStateService } from './datos-remitente-state-service';

@Component({
  selector: 'app-datos-remitente-edit',
  templateUrl: './datos-remitente-edit.component.html',
})
export class DatosRemitenteEditComponent implements OnInit {

  @Input()
  state: DatosRemitenteStateService = null;

  constructor(
  ) {

  }

  ngOnInit() {
  }

}
