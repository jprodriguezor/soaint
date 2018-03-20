import { Component, OnInit, ViewChild, AfterViewInit, AfterContentInit, ChangeDetectorRef } from '@angular/core';
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

@Component({
  selector: 'app-corregir-radicacion',
  templateUrl: './corregir-radicacion.component.html',
  styleUrls: ['./corregir-radicacion.component.scss'],
})
export class CorregirRadicacionComponent implements OnInit, AfterContentInit {

  @ViewChild('datosGenerales') datosGenerales;

  @ViewChild('datosRemitente') datosRemitente;

  @ViewChild('datosDestinatario') datosDestinatario;

  tabIndex = 0;

  formsTabOrder: Array<any> = [];

  formGenerales: FormGroup;
  formRemitente: FormGroup;

  task: TareaDTO;
  activeTaskUnsubscriber: Subscription;
  readonly = false;

  radicacion: ComunicacionOficialDTO;
  radicacionEntradaDTV: RadicacionEntradaDTV = null;
  radicacionEntradaFormData: RadicacionEntradaFormInterface = null;
  comunicacion: ComunicacionOficialDTO = {};


  destinatario: any = null;
  generales: any = null;
  remitente: any = null;
  descripcionAnexos: any;
  radicadosReferidos: any;
  agentesDestinatario: any;
  disabled = true;

  constructor(
    private _store: Store<State>,
    private _sandbox: RadicarComunicacionesSandBox,
    private _asiganacionSandbox: AsiganacionDTOSandbox,
    private _comunicacionOficialApi: CominicacionOficialSandbox,
    private _taskSandBox: TaskSandBox,
    private formBuilder: FormBuilder,
    private _produccionDocumentalApi: ProduccionDocumentalApiService,
    private messagingService: MessagingService,
    private _changeDetectorRef: ChangeDetectorRef
  ) {
  }

  ngOnInit() {
    this.Init();
  }

  ngAfterContentInit() {
    this.formsTabOrder.push(this.datosGenerales);
    this.formsTabOrder.push(this.datosRemitente);
    this.formsTabOrder.push(this.datosDestinatario);
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
      destinatario: this.destinatario,
      generales: this.generales,
      remitente: this.remitente,
      descripcionAnexos: this.descripcionAnexos,
      radicadosReferidos: this.radicadosReferidos,
      agentesDestinatario: this.agentesDestinatario
    };

    if (this.remitente.datosContactos) {
      payload.datosContactos = this.remitente.datosContactos.contacts;
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
        this.radicacionEntradaDTV = new RadicacionEntradaDTV(this.comunicacion);
        this.radicacionEntradaFormData = this.radicacionEntradaDTV.getRadicacionEntradaFormData();
        this.destinatario = this.radicacionEntradaFormData.destinatario;
        this.remitente = this.radicacionEntradaFormData.remitente;
        this.generales = this.radicacionEntradaFormData.generales;
        this.InitFormGenerales();
        this.InitFormRemitente();
        this.InitFormDestinatario();
        this._changeDetectorRef.detectChanges();
      });
    }
  }


  InitFormGenerales() {
      const reqDistFisica = (this.generales.reqDistFisica === 1);
      this.formGenerales = this.formBuilder.group({
        'fechaRadicacion': [this.generales.fechaRadicacion],
        'nroRadicado': [this.generales.nroRadicado],
        'tipoComunicacion': [{value: {codigo: 'EE', nombre: 'Comunicación Oficial Externa Recibida', codPadre: 'TP-CMC', id: 30}, disabled: this.disabled}, Validators.required],
        'medioRecepcion': [{value: {codigo: 'ME-RECV', nombre: 'Virtual', codPadre: 'ME-RECE', id: 18}, disabled: this.disabled}, Validators.required],
        'empresaMensajeria': [{value: this.generales.empresaMensajeria, disabled: this.disabled}, Validators.required],
        'numeroGuia': [{value: this.generales.numeroGuia, disabled: this.disabled}, Validators.compose([Validators.required, Validators.maxLength(8)])],
        'tipologiaDocumental': [{value: {codigo: 'TL-DOCOF', nombre: 'Oficio', codPadre: 'TL-DOC', id: 49}, disabled: !this.disabled}, Validators.required],
        'unidadTiempo': [{value: {codigo: 'UNID-TIH', nombre: 'Horas', codPadre: 'UNID-TI', id: 90}, disabled: this.disabled}],
        'numeroFolio': [{value: this.generales.numeroFolio, disabled: this.disabled}, Validators.required],
        'inicioConteo': [this.generales.inicioConteo],
        'reqDistFisica': [{value: reqDistFisica, disabled: this.disabled}],
        'reqDigit': [{value: this.generales.reqDigit, disabled: this.disabled}],
        'tiempoRespuesta': [{value: this.generales.tiempoRespuesta, disabled: this.disabled}],
        'asunto': [{value: this.generales.asunto, disabled: !this.disabled}, Validators.compose([Validators.required, Validators.maxLength(500)])],
        'radicadoReferido': [{value: this.generales.radicadoReferido, disabled: !this.disabled}],
        'tipoAnexos': [{value: null, disabled: this.disabled}],
        'soporteAnexos': [{value: null, disabled: this.disabled}],
        'tipoAnexosDescripcion': [{value: null}, Validators.maxLength(300)],
        'hasAnexos': [{value: this.generales.hasAnexos, disabled: this.disabled}]
      });
      this.datosGenerales.descripcionAnexos = this.radicacionEntradaFormData.descripcionAnexos;
      this.datosGenerales.form = this.formGenerales;
      this.datosRemitente.setTipoComunicacion(this.formGenerales.controls['tipoComunicacion'].value);
  }

  InitFormRemitente() {
    this.formRemitente = this.formBuilder.group({
      'tipoPersona': [{value: {codigo: 'TP-PERA', nombre: 'Anonimo', codPadre: '', id: 30}, disabled: this.disabled}, Validators.required],
        'nit': [{value: this.remitente.nit, disabled: this.disabled}],
        'actuaCalidad': [{value: this.remitente.actuaCalidad, disabled: this.disabled}],
        'tipoDocumento': [{value: this.remitente.tipoDocumento, disabled: this.disabled}],
        'razonSocial': [{value: this.remitente.razonSocial, disabled: this.disabled}, Validators.required],
        'nombreApellidos': [{value: this.remitente.nombreApellidos, disabled: this.disabled}, Validators.required],
        'nroDocumentoIdentidad': [{value: this.remitente.nroDocumentoIdentidad, disabled: this.disabled}],
        'sedeAdministrativa': [{value: this.remitente.sedeAdministrativa, disabled: this.disabled}, Validators.required],
        'dependenciaGrupo': [{value: this.remitente.dependenciaGrupo, disabled: this.disabled}, Validators.required],
    });
    this.datosRemitente.onSelectTipoPersona(this.formRemitente.controls['tipoPersona'].value);
    this.datosRemitente.datosContactos.contacts = this.radicacionEntradaFormData.datosContactos;
}

InitFormDestinatario() {
  this.datosDestinatario.agentesDestinatario = [...this.radicacionEntradaFormData.agentesDestinatario];
}

  actualizarComunicacion() {
    this._comunicacionOficialApi.actualizarComunicacion(this.comunicacion);
    this._taskSandBox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: {
      }
    });
    this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
  }

  HasDocuments() {
    if (this.comunicacion && this.comunicacion.ppdDocumentoList) {
      return (this.comunicacion.ppdDocumentoList[0].ideEcm) ? true : false;
    }
    return false;
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


}
