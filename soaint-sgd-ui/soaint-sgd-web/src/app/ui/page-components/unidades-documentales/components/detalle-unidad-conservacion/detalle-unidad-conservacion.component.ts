import { Component, OnInit, Input } from '@angular/core';
import { StateUnidadDocumentalService } from 'app/infrastructure/service-state-management/state.unidad.documental';

@Component({
  selector: 'app-detalle-unidad-conservacion',
  templateUrl: './detalle-unidad-conservacion.component.html',
  styleUrls: ['./detalle-unidad-conservacion.component.css']
})
export class DetalleUnidadConservacionComponent implements OnInit {

  @Input() state: StateUnidadDocumentalService;

  constructor() { }

  ngOnInit() {

  }

}
