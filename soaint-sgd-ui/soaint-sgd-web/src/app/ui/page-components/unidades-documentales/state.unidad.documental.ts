import { ListadoUnidadDocumentalModel } from 'app/ui/page-components/unidades-documentales/models/listado.unidad.documental.model';
import { FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import { UnidadDocumentalApiService } from 'app/infrastructure/api/unidad-documental.api';
import { Observable } from 'rxjs/Observable';
import { Store } from '@ngrx/store';
import { State } from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-reducers';
import { SerieSubserieApiService } from 'app/infrastructure/api/serie-subserie.api';
import { SerieDTO } from 'app/domain/serieDTO';
import { SubserieDTO } from 'app/domain/subserieDTO';
import { UnidadDocumentalAccion } from 'app/ui/page-components/unidades-documentales/models/enums/unidad.documental.accion.enum';



export class StateUnidadDocumental {

    ListadoUnidadDocumental: ListadoUnidadDocumentalModel[] = [];
    ListadoSeries: Observable<SerieDTO[]>;
    ListadoSubseries: Observable<SubserieDTO[]>;
    OpcionSeleccionada: number;

    formBuscar: FormGroup;

    constructor(
        private fb: FormBuilder,
        private unidadDocumentalApiService: UnidadDocumentalApiService,
        private serieSubserieApiService: SerieSubserieApiService,
        private _store: Store<State>
    ) {
        this.InitForm();
    }

    VerDetalleUnidadConservacion() {

    }

    OnBlurEvents(control: string) {
        const formControl = this.formBuscar.get(control);
        if (control === 'serie') {
            this.GetSubSeries(formControl.value);
        }
    }

    InitData() {
        this.InitForm();
        this.GetListadosSeries();
        this.OpcionSeleccionada = 1 // abrir
    }

    InitForm() {
       this.formBuscar = this.fb.group({
        serie: ['', [Validators.required]],
        subserie: ['', [Validators.required]],
        identificador: [''],
        nombre: [''],
        descriptor1: [''],
        descriptor2: [''],
        accion: [''],
       });
    }

    GetListadosSeries() {
        this.ListadoSeries = this.serieSubserieApiService.ListarSerie({});
        this.ListadoSubseries = Observable.empty<SubserieDTO[]>();
    }

    GetSubSeries(serie: string) {
        this.ListadoSubseries = this.serieSubserieApiService.ListarSubserie({});
    }

    Buscar(form: FormControl) {

    }

    Abrir() {

    }

    Cerrar() {

    }

    Reactivar() {

    }

    Finalizar() {

    }

}
