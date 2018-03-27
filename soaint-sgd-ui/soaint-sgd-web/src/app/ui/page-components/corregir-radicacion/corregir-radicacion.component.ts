import { Component, OnInit, AfterViewInit, AfterContentInit, ChangeDetectorRef, OnDestroy, AfterViewChecked } from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormControl} from '@angular/forms';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {Subscription} from 'rxjs/Subscription';
import {Observable} from 'rxjs/Observable';
import {TareaDTO} from '../../../domain/tareaDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Sandbox as AsiganacionDTOSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {RadicacionEntradaDTV} from '../../../shared/data-transformers/radicacionEntradaDTV';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {getArrayData as getFuncionarioArrayData, getAuthenticatedFuncionario, getSelectedDependencyGroupFuncionario} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';
import {ROUTES_PATH} from '../../../app.route-names';
import {go} from '@ngrx/router-store';
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Sandbox as CominicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';
import { ProduccionDocumentalApiService, MessagingService } from '../../../infrastructure/__api.include';
import { VersionDocumentoDTO } from '../produccion-documental/models/DocumentoDTO';
import { PushNotificationAction } from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import { RadicacionEntradaFormInterface } from '../../../shared/interfaces/data-transformers/radicacionEntradaForm.interface';
import { DatosGeneralesStateService } from '../../bussiness-components/datos-generales-edit/datos-generales-state-service';
import { DatosRemitenteStateService } from '../../bussiness-components/datos-remitente-edit/datos-remitente-state-service';
import { DatosDestinatarioStateService } from '../../bussiness-components/datos-destinatario-edit/datos-destinatario-state-service';
import { ComunicacionOficialEntradaDTV } from '../../../shared/data-transformers/comunicacionOficialEntradaDTV';
import { CorrespondenciaApiService } from '../../../infrastructure/api/correspondencia.api';
import { AnexoDTO } from '../../../domain/anexoDTO';
import { getDestinatarioPrincial } from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';

@Component({
  selector: 'app-corregir-radicacion',
  templateUrl: './corregir-radicacion.component.html',
})
export class CorregirRadicacionComponent implements OnInit, OnDestroy {

  tabIndex = 0;

  formsTabOrder: Array<any> = [];

  formGenerales: FormGroup;
  formRemitente: FormGroup;

  task: TareaDTO;
  activeTaskUnsubscriber: Subscription;
  readonly = false;

  radicacionEntradaFormData: RadicacionEntradaFormInterface = null;
  comunicacion: ComunicacionOficialDTO = {};

  descripcionAnexos: any;
  radicadosReferidos: any;
  agentesDestinatario: any;
  disabled = true;

  stateGenerales: DatosGeneralesStateService = null;
  stateRemitente: DatosRemitenteStateService = null;
  stateDestinatario: DatosDestinatarioStateService = null;

  constructor(
    private _store: Store<State>,
    private _sandbox: RadicarComunicacionesSandBox,
    private _asiganacionSandbox: AsiganacionDTOSandbox,
    private _comunicacionOficialApi: CominicacionOficialSandbox,
    private _taskSandBox: TaskSandBox,
    private formBuilder: FormBuilder,
    private _produccionDocumentalApi: ProduccionDocumentalApiService,
    private messagingService: MessagingService,
    private _changeDetectorRef: ChangeDetectorRef,
    private _generalesStateService: DatosGeneralesStateService,
    private _remitenteStateService: DatosRemitenteStateService,
    private _destinatarioStateService: DatosDestinatarioStateService,
    private _correspondenciaService: CorrespondenciaApiService
  ) {
    this.stateGenerales = this._generalesStateService;
    this.stateRemitente = this._remitenteStateService;
    this.stateDestinatario = this._destinatarioStateService;
  }

  ngOnInit() {
    this.Init();
  }

  openNext() {
    this.tabIndex = (this.tabIndex === 2) ? 0 : this.tabIndex + 1;
  }

  openPrev() {
    this.tabIndex = (this.tabIndex === 0) ? 2 : this.tabIndex - 1;
  }

  updateTabIndex(event) {
    this.tabIndex = event.index;
  }

