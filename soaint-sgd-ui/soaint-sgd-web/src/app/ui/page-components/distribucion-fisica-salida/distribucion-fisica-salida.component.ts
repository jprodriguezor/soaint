import {Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation, ChangeDetectorRef} from '@angular/core';
import {Sandbox as DistribucionFisicaSandbox} from '../../../infrastructure/state-management/distrubucionFisicaDTO-state/distrubucionFisicaDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {getArrayData as getFuncionarioArrayData, getAuthenticatedFuncionario, getSelectedDependencyGroupFuncionario} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {getArrayData as DistribucionArrayData} from '../../../infrastructure/state-management/distrubucionFisicaDTO-state/distrubucionFisicaDTO-selectors';
import {Sandbox as FuncionarioSandBox} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {Subscription} from 'rxjs/Subscription';
import {AgentDTO} from '../../../domain/agentDTO';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {getTipologiaDocumentalArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/tipologia-documental-selectors';
import {RadicacionEntradaDTV} from '../../../shared/data-transformers/radicacionEntradaDTV';
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {DocumentoDTO} from '../../../domain/documentoDTO';
import {PlanillasApiService} from '../../../infrastructure/api/planillas.api';
import {PlanillaDTO} from '../../../domain/PlanillaDTO';
import {PlanAgentesDTO} from '../../../domain/PlanAgentesDTO';
import {PlanAgenDTO} from '../../../domain/PlanAgenDTO';
import {Sandbox as ProcessSandbox} from '../../../infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox';
import {correspondenciaEntrada} from "../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors";
import { VALIDATION_MESSAGES } from '../../../shared/validation-messages';
import { TaskForm } from '../../../shared/interfaces/task-form.interface';
import { TareaDTO } from '../../../domain/tareaDTO';
import { VariablesTareaDTO } from '../produccion-documental/models/StatusDTO';
import { AbortTaskAction } from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';
import { getActiveTask } from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import { CorrespondenciaApiService } from '../../../infrastructure/api/correspondencia.api';
import index from '@angular/cli/lib/cli';
import { isNullOrUndefined } from 'util';
import { PushNotificationAction } from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import { Sandbox as TaskSandBox } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import { afterTaskComplete } from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers';
import { getModalidadCorreoArrayData } from '../../../infrastructure/state-management/constanteDTO-state/selectors/modalidad-correo-selectors';
import { getClaseEnvioArrayData } from '../../../infrastructure/state-management/constanteDTO-state/selectors/clase-envio-selectors';
import { ROUTES_PATH } from '../../../app.route-names';
import { go } from '@ngrx/router-store';



@Component({
  selector: 'app-distribucion-fisica-salida',
  templateUrl: './distribucion-fisica-salida.component.html',
  styleUrls: ['./distribucion-fisica-salida.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class DistribucionFisicaSalidaComponent implements OnInit, OnDestroy {

  form: FormGroup;

  comunicaciones$: Observable<ComunicacionOficialDTO[]>;

  listadoComunicaciones: ComunicacionOficialDTO[] = [];

  selectedComunications: ComunicacionOficialDTO[] = [];

  start_date: Date = new Date();

  end_date: Date = new Date();

  dependencia: any;

  planillaGenerada: PlanillaDTO;

  numeroPlanillaDialogVisible: boolean = false;

  funcionariosSuggestions$: Observable<FuncionarioDTO[]>;

  modalidadCorreo: ConstanteDTO[];

  claseEnvio: ConstanteDTO[];

  dependenciaSelected$: Observable<DependenciaDTO>;

  dependenciaSelected: DependenciaDTO;

  funcionarioLog: FuncionarioDTO;

  funcionarioSubcription: Subscription;

  comunicacionesSubcription: Subscription;

  tipologiaDocumentalSuggestions$: Observable<ConstanteDTO[]>;

  tipologiasDocumentales: ConstanteDTO[];

  dependencias: DependenciaDTO[] = [];

  comunicacionSeleccionada: ComunicacionOficialDTO;

  informacionEnvioDialogVisible = false;

  detalleDialogVisible = false;

  validations: any = {};


  @ViewChild('popUpInformacionEnvio') popUpInformacionEnvio;

  @ViewChild('detallesView') detallesView;

  @ViewChild('popUpPlanillaGenerada') popUpPlanillaGenerada;

  downloadName: string;

  constructor(private _store: Store<RootState>,
              private _distribucionFisicaApi: DistribucionFisicaSandbox,
              private _funcionarioSandbox: FuncionarioSandBox,
              private _constSandbox: ConstanteSandbox,
              private _dependenciaSandbox: DependenciaSandbox,
              private _planillaService: PlanillasApiService,
              private correspondenciaApiService: CorrespondenciaApiService,
              private _processSandbox: ProcessSandbox,
              private formBuilder: FormBuilder,
              private _detectChanges: ChangeDetectorRef,
              private _taskSandBox: TaskSandBox,
            ) {



  }

  ngOnInit() {
    this.start_date.setHours(this.start_date.getHours() - 24);
    this._funcionarioSandbox.loadAllFuncionariosDispatch();
    this._constSandbox.loadDatosGeneralesDispatch();
    this.InitDataForm();
    // this.InitPropiedadesTarea();
    this.InitSubscriptions();
    this.initForm();
  }

  InitSubscriptions() {
    this.funcionariosSuggestions$ = this._store.select(getFuncionarioArrayData); 
    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);


    this.tipologiaDocumentalSuggestions$ = this._store.select(getTipologiaDocumentalArrayData);
    this.tipologiaDocumentalSuggestions$.subscribe((results) => {
      this.tipologiasDocumentales = results;
    });  
    this.funcionarioSubcription = this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });
    this.dependenciaSelected$.subscribe((result) => {
      this.dependenciaSelected = result;
    });
  }


  getDatosRemitente(comunicacion): Observable<AgentDTO> {
    const radicacionEntradaDTV = new RadicacionEntradaDTV(comunicacion);
    return radicacionEntradaDTV.getDatosRemitente();
  }

  getDatosDestinatario(comunicacion): Observable<AgentDTO[]> {
      const radicacionEntradaDTV = new RadicacionEntradaDTV(comunicacion);
      const destinatarioDTV = radicacionEntradaDTV.getDatosDestinatarios();
      return destinatarioDTV;
  }

  getDatosDestinatarioInmediate(comunicacion): AgentDTO[] {
    const radicacionEntradaDTV = new RadicacionEntradaDTV(comunicacion);
    return radicacionEntradaDTV.getDatosDestinatarioInmediate();
  }

  getDatosDocumentos(comunicacion): Observable<DocumentoDTO[]> {
    const radicacionEntradaDTV = new RadicacionEntradaDTV(comunicacion);
    return radicacionEntradaDTV.getDatosDocumento();
  }

  ngOnDestroy() {
    this.funcionarioSubcription.unsubscribe();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'claseEnvio': [null, [Validators.required]],
      'modalidadCorreo': [null, [Validators.required]],
      'dependencia': [null, [Validators.required]],
      'nroRadicado': [null],
      'tipologia': [null],
    });
  }

  OnBlurEvents(control: string) {
    this.SetValidationMessages(control);
  }

  SetValidationMessages(control: string) {
    const formControl = this.form.get(control);
    if (formControl.touched && formControl.invalid) {
      const error_keys = Object.keys(formControl.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    } else {
        this.validations[control] = '';
    }
  }

  ResetForm() {
    this.form.reset();
    this.listadoComunicaciones = [];
  }

  InitDataForm() {
    this._dependenciaSandbox.loadDependencies({})
    .subscribe((results) => {
      this.dependencias = results.dependencias;
    });
    this._store.select(getModalidadCorreoArrayData)
    .subscribe(response => {
      this.modalidadCorreo = response;
    });
    this._store.select(getClaseEnvioArrayData)
    .subscribe(response => {
      this.claseEnvio = response;
    });
  }

  convertDate(inputFormat) {
    function pad(s) {
      return (s < 10) ? '0' + s : s;
    }
    const d = new Date(inputFormat);
    return [pad(d.getFullYear()), pad(d.getMonth() + 1), d.getDate()].join('-');
  }

  findTipoDoc(code): string {
    return this.tipologiasDocumentales.find((element) => element.codigo == code).nombre;
  }

  findDependency(code): string {
    const result = this.dependencias.find((element) => element.codigo == code);
    return result ? result.nombre : '';
  }

  findSede(code): string {
    const result = this.dependencias.find((element) => element.codSede == code);
    return result ? result.nomSede : '';
  }

  listarDistribuciones() {
    this.comunicaciones$ =  this.correspondenciaApiService.ListarComunicacionesSalidaDistibucionFisica(this.GetPayload());
    this.comunicaciones$.subscribe(response => {
      this.listadoComunicaciones = [];
      this.listadoComunicaciones = [...response];
      this.selectedComunications = [];
    });

  }

  GetPayload(): any {
    const payload: any = {
      fecha_ini: this.convertDate(this.start_date),
      fecha_fin: this.convertDate(this.end_date),
      clase_envio: this.form.get('claseEnvio').value.codigo,
      mod_correo: this.form.get('modalidadCorreo').value.codigo,
    };   
    if (this.form.value.dependencia) {
      payload.cod_dep = this.form.get('dependencia').value.codigo;
    }
    if (this.form.value.tipologia) {
      payload.cod_tipo_doc = this.form.get('tipologia').value.codigo;
    }
    if (this.form.value.nroRadicado) {
      payload.nro_radicado = this.form.get('nroRadicado').value;
    }
    return payload;
  }

  generarDatosExportar(): PlanillaDTO {
    const agensDTO: PlanAgenDTO[] = [];

    this.selectedComunications.forEach((element) => {
       const agenDTO: PlanAgenDTO = {
        idePlanAgen: null,
        estado: null,
        varPeso: null,
        varValor: null,
        varNumeroGuia: null,
        fecObservacion: null,
        codNuevaSede: null,
        codNuevaDepen: null,
        observaciones: null,
        usuario: null,
        codCauDevo: null,
        fecCarguePla: null,
        ideAgente: this.getDatosDestinatarioInmediate(element)[0].ideAgente,
        ideDocumento: element.correspondencia.ideDocumento,
        nroRadicado: null,
        tipoPersona: null,
        tipologiaDocumental: null,
        nit: null,
        nroDocuIdentidad: null,
        nombre: null,
        razonSocial: null,
        folios: null,
        anexos: null
      };
      delete agenDTO.usuario;
      agensDTO.push(agenDTO);
    });

    const agentes: PlanAgentesDTO = {
      pagente: agensDTO
    };

    const planilla: PlanillaDTO = {
      idePlanilla: null,
      nroPlanilla: null,
      fecGeneracion: null,
      codTipoPlanilla: null,
      codFuncGenera: this.funcionarioLog.id.toString(),
      codSedeOrigen: this.dependenciaSelected.codSede,
      codDependenciaOrigen: this.dependenciaSelected.codigo,
      codSedeDestino: this.form.get('dependencia').value.codSede,
      codDependenciaDestino: this.form.get('dependencia').value.codigo,
      codClaseEnvio: null,
      codModalidadEnvio: null,
      pagentes: agentes,
      ideEcm: null
    };

    return planilla;
  };

  generarPlanilla(): void {
    if (this.InformacionEnvioCompletada()) {
      const dependenciaDestinoArray= [];
      const sedeDestinoArray= [];
      const planilla = this.generarDatosExportar();
  
      this._planillaService.generarPlanillas(planilla).subscribe((result) => {
  
        this.selectedComunications.forEach((element) => {
          dependenciaDestinoArray.push(element.correspondencia.codDependencia);
          sedeDestinoArray.push(element.correspondencia.codSede);
        });  
        this.popUpPlanillaGenerada.setDependenciaDestino(this.findDependency(dependenciaDestinoArray[0] ));
        this.popUpPlanillaGenerada.setSedeDestino(this.findSede( sedeDestinoArray[0]));
        this.planillaGenerada = result;
        this.numeroPlanillaDialogVisible = true;
        this.listarDistribuciones();
        this._detectChanges.detectChanges();
      });
    } else {
      this._store.dispatch(new PushNotificationAction({
        severity: 'warning', 
        summary: 'Complete información de envío en las comunicacion(es) seleccionada(s)'
      }));
    }

  }

  Finalizar(): void {
    this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
  }

  InformacionEnvioCompletada(): boolean {
    const anyInvalid = this.listadoComunicaciones.find(com => isNullOrUndefined(com.correspondencia.envio_peso) || isNullOrUndefined(com.correspondencia.envio_valor));
    return anyInvalid ? false : true;
  }

  InformacionEnvio(index: number): void {
    this.comunicacionSeleccionada = this.listadoComunicaciones[index];
    this.informacionEnvioDialogVisible = true;
  }

  ActualizarInformacionEnvio(comunicacion?: ComunicacionOficialDTO) {
    if(!isNullOrUndefined(comunicacion)) {
      const index = this.listadoComunicaciones.findIndex(com => com.correspondencia.nroRadicado === comunicacion.correspondencia.nroRadicado);
      this.listadoComunicaciones[index] = comunicacion;
    } 
    this.informacionEnvioDialogVisible = false; 
  }

  VerDetalle(index: number): void {
    this.detalleDialogVisible = true;
  }


  exportarPlanilla(formato) {
    this._planillaService.exportarPlanilla({
      nroPlanilla: this.planillaGenerada.nroPlanilla,
      formato: formato
    }).subscribe((result) => {
      const pdf = 'data:application/octet-stream;base64,' + result.base64EncodedFile;
      const dlnk: any = document.getElementById('dwnldLnk');
      dlnk.href = pdf;
      dlnk.download = 'planilla.' + formato.toLowerCase();
      dlnk.click();
    });
  }

  hideNumeroPlanillaDialog() {
    this.numeroPlanillaDialogVisible = false;
  }

  hideInformacionEnvioPlanillaDialog() {
    this.informacionEnvioDialogVisible = false;
  }

  hideDetallePlanillaDialog() {
    this.detalleDialogVisible = false;
  }

  save(): Observable<any> {
    return  Observable.of(true).delay(5000);
  }

}
