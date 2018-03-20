import { ChangeDetectorRef, Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { TareaDTO } from 'app/domain/tareaDTO';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs/Rx';
import { ConstanteDTO } from 'app/domain/constanteDTO';

@Component({
  selector: 'disposicion-final',
  templateUrl: './disposicion-final.component.html',
  encapsulation: ViewEncapsulation.None,
})

export class DisposicionFinalComponent implements OnInit, OnDestroy {

    task: TareaDTO;
    formSearch: FormGroup;
    validations: any = {};
    stacked: boolean;

    tiposDisposicionFinal$: Observable<ConstanteDTO[]>;
    sedes$: Observable<ConstanteDTO[]>;
    dependencias$: Observable<ConstanteDTO[]>;
    series$: Observable<ConstanteDTO[]>;
    subseries$: Observable<ConstanteDTO[]>;

    listaDisposiciones: any[];
    selectedItemsListaDisposiciones: any[];

    constructor(
              private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef) {

    this.initForm();
  }

    initForm() {
      this.formSearch = this.formBuilder.group({
        'tipoDisposicionFinal': [null, Validators.required],
        'sede': [null, Validators.required],
        'dependencia': [null, Validators.required],
        'serie': [null, Validators.required],
        'subserie': [null, Validators.required],
        'idUnidadDocumental': [null],
        'nombreUnidadDocumental': [null],
        'descriptor1': [null],
        'descriptor2': [null],
      });
    }

    ngOnInit() {}

    ngOnDestroy() {}

    transponer() {
        this.stacked = !this.stacked;
    }
}
