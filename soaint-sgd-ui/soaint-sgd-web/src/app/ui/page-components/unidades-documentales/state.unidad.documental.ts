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
import { EstadoUnidadDocumental } from './models/enums/estado.unidad.documental.enum';
import { MensajeRespuestaDTO } from '../../../domain/MensajeRespuestaDTO';
import { ChangeDetectorRef, Injectable, ApplicationRef } from '@angular/core';
import { isNullOrUndefined } from 'util';
import { ConstanteDTO } from '../../../domain/constanteDTO';
import { sedeDestinatarioEntradaSelector } from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {Subscription} from 'rxjs/Subscription';
import { DependenciaApiService } from '../../../infrastructure/api/dependencia.api';
import { DependenciaDTO } from '../../../domain/dependenciaDTO';

@Injectable()
export class StateUnidadDocumentalService implements TaskForm {

    ListadoUnidadDocumental: UnidadDocumentalDTO[] = [];
    unidadesSeleccionadas: UnidadDocumentalDTO[] = [];

    // dropdowns
    tiposDisposicionFinal$: Observable<ConstanteDTO[]> = Observable.of([]);
    sedes$: Observable<ConstanteDTO[]> = Observable.of([]);
    dependencias: DependenciaDTO[] = [];
    ListadoSeries: Observable<SerieDTO[]>;
    ListadoSubseries: SubserieDTO[];

    // generales
    UnidadDocumentalSeleccionada: DetalleUnidadDocumentalDTO;
    formBuscar: FormGroup;
    EsSubserieRequerido: boolean;
    NoUnidadesSeleccionadas = 'No hay unidades documentales seleccionadas';
    validations: any = {};
    subscribers: Array<Subscription> = [];

    // gestionar unidad documental
    OpcionSeleccionada: number;
    AbrirDetalle: boolean;
    FechaExtremaFinal: Date;
    MensajeIngreseFechaExtremaFinal = 'Por favor ingrese la fecha extrema final para proceder al cierre.';

    // tarea
    task: TareaDTO;
    taskVariables: VariablesTareaDTO = {};

    constructor(
        private fb: FormBuilder,
        private unidadDocumentalApiService: UnidadDocumentalApiService,
        private serieSubserieApiService: SerieSubserieApiService,
        private _store: Store<State>,
        private _dependenciaSandbox: Sandbox,
        private _taskSandBox: TaskSandBox,
        private confirmationService: ConfirmationService,
        private _appRef: ApplicationRef,
        private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
        private _dependenciaApiService: DependenciaApiService
    ) {
    }

    OnBlurEvents(control: string) {
        const formControl = this.formBuscar.get(control);
        if (formControl.touched && formControl.invalid) {
          const error_keys = Object.keys(formControl.errors);
          const last_error_key = error_keys[error_keys.length - 1];
          this.validations[control] = VALIDATION_MESSAGES[last_error_key];
        } else {
            this.validations[control] = '';
        }
    }


    InitForm(camposRequeridos: string[]) {
       this.formBuscar = this.fb.group({
        tipoDisposicionFinal: [null],
        sede: [null],
        dependencia: [null],
        serie: [null],
        subserie: [null],
        identificador: [''],
        nombre: [''],
        descriptor1: [''],
        descriptor2: [''],
       });
       this.SetFormCamposRequeridos(camposRequeridos);
    }

    ResetForm() {
        this.formBuscar.reset();
    }

    InitPropiedadesTarea() {
        this._store.select(getActiveTask).subscribe((activeTask) => {
            this.task = activeTask;
            if (this.task.variables.codDependencia) {
                const codDependencia = this.task.variables.codDependencia
                this.formBuscar.controls['dependencia'].setValue(codDependencia);
                this.GetListadosSeries();
                this.Listar();
            }
        });
    }

    SetFormCamposRequeridos(camposRequeridos: string[]) {
        if (camposRequeridos.length) {
            camposRequeridos.forEach(_control => {
                this.formBuscar.controls[_control].setValidators(Validators.required);
            });
            this.formBuscar.updateValueAndValidity();
        }
    }

