import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { TareaDTO } from 'app/domain/tareaDTO';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs/Rx';
import { ConstanteDTO } from 'app/domain/constanteDTO';
import { UnidadDocumentalDTO } from '../../../domain/unidadDocumentalDTO';
import { UnidadDocumentalApiService } from '../../../infrastructure/api/unidad-documental.api';
import { StateUnidadDocumentalService } from '../unidades-documentales/state.unidad.documental';

@Component({
  selector: 'disposicion-final',
  templateUrl: './disposicion-final.component.html',
  encapsulation: ViewEncapsulation.None,
})

export class DisposicionFinalComponent implements OnInit, OnDestroy {

    state: StateUnidadDocumentalService;
    validations: any = {};
    stacked: boolean;

    unidadesDocumentales$: Observable<UnidadDocumentalDTO[]> = Observable.of([]);

    series$: Observable<ConstanteDTO[]> = Observable.of([]);
    subseries$: Observable<ConstanteDTO[]> = Observable.of([]);

    listaDisposiciones: any[];
    selectedItemsListaDisposiciones: any[];

    constructor(
              private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private _unidadDocumentalStateService: StateUnidadDocumentalService
            ) {
      this.state = _unidadDocumentalStateService;
    }

    ngOnInit() {
      this.state.InitForm([
        // 'tipoDisposicionFinal',
        'sede',
        'dependencia',
      ]);
      this.StateLoadData();
    }

    StateLoadData() {
      this.state.GetListadoSedes();
      this.state.SetFormSubscriptions([
        'sede',
        'dependencia',
        'serie',
      ]);
      this.state.Listar();
    }

    ngOnDestroy() {

    }

    transponer() {
        this.stacked = !this.stacked;
    }

}
