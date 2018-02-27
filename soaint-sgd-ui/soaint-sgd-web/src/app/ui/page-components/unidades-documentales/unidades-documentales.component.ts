import { Component, OnInit } from '@angular/core';
import { StateUnidadDocumental } from 'app/ui/page-components/unidades-documentales/state.unidad.documental';
import { FormBuilder } from '@angular/forms';
import { UnidadDocumentalApiService } from 'app/infrastructure/api/unidad-documental.api';

@Component({
  selector: 'app-unidades-documentales',
  templateUrl: './unidades-documentales.component.html',
  styleUrls: ['./unidades-documentales.component.css']
})
export class UnidadesDocumentalesComponent implements OnInit {

  State: StateUnidadDocumental;

  constructor(
    private fb: FormBuilder,
    private unidadDocumentalApiService: UnidadDocumentalApiService
  ) {
    this.State = new StateUnidadDocumental(
      this.fb,
      this.unidadDocumentalApiService
    );
  }

  ngOnInit() {
    this.State.InitForm();
  }

}