    SetFormSubscriptions(controls: string[]) {
        if (controls.length) {
            controls.forEach(_control => {
                this.subscribers.push(
                this.formBuscar.get(_control).valueChanges.distinctUntilChanged().subscribe(value => {
                    if (value !== null) {
                        switch (_control) {
                            case 'sede' : this.GetListadoDependencias(value.id); break;
                            case 'dependencia': this.GetListadosSeries(); break;
                            case 'serie': this.GetSubSeries(value.codigo); break;
                        }
                    }
                  }))
            });
            this.formBuscar.updateValueAndValidity();
        }
    }

    GetDetalleUnidadUnidadDocumental(payload: any) {
        this.UnidadDocumentalSeleccionada = this.unidadDocumentalApiService.GetDetalleUnidadDocumental(payload);
        this.AbrirDetalle = true;
    }

    GetListadoTiposDisposicion() {
        this.tiposDisposicionFinal$ = Observable.of([]);
    }

    GetListadoSedes() {
        this._store.dispatch(new SedeAdministrativaLoadAction());
        this.sedes$ = this._store.select(sedeDestinatarioEntradaSelector);
    }

    GetListadoDependencias(sedeId: any) {
        this.formBuscar.controls['dependencia'].reset();
        this._dependenciaApiService.Listar({})
        .subscribe(resp => {
            this.dependencias = resp.filter(_item => _item.ideSede === sedeId);
        });
        ;
    }

    GetListadosSeries() {
        this.formBuscar.controls['serie'].reset();
        this.ListadoSeries = this.serieSubserieApiService.ListarSerieSubserie({
            idOrgOfc: this.formBuscar.controls['dependencia'].value,
        })
        .map(map => map.listaSerie);
        this.ListadoSubseries = [];
    }

