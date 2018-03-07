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
import { Sandbox as TaskSandBox } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import { VariablesTareaDTO } from '../produccion-documental/models/StatusDTO';
import { UnidadDocumentalDTO } from '../../../domain/unidadDocumentalDTO';
import { ConfirmationService } from 'primeng/primeng';
import { PushNotificationAction } from '../../../infrastructure/state-management/notifications-state/notifications-actions';




export class StateUnidadDocumental implements TaskForm {

    ListadoUnidadDocumental: Observable<ListadoUnidadDocumentalModel[]>;
    ListadoSeries: Observable<SerieDTO[]>;
    ListadoSubseries: SubserieDTO[];
    UnidadDocumentalSeleccionada: DetalleUnidadDocumentalDTO;
    OpcionSeleccionada: number;
    AbrirDetalle: boolean;
    task: TareaDTO;
    taskVariables: VariablesTareaDTO = {};
    FechaExtremaFinal: Date;
    EsSubserieRequerido: boolean;
    NoUnidadesSeleccionadas = 'No hay unidades documentales seleccionadas';

    formBuscar: FormGroup;

    constructor(
        private fb: FormBuilder,
        private unidadDocumentalApiService: UnidadDocumentalApiService,
        private serieSubserieApiService: SerieSubserieApiService,
        private _store: Store<State>,
        private _dependenciaSandbox: Sandbox,
        private _taskSandBox: TaskSandBox,
        private confirmationService: ConfirmationService
    ) {
    }

    OnBlurEvents(control: string) {
        const formControl = this.formBuscar.get(control);
        if (control === 'serie') {
            this.GetSubSeries(formControl.value);
        } else if (control === 'subserie') {
            this.SubscribirSubserie();
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

    SeleccionarTodos(checked: boolean) {
        this.ListadoUnidadDocumental = this.ListadoUnidadDocumental.map((_map) => {
            const unidadesDocumentales = _map.reduce((listado, unidad) => {
                unidad.seleccionado = checked;
                listado.push(unidad);
                return listado;
            }, []);
            return unidadesDocumentales;
        });
    }

    GetUnidadesSeleccionadas(): ListadoUnidadDocumentalModel[] {
        let ListadoFiltrado = [];
        const UnidadesSeleccionadas = this.ListadoUnidadDocumental.subscribe(data => {
            ListadoFiltrado =  data.filter(item => item.seleccionado);
        });
        return ListadoFiltrado;
    }

    Abrir() {
        const unidadesSeleccionadas = this.GetUnidadesSeleccionadas();
        if (unidadesSeleccionadas.length) {

        } else {
            this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: this.NoUnidadesSeleccionadas}));
        }
    }

    Cerrar() {
        let unidadesSeleccionadas = this.GetUnidadesSeleccionadas();
        if (unidadesSeleccionadas.length) {
           const pendienteFechaYSoporte = unidadesSeleccionadas.find(item => (item.fechaExtremaFinal === null && item.soporte === null));
           if (pendienteFechaYSoporte) {
               this.confirmationService.confirm({
                message: `<p style="text-align: center">¿Desea que la fecha extrema final sea la misma fecha del cierre del trámite Aceptar / Cancelar. ?</p>`,
                accept: () => {
                    unidadesSeleccionadas = unidadesSeleccionadas.reduce((listado, current) => {
                        if (current.fechaExtremaFinal === null && current.soporte) {
                            current.fechaExtremaFinal = current.fechaCierreTramite;
                            listado.push(current);
                        }
                        return listado;
                    }, [])
                },
                reject: () => {

                }
              });
           }

        } else {
            this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: this.NoUnidadesSeleccionadas}));
        }
    }

    Reactivar() {
        const unidadesSeleccionadas = this.GetUnidadesSeleccionadas();
        if (unidadesSeleccionadas.length) {

        } else {
            this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: this.NoUnidadesSeleccionadas}));
        }
    }

    Finalizar() {
        this._taskSandBox.completeTaskDispatch({
            idProceso: this.task.idProceso,
            idDespliegue: this.task.idDespliegue,
            idTarea: this.task.idTarea,
            parametros: this.taskVariables
        });
    }

    save(): Observable<any> {
        return Observable.of(true).delay(5000);
    }

}