  Init() {
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
        this.task = activeTask;
        this.restore();
    });
  }

  abort() {
    this._taskSandBox.abortTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      instanciaProceso: this.task.idInstanciaProceso
    });
  }

  save(): Observable<any> {
    const payload: any = {
      destinatario: this.stateDestinatario.form,
      generales: this.stateGenerales.form,
      remitente: this.stateRemitente.form,
      descripcionAnexos: this.stateGenerales.descripcionAnexos,
      radicadosReferidos: this.stateGenerales.radicadosReferidos,
      agentesDestinatario: this.stateDestinatario.agentesDestinatario
    };
    if (this.radicacionEntradaFormData.datosContactos) {
      payload.datosContactos = this.stateRemitente.contacts;
    }
    const tareaDto: any = {
      idTareaProceso: this.task.idTarea,
      idInstanciaProceso: this.task.idInstanciaProceso,
      payload: payload
    };
    return this._sandbox.quickSave(tareaDto);
  }

  restore() {
    if (this.task) {
      this._asiganacionSandbox.obtenerComunicacionPorNroRadicado(this.task.variables.numeroRadicado)
      .subscribe((result) => {
        this.comunicacion = result;
        const radicacionEntradaDTV: RadicacionEntradaDTV = new RadicacionEntradaDTV(this.comunicacion);
        this.radicacionEntradaFormData = radicacionEntradaDTV.getRadicacionEntradaFormData();
        this.InitGenerales();
        this.InitRemitente();
        this.InitDestinatario();
        setTimeout(() => {
          this._changeDetectorRef.detectChanges();
        }, 0);
      });
    }
  }

  InitGenerales() {
    this.stateGenerales.dataform = this.radicacionEntradaFormData.generales;
    // this._correspondenciaService.ListarAnexos(this.task.variables.numeroRadicado)
    // .subscribe(_response => {
    //   this.stateGenerales.descripcionAnexos = _response.map(_map => {
    //     return {
    //       tipoAnexo: {nombre: _map.descTipoAnexo},
    //       soporteAnexo: {nombre: _map.descTipoSoporte},
    //       descripcion: _map.descripcion,
    //     }
    //   });
    // });
    this.stateGenerales.ppdDocumentoList = this.comunicacion.ppdDocumentoList;
    this.stateGenerales.radicadosReferidos = this.radicacionEntradaFormData.radicadosReferidos;
    this.stateGenerales.Init();
    this.formsTabOrder.push(this.stateGenerales.form);
  }

  InitRemitente() {
    this.stateRemitente.tipoComunicacion = this.radicacionEntradaFormData.generales.tipoComunicacion;
    this.stateRemitente.dataform = this.radicacionEntradaFormData.remitente;
    this.stateRemitente.contacts = this.radicacionEntradaFormData.datosContactos;
    this.stateRemitente.Init();
    this.formsTabOrder.push(this.stateRemitente.form);
  }

  InitDestinatario() {
    this.stateDestinatario.agentesDestinatario =  [...this.radicacionEntradaFormData.agentesDestinatario];
    this.stateDestinatario.Init();
    this.formsTabOrder.push(this.stateDestinatario.form);
  }

  actualizarComunicacion() {
    const payload = this.GetComunicacionPayload();
    this._comunicacionOficialApi.actualizarComunicacion(payload);
    this._taskSandBox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: {
      }
    });
    this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
  }

  GetComunicacionPayload(): any {
    const radicacionEntradaFormPayload: any = {
      destinatario: this.stateDestinatario.form.value,
      generales: this.stateGenerales.form.value,
      remitente: this.stateRemitente.form.value,
      descripcionAnexos: this.stateGenerales.descripcionAnexos,
      radicadosReferidos: this.stateGenerales.radicadosReferidos,
      agentesDestinatario: this.stateDestinatario.agentesDestinatario,
      task: this.task
    };
    if (this.stateRemitente.contacts) {
      radicacionEntradaFormPayload.datosContactos = this.stateRemitente.contacts;
    }
    const comunicacionOficialDTV = new ComunicacionOficialEntradaDTV(radicacionEntradaFormPayload, this._store);

    return comunicacionOficialDTV.getComunicacionOficial();
  }

  HasDocuments() {
    if (this.comunicacion && this.comunicacion.ppdDocumentoList) {
      return (this.comunicacion.ppdDocumentoList[0].ideEcm) ? true : false;
    }
    return false;
  }

  IsDisabled() {
    return (this.formsTabOrder[this.tabIndex] && this.formsTabOrder[this.tabIndex].valid);
  }

  uploadVersionDocumento(doc: VersionDocumentoDTO) {
    const formData = new FormData();
    formData.append('documento', doc.file, doc.nombre);
    if (doc.id) {
        formData.append('idDocumento', doc.id);
    }
    formData.append('selector', 'PD');
    formData.append('nombreDocumento', doc.nombre);
    formData.append('tipoDocumento', doc.tipo);
    formData.append('sede', this.task.variables.nombreSede);
    formData.append('dependencia', this.task.variables.nombreDependencia);
    formData.append('nroRadicado', this.task.variables && this.task.variables.numeroRadicado || null);
    this._produccionDocumentalApi.subirVersionDocumento(formData).subscribe(
    resp => {
      if ('0000' === resp.codMensaje) {
        this._store.dispatch(new PushNotificationAction({severity: 'success', summary: resp.mensaje}))
        return resp.documentoDTOList[0];
      } else {
        this._store.dispatch(new PushNotificationAction({severity: 'error', summary: resp.mensaje}));
        return doc
      }
    },
    error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error}))
  );
}

ngOnDestroy() {
  this._changeDetectorRef.detach();
}


}