    GetSubSeries(serie: string) {
        this.formBuscar.controls['subserie'].reset();
        if (serie) {
            this.serieSubserieApiService.ListarSerieSubserie({
                idOrgOfc: this.formBuscar.controls['dependencia'].value,
                codSerie: serie,
            })
            .map(map => map.listaSubSerie)
            .subscribe(result => {
                this.ListadoSubseries =  result;
                if (result) {
                    this.formBuscar.controls['subserie'].setValidators(Validators.required);
                    this.formBuscar.updateValueAndValidity();
                }
            });
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

    Listar(value?: any) {
        this.OpcionSeleccionada = (value) ? value : this.OpcionSeleccionada;
        this.unidadDocumentalApiService.Listar(this.GetPayload())
        .subscribe(response => {
            const ListadoMapeado =  response.reduce((_listado, _current) => {
                _current.estado = (_current.inactivo) ? 'Inactivo' : 'Activo';
                _current.seleccionado = true;
                switch (_current.soporte) {
                    case 'fisico': _current.soporte = 'Físico'; break;
                    case 'electronico': _current.soporte = 'Electrónico'; break;
                    case 'hibrido': _current.soporte = 'Híbrido'; break;
                }
                _listado.push(_current);
                return _listado;
            }, []);
            this.ListadoUnidadDocumental = [...ListadoMapeado];
        });
    }

    Agregar() {
        const unidadesSeleccionadas = this.GetUnidadesSeleccionadas();
        if (unidadesSeleccionadas.length) {
            const listadoMapeado = this.ListadoUnidadDocumental
            .reduce((listado, currenvalue: any) => {
                  const item = this.unidadesSeleccionadas.find(_item => _item.soporte === 'Físico' && _item.fechaCierre === null);
                  if (item) {
                      currenvalue.fechaCierre = this.FechaExtremaFinal;
                  }
                  listado.push(currenvalue);
                  return listado;
            }, []);
            this.ListadoUnidadDocumental = [...listadoMapeado];
        }
    }

    GetPayload(accionSeleccionada?: UnidadDocumentalAccion): UnidadDocumentalDTO {

        const payload: UnidadDocumentalDTO = {
            codigoDependencia: this.formBuscar.controls['dependencia'].value,
        }
        if (!isNullOrUndefined(accionSeleccionada)) {
            payload.accion = UnidadDocumentalAccion[accionSeleccionada]
        }

        if (this.formBuscar.controls['serie'].value) {
            payload.codigoSerie = this.formBuscar.controls['serie'].value;
        }
        if (this.formBuscar.controls['subserie'].value) {
            payload.codigoSubSerie = this.formBuscar.controls['subserie'].value;
        }
        if (this.formBuscar.controls['identificador'].value) {
            payload.codigoUnidadDocumental = this.formBuscar.controls['identificador'].value;
        }
        if (this.formBuscar.controls['nombre'].value) {
            payload.nombreUnidadDocumental = this.formBuscar.controls['nombre'].value;
        }
        if (this.formBuscar.controls['descriptor1'].value) {
            payload.descriptor1 = this.formBuscar.controls['descriptor1'].value;
        }
        if (this.formBuscar.controls['descriptor2'].value) {
            payload.descriptor1 = this.formBuscar.controls['descriptor2'].value;
        }

        return payload;
    }


    GetUnidadesSeleccionadas(): UnidadDocumentalDTO[] {
        const ListadoFiltrado = this.unidadesSeleccionadas
            if (!ListadoFiltrado.length) {
                this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: this.NoUnidadesSeleccionadas}));
            }
        return ListadoFiltrado;
    }

    Abrir() {
        const unidadesSeleccionadas = this.GetUnidadesSeleccionadas();
        if (unidadesSeleccionadas.length) {
            const payload = unidadesSeleccionadas.map(_map => { return { id: _map.id, } });
            this.unidadDocumentalApiService.abrirUnidadesDocumentales(payload)
            .subscribe(response => {
                this.ManageActionResponse(response);
            });
        }
    }

    Cerrar() {
        const unidadesSeleccionadas: UnidadDocumentalDTO[] = this.GetUnidadesSeleccionadas();
        let payload = {};
        if (unidadesSeleccionadas.length) {
           const pendienteFechaYSoporte = unidadesSeleccionadas.find(item => (item.fechaExtremaFinal === null && item.soporte === 'Físico'));
           if (pendienteFechaYSoporte) {
               this.confirmationService.confirm({
                message: `<p style="text-align: center">¿Desea que la fecha extrema final sea la misma fecha del cierre del trámite Aceptar / Cancelar. ?</p>`,
                accept: () => {
                    payload = unidadesSeleccionadas.reduce((listado, current) => {
                        if (current.fechaExtremaFinal === null && current.soporte === 'fisico') {
                            const item = {
                                id: current.id,
                                fechaExtremaFinal: current.fechaCierre,
                            }
                            listado.push(current);
                        }
                        return listado;
                    }, []);
                    this.SubmitCerrar(payload)
                },
                reject: () => {
                    this._store.dispatch(new PushNotificationAction({severity: 'info', summary: this.MensajeIngreseFechaExtremaFinal}));
                }
              });
           } else {
                 payload = unidadesSeleccionadas.map(_map => { return { idUnidadDocumental: _map.id, } });
                 this.SubmitCerrar(payload);
           }

        }
    }

    SubmitCerrar(payload: any) {
        this.unidadDocumentalApiService.cerrarUnidadesDocumentales(payload)
        .subscribe(response => {
            this.ManageActionResponse(response);
        });
    }

    Reactivar() {
        const unidadesSeleccionadas = this.GetUnidadesSeleccionadas();
        if (unidadesSeleccionadas.length) {
            const payload = unidadesSeleccionadas.map(_map => { return { id: _map.id, } });
            this.unidadDocumentalApiService.reactivarUnidadesDocumentales(payload)
            .subscribe(response => {
               this.ManageActionResponse(response);
            });
        }
    }

    ManageActionResponse(response: MensajeRespuestaDTO) {
        const mensajeRespuesta: MensajeRespuestaDTO = response;
        const mensajeSeverity = (mensajeRespuesta.codMensaje === '0000') ? 'success' : 'error';
        this.Listar();
        this._store.dispatch(new PushNotificationAction({severity: mensajeSeverity, summary: mensajeRespuesta.mensaje}));
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
