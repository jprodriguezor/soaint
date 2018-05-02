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
import { TaskForm } from 'app/shared/interfaces/task-form.interface';
import { TareaDTO } from 'app/domain/tareaDTO';
import { Sandbox } from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import { getActiveTask } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import { async } from '@angular/core/testing';
import { Sandbox as TaskSandBox } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import { UnidadDocumentalDTO } from '../../domain/unidadDocumentalDTO';
import { ConfirmationService } from 'primeng/primeng';
import { PushNotificationAction } from '../../infrastructure/state-management/notifications-state/notifications-actions';
import { MensajeRespuestaDTO } from 'app/domain/MensajeRespuestaDTO';
import { ChangeDetectorRef, Injectable, ApplicationRef } from '@angular/core';
import { isNullOrUndefined } from 'util';
import { ConstanteDTO } from 'app/domain/constanteDTO';
import { sedeDestinatarioEntradaSelector } from '../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {Subscription} from 'rxjs/Subscription';
import { DependenciaApiService } from '../../infrastructure/api/dependencia.api';
import { DependenciaDTO } from 'app/domain/dependenciaDTO';
import {Subject} from 'rxjs/Subject';

@Injectable()
export class StateUnidadDocumentalService {

    ListadoUnidadDocumental: UnidadDocumentalDTO[] = [];
    unidadesSeleccionadas: UnidadDocumentalDTO[] = [];

    // 
    private ListadoActualizadoSubject = new Subject<void>();
    public ListadoActualizado$ = this.ListadoActualizadoSubject.asObservable();

    // dropdowns
    tiposDisposicionFinal$: Observable<ConstanteDTO[]> = Observable.of([]);
    sedes$: Observable<ConstanteDTO[]> = Observable.of([]);
    dependencias: DependenciaDTO[] = [];
    ListadoSeries: SerieDTO[];
    ListadoSubseries: SubserieDTO[];

    // generales
    UnidadDocumentalSeleccionada: UnidadDocumentalDTO;
    EsSubserieRequerido: boolean;
    NoUnidadesSeleccionadas = 'No hay unidades documentales seleccionadas';
    validations: any = {};
    subscribers: Array<Subscription> = [];

    // gestionar unidad documental
    OpcionSeleccionada: number;
    AbrirDetalle: boolean;
    FechaExtremaFinal: Date;
    MensajeIngreseFechaExtremaFinal = 'Por favor ingrese la fecha extrema final para proceder al cierre.';

    constructor(
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

    GetDetalleUnidadUnidadDocumental(index: string) {
        const unidadDocumentalIndex = this.ListadoUnidadDocumental[index];
        this.unidadDocumentalApiService.GetDetalleUnidadDocumental(unidadDocumentalIndex.id)
        .subscribe(response => {
            this.UnidadDocumentalSeleccionada = response;
            this.AbrirDetalle = true;
        });
    }

    GetListadoTiposDisposicion() {
        this.tiposDisposicionFinal$ = Observable.of([]);
    }

    GetListadoSedes() {
        this._store.dispatch(new SedeAdministrativaLoadAction());
        this.sedes$ = this._store.select(sedeDestinatarioEntradaSelector);
    }

    GetListadoDependencias(sedeId: any) {
        this._dependenciaApiService.Listar({})
        .subscribe(resp => {
            this.dependencias = resp.filter(_item => _item.ideSede === sedeId);
        });
    }

    GetListadosSeries(codigodependencia): Observable<SerieDTO[]> {
        return this.serieSubserieApiService.ListarSerieSubserie({
            idOrgOfc: codigodependencia,
        })
        .map(map => map.listaSerie);
    }

    GetSubSeries(codigoserie: string, codigodependencia: string): Observable<SubserieDTO[]>{
        this.ListadoSubseries = [];
          return this.serieSubserieApiService.ListarSerieSubserie({
                idOrgOfc: codigodependencia,
                codSerie: codigoserie,
            })
            .map(map => map.listaSubSerie)
    }



    CerrarDetalle() {
        this.AbrirDetalle = false;
    }

    Listar(payload?: UnidadDocumentalDTO, value?: any) {
        this.unidadesSeleccionadas = [];
        this.unidadDocumentalApiService.Listar(payload)
        .subscribe(response => {
            let ListadoMapeado =  [];
            if(response.length) {
                ListadoMapeado = response.reduce((_listado, _current) => {
                    _current.seleccionado = true;
                    switch (_current.soporte) {
                        case 'fisico': _current.soporte = 'Físico'; break;
                        case 'electronico': _current.soporte = 'Electrónico'; break;
                        case 'hibrido': _current.soporte = 'Híbrido'; break;
                    }
                    _listado.push(_current);
                    return _listado;
                }, []);
               
            } 
            this.ListadoUnidadDocumental = [...ListadoMapeado]; 
            this.ListadoActualizadoSubject.next();           
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
            const payload = unidadesSeleccionadas.map(_map => { return { id: _map.id, accion: 'abrir' } });
            this.GestionarUnidadesDocumentales(payload);
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
                                accion: 'cerrar'
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
                 payload = unidadesSeleccionadas.map(_map => { return { id: _map.id, accion: 'cerrar' } });
                 this.SubmitCerrar(payload);
           }

        }
    }

    SubmitCerrar(payload: any) {
        this.GestionarUnidadesDocumentales(payload);
    }

    Reactivar() {
        const unidadesSeleccionadas = this.GetUnidadesSeleccionadas();
        if (unidadesSeleccionadas.length) {
            const payload = unidadesSeleccionadas.map(_map => { return { id: _map.id, accion: 'reactivar'} });
            this.GestionarUnidadesDocumentales(payload);
        }
    }

    GestionarUnidadesDocumentales(payload: any) {
        this.unidadDocumentalApiService.gestionarUnidadesDocumentales(payload)
        .subscribe(response => {
           this.ManageActionResponse(response);
        });
    }

    ManageActionResponse(response: MensajeRespuestaDTO) {
        const mensajeRespuesta: MensajeRespuestaDTO = response;
        const mensajeSeverity = (mensajeRespuesta.codMensaje === '0000') ? 'success' : 'error';
        this.Listar();
        this._store.dispatch(new PushNotificationAction({severity: mensajeSeverity, summary: mensajeRespuesta.mensaje}));
    }



}
