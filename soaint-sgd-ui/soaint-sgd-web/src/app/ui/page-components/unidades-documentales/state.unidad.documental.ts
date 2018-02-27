import { ListadoUnidadDocumentalModel } from 'app/ui/page-components/unidades-documentales/models/listado.unidad.documental.model';
import { FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import { UnidadDocumentalApiService } from 'app/infrastructure/api/unidad-documental.api';
import { Observable } from 'rxjs/Observable';



export class StateUnidadDocumental {

    ListadoUnidadDocumental: ListadoUnidadDocumentalModel[] = [];
    ListadoSeries: Observable<any>;
    ListadoSubseries: Observable<any>;

    formBuscar: FormGroup;

    constructor(
        private fb: FormBuilder,
        private unidadDocumentalApiService: UnidadDocumentalApiService
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
        this.ListadoSeries = Observable.empty<Response>();
        this.ListadoSubseries = Observable.empty<Response>();
    }

    GetSubSeries(serie: string) {
        this.ListadoSubseries = Observable.empty<Response>();
    }

    Buscar(form: FormControl) {

    }

}
