import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {PlanAgenDTO} from "../../../domain/PlanAgenDTO";
import {RadicacionEntradaDTV} from "../../../shared/data-transformers/radicacionEntradaDTV";
import {AgentDTO} from "../../../domain/agentDTO";
import {Observable} from "rxjs/Observable";
import {getTipologiaDocumentalArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/tipologia-documental-selectors";
import {FormBuilder, FormGroup} from "@angular/forms";
import {FuncionarioDTO} from "../../../domain/funcionarioDTO";
import {DependenciaDTO} from "../../../domain/dependenciaDTO";
import {Subscription} from "rxjs/Subscription";
import {ConstanteDTO} from "../../../domain/constanteDTO";
import {Store} from "@ngrx/store";
import {PlanillasApiService} from "../../../infrastructure/api/planillas.api";
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {
  getArrayData as getFuncionarioArrayData,
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {
  getAgragarObservacionesDialogVisible,
  getDetailsDialogVisible,
  getJustificationDialogVisible,
  getRejectDialogVisible
} from "../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-selectors";
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as FuncionarioSandbox} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox";
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {getArrayData as PlanillasArrayData} from '../../../infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-selectors';
import {Sandbox as CargarPlanillasSandbox} from "../../../infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-sandbox";
import {PlanillaDTO} from "../../../domain/PlanillaDTO";
import {PlanAgentesDTO} from "../../../domain/PlanAgentesDTO";

@Component({
  selector: 'app-cargar-planillas',
  templateUrl: './cargar-planillas.component.html',
  styleUrls: ['./cargar-planillas.component.css'],
  changeDetection: ChangeDetectionStrategy.Default
})
export class CargarPlanillasComponent implements OnInit, OnDestroy {

  form: FormGroup;

  comunicaciones: PlanAgenDTO[] = [];

  data: PlanillaDTO | any = {};

  selectedComunications: PlanAgenDTO[] = [];

  start_date: Date = new Date();

  editarPlanillaDialogVisible: boolean = false;

  dependencia: any;

  funcionariosSuggestions$: Observable<FuncionarioDTO[]>;

  justificationDialogVisible$: Observable<boolean>;

  detailsDialogVisible$: Observable<boolean>;

  agregarObservacionesDialogVisible$: Observable<boolean>;

  rejectDialogVisible$: Observable<boolean>;

  dependenciaSelected$: Observable<DependenciaDTO>;

  dependenciaSelected: DependenciaDTO;

  funcionarioLog: FuncionarioDTO;

  funcionarioSubcription: Subscription;

  comunicacionesSubcription: Subscription;

  tipologiaDocumentalSuggestions$: Observable<ConstanteDTO[]>;

  tipologiasDocumentales: ConstanteDTO[];

  dependencias: DependenciaDTO[] = [];

  @ViewChild('popupEditar') popupEditar;

  constructor(private _store: Store<RootState>,
              private _cargarPlanillasApi: CargarPlanillasSandbox,
              private _funcionarioSandbox: FuncionarioSandbox,
              private _constSandbox: ConstanteSandbox,
              private _dependenciaSandbox: DependenciaSandbox,
              private _planillaService: PlanillasApiService,
              private _changeDetectorRef: ChangeDetectorRef,
              private formBuilder: FormBuilder) {
    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
    this.dependenciaSelected$.subscribe((result) => {
      this.dependenciaSelected = result;
    });
    // this.comunicaciones$ = this._store.select(PlanillasArrayData);
    // this.data$ = this._store.select(getDataobj);
    this.funcionariosSuggestions$ = this._store.select(getFuncionarioArrayData);
    this.justificationDialogVisible$ = this._store.select(getJustificationDialogVisible);
    this.detailsDialogVisible$ = this._store.select(getDetailsDialogVisible);
    this.agregarObservacionesDialogVisible$ = this._store.select(getAgragarObservacionesDialogVisible);
    this.rejectDialogVisible$ = this._store.select(getRejectDialogVisible);
    this.start_date.setHours(this.start_date.getHours() - 24);
    this.funcionarioSubcription = this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });
    this.comunicacionesSubcription = this._store.select(PlanillasArrayData).subscribe((result) => {
      this.selectedComunications = [];
    });
    this.initForm();
  }

  ngOnInit() {
    this.tipologiaDocumentalSuggestions$ = this._store.select(getTipologiaDocumentalArrayData);
    this.tipologiaDocumentalSuggestions$.subscribe((results) => {
      this.tipologiasDocumentales = results;
    });

    this._funcionarioSandbox.loadAllFuncionariosDispatch();
    this._constSandbox.loadDatosGeneralesDispatch();
    this.listarDependencias();
  }

  getDatosRemitente(comunicacion): Observable<AgentDTO> {
    const radicacionEntradaDTV = new RadicacionEntradaDTV(comunicacion);
    return radicacionEntradaDTV.getDatosRemitente();
  }

  ngOnDestroy() {
    this.funcionarioSubcription.unsubscribe();
    this.comunicacionesSubcription.unsubscribe();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'funcionario': [null],
      'dependencia': [null],
      'numeroPlanilla': [null],
      'tipologia': [null],
    });
  }

  getEstadoLabel(estado) {
    return this.popupEditar.estadoEntregaSuggestions.find((element) => element.codigo == estado);
  }

  listarDependencias() {
    this._dependenciaSandbox.loadDependencies({}).subscribe((results) => {
      this.dependencias = results.dependencias;
      this.listarDistribuciones();
    });
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
    this._cargarPlanillasApi.loadData({
      nro_planilla: this.form.get('numeroPlanilla').value ? this.form.get('numeroPlanilla').value : '',
    }).subscribe((result) => {
      this.data = result;
      this.comunicaciones = [...result.pagentes.pagente];
      this.refreshView();
    });
  }

  showEditarPlanillaDialog() {
    this.editarPlanillaDialogVisible = true;
  }

  hideEditarPlanillaDialog() {
    this.editarPlanillaDialogVisible = false;
  }

  createAgents(): PlanAgenDTO[] {
    let agents: PlanAgenDTO[] = [];
    this.selectedComunications.forEach((element) => {
      let agent: PlanAgenDTO = {
        idePlanAgen: element.idePlanAgen,
        estado: this.popupEditar.form.get('estadoEntrega').value.codigo,
        varPeso: element.varPeso,
        varValor: element.varValor,
        varNumeroGuia: element.varNumeroGuia,
        fecObservacion: element.fecObservacion,
        usuario: this.funcionarioLog.nombre,
        codNuevaSede: this.popupEditar.form.get('dependenciaDestino').value ? this.popupEditar.form.get('dependenciaDestino').value.codSede : null,
        codNuevaDepen: this.popupEditar.form.get('dependenciaDestino').value ? this.popupEditar.form.get('dependenciaDestino').value.codigo : null,
        observaciones: this.popupEditar.form.get('observaciones').value,
        codCauDevo: element.codCauDevo,
        fecCarguePla: this.popupEditar.form.get('fechaEntrega').value,
        ideAgente: element.ideAgente,
        ideDocumento: element.ideDocumento,
        nroRadicado: element.nroRadicado,
        tipoPersona: element.tipoPersona,
        tipologiaDocumental: null,
        nit: element.nit,
        nroDocuIdentidad: element.nroDocuIdentidad,
        nombre: element.nombre,
        razonSocial: element.razonSocial,
        folios: element.folios,
        anexos: element.anexos,
      };
      agents.push(agent);
    });

    return agents;
  }

  editarPlanilla() {
    let agents: PlanAgenDTO[] = this.createAgents();
    let coms = [...this.comunicaciones];
    agents.forEach((element) => {
      const index = coms.findIndex((el) => el.ideAgente == element.ideAgente);
      if (index > -1)
        coms[index] = element;
    });
    this.comunicaciones = [...coms];
    this.hideEditarPlanillaDialog();
    this.refreshView();

  }

  onDocUploaded(event): void {
    console.log(event);
  }

  actualizarPlanilla() {
    this.comunicaciones.forEach((p) => {
      delete p.usuario;
      delete p['_$visited'];
    });
    let agentes: PlanAgentesDTO = {
      pagente: this.comunicaciones
    };
    let planilla: PlanillaDTO = {
      idePlanilla: null,
      nroPlanilla: null,
      fecGeneracion: null,
      codTipoPlanilla: null,
      codFuncGenera: null,
      codSedeOrigen: null,
      codDependenciaOrigen: null,
      codSedeDestino: null,
      codDependenciaDestino: null,
      codClaseEnvio: null,
      codModalidadEnvio: null,
      pagentes: agentes
    };

    this._planillaService.cargarPlanillas(planilla).subscribe(() => {
      this.listarDistribuciones();
    });

  }

  canUpdatePlanilla(): boolean {
    return this.comunicaciones.findIndex((e) => e.estado && e.estado != '') > -1;
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

}
