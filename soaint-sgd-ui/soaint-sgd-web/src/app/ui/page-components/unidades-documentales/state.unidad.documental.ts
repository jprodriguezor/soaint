import { ListadoUnidadDocumentalModel } from 'app/ui/page-components/unidades-documentales/models/listado.unidad.documental.model';
import { FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import { UnidadDocumentalApiService } from 'app/infrastructure/api/unidad-documental.api';
import { Observable } from 'rxjs/Observable';
import { Store } from '@ngrx/store';
import { State } from 'app/infrastructure/redux-store/redux-reducers';
import { SerieSubserieApiService } from 'app/infrastructure/api/serie-subserie.api';
import { SerieDTO } from 'app/domain/serieDTO';
import { SubserieDTO } from 'app/domain/subserieDTO';
import { UnidadDocumentalAccion } from 'app/ui/page-components/unidades-documentales/models/enums/unidad.documental.accion.enum';
import { DetalleUnidadDocumentalDTO } from 'app/ui/page-components/unidades-documentales/models/DetalleUnidadDocumentalDTO';
import { TaskForm } from 'app/shared/interfaces/task-form.interface';
import { TareaDTO } from 'app/domain/tareaDTO';
import { Sandbox } from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import { getActiveTask } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import { async } from '@angular/core/testing';
import { SelectItem } from 'primeng/components/common/selectitem';



export class StateUnidadDocumental implements TaskForm {

    ListadoUnidadDocumental: Observable<ListadoUnidadDocumentalModel[]>;
    ListadoSeries: Observable<SerieDTO[]>;
    ListadoSubseries: SubserieDTO[];
    UnidadDocumentalSeleccionada: DetalleUnidadDocumentalDTO;
    OpcionSeleccionada: number;
    AbrirDetalle: boolean;
    task: TareaDTO;
    FechaExtremaInicial: Date;
    EsSubserieRequerido: boolean;

    formBuscar: FormGroup;

    constructor(
        private fb: FormBuilder,
        private unidadDocumentalApiService: UnidadDocumentalApiService,
        private serieSubserieApiService: SerieSubserieApiService,
        private _store: Store<State>,
        private _dependenciaSandbox: Sandbox
    ) {
    }

    OnBlurEvents(control: string) {
        const formControl = this.formBuscar.get(control);
        if (control === 'serie') {
            this.GetSubSeries(formControl.value);
        }
    }

    InitData() {
        this.InitForm();
        this.OpcionSeleccionada = 1 // abrir
        this.InitPropiedadesTarea();
    }

    InitForm() {
       this.formBuscar = this.fb.group({
        serie: [null, [Validators.required]],
        subserie: [null],
        identificador: [''],
        nombre: [''],
        descriptor1: [''],
        descriptor2: [''],
        accion: [''],
       });
    }

    ResetForm() {
        this.formBuscar.reset();
    }

    InitPropiedadesTarea() {
        this._store.select(getActiveTask).subscribe((activeTask) => {
            this.task = activeTask;
            if (this.task.variables.codDependencia) {
                const codDependencia = this.task.variables.codDependencia
                this.GetListadosSeries(codDependencia);
                this.GetListadoUnidadesDocumentales(codDependencia);
            }
        });
    }

    GetListadoUnidadesDocumentales(codDependencia: string) {
        this.ListadoUnidadDocumental = this.unidadDocumentalApiService.Listar({idOrgOfc: codDependencia});
    }

    GetDetalleUnidadUnidadDocumental(payload: any) {
        this.UnidadDocumentalSeleccionada = this.unidadDocumentalApiService.GetDetalleUnidadDocumental(payload);
        this.AbrirDetalle = true;
    }

    GetListadosSeries(codDependencia: string) {
        this.ListadoSeries = this.serieSubserieApiService.ListarSerieSubserie({
            idOrgOfc: codDependencia,
        })
        .map(map => map.listaSerie);
        this.ListadoSubseries = [];
    }

    GetSubSeries(serie: string) {
        if (serie) {
            this.serieSubserieApiService.ListarSerieSubserie({
                idOrgOfc: this.task.variables.codDependencia,
                codSerie: serie,
            })
            .map(map => map.listaSubSerie)
            .subscribe(result => {
                this.ListadoSubseries =  result;
            })
        }
    }

    HabilitarOpcion(opcion: number) {
        if (UnidadDocumentalAccion[this.OpcionSeleccionada] === UnidadDocumentalAccion[opcion]) {
            return true;
        }
        return false;
    }

    SubscribirSubserie() {
         const length = (this.ListadoSubseries) ? this.ListadoSubseries.length : 0;
         if ((length) && (!this.formBuscar.controls['subserie'].value)) {
           this.formBuscar.controls['subserie'].setErrors({incorrect: true});
           this.EsSubserieRequerido = true;
         } else {
           this.EsSubserieRequerido = false;
         }
    }

    EsRequerido() {
        return this.EsSubserieRequerido;
    }

    CerrarDetalle() {
        this.AbrirDetalle = false;
    }

    Buscar(form: FormControl) {

    }

    Agregar() {

    }

    Abrir() {

    }

    Cerrar() {

    }

    Reactivar() {

    }

    Finalizar() {

    }

    save(): Observable<any> {
        return Observable.of(true).delay(5000);
    }

}
