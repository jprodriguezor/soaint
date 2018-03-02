import { Component, OnInit, Input } from '@angular/core';
import { StateUnidadDocumental } from 'app/ui/page-components/unidades-documentales/state.unidad.documental';

@Component({
  selector: 'app-detalle-unidad-conservacion',
  templateUrl: './detalle-unidad-conservacion.component.html',
  styleUrls: ['./detalle-unidad-conservacion.component.css']
})
export class DetalleUnidadConservacionComponent implements OnInit {

  @Input() State: StateUnidadDocumental;

  constructor() { }

  ngOnInit() {

  }

}
