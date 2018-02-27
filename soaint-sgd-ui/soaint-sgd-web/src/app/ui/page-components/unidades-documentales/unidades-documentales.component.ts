import { Component, OnInit } from '@angular/core';
import { StateUnidadDocumental } from 'app/ui/page-components/unidades-documentales/state.unidad.documental';
import { FormBuilder } from '@angular/forms';
import { UnidadDocumentalApiService } from 'app/infrastructure/api/unidad-documental.api';
import { Store } from '@ngrx/store';
import { State } from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-reducers';
import { SerieSubserieApiService } from 'app/infrastructure/api/serie-subserie.api';

@Component({
  selector: 'app-unidades-documentales',
  templateUrl: './unidades-documentales.component.html',
  styleUrls: ['./unidades-documentales.component.css']
})
export class UnidadesDocumentalesComponent implements OnInit {

  State: StateUnidadDocumental;

  constructor(
    private fb: FormBuilder,
    private unidadDocumentalApiService: UnidadDocumentalApiService,
    private serieSubserieApiService: SerieSubserieApiService,
    private _store: Store<State>
  ) {
    this.State = new StateUnidadDocumental(
      this.fb,
      this.unidadDocumentalApiService,
      this.serieSubserieApiService,
      this._store
    );
  }

  ngOnInit() {
    this.State.InitData();
  }

}
