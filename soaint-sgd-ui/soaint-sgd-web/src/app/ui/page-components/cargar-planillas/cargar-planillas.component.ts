import {Component, OnInit, ViewChild} from '@angular/core';
import {PlanAgentesDTO} from "../../../domain/PlanAgentesDTO";
import {PlanAgenDTO} from "../../../domain/PlanAgenDTO";
import {PlanillaDTO} from "../../../domain/PlanillaDTO";
import {RadicacionEntradaDTV} from "../../../shared/data-transformers/radicacionEntradaDTV";
import {AgentDTO} from "../../../domain/agentDTO";
import {Observable} from "rxjs/Observable";
import {getTipologiaDocumentalArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/tipologia-documental-selectors";
import {DocumentoDTO} from "../../../domain/documentoDTO";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ComunicacionOficialDTO} from "../../../domain/comunicacionOficialDTO";
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
import {
  getArrayData as PlanillasArrayData,
  getDataobj
} from '../../../infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-selectors';
import {Sandbox as CargarPlanillasSandbox} from "../../../infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-sandbox";

@Component({
  selector: 'app-cargar-planillas',
  templateUrl: './cargar-planillas.component.html',
  styleUrls: ['./cargar-planillas.component.css']
})
export class CargarPlanillasComponent implements OnInit {

  form: FormGroup;

  comunicaciones$: Observable<ComunicacionOficialDTO[]>;

  data$: Observable<any>;

  selectedComunications: ComunicacionOficialDTO[] = [];

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

  @ViewChild('popupjustificaciones') popupjustificaciones;

  @ViewChild('popupAgregarObservaciones') popupAgregarObservaciones;

  @ViewChild('popupReject') popupReject;

  @ViewChild('detallesView') detallesView;

  constructor(private _store: Store<RootState>,
              private _cargarPlanillasApi: CargarPlanillasSandbox,
              private _funcionarioSandbox: FuncionarioSandbox,
              private _constSandbox: ConstanteSandbox,
              private _dependenciaSandbox: DependenciaSandbox,
              private _planillaService: PlanillasApiService,
              private formBuilder: FormBuilder) {
    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
    this.dependenciaSelected$.subscribe((result) => {
      this.dependenciaSelected = result;
    });
    this.comunicaciones$ = this._store.select(PlanillasArrayData);
    this.data$ = this._store.select(getDataobj);
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

  getDatosDestinatario(comunicacion): Observable<AgentDTO[]> {
    const radicacionEntradaDTV = new RadicacionEntradaDTV(comunicacion);
    return radicacionEntradaDTV.getDatosDestinatarios();
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

  listarDependencias() {
    this._dependenciaSandbox.loadDependencies({}).subscribe((results) => {
      this.dependencias = results.dependencias;
      this.listarDistribuciones();
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
    this._cargarPlanillasApi.loadDispatch({
      // fecha_ini: this.convertDate(this.start_date),
      // fecha_fin: this.convertDate(this.end_date),
      // cod_dependencia: this.form.get('dependencia').value ? this.form.get('dependencia').value.codigo : '',
      // cod_tipologia_documental: this.form.get('tipologia').value ? this.form.get('tipologia').value.codigo : '',
      nro_planilla: this.form.get('numeroPlanilla').value ? this.form.get('numeroPlanilla').value : '',
    });
  }

  generarDatosExportar(): PlanillaDTO {
    let agensDTO: PlanAgenDTO[] = [];

    this.selectedComunications.forEach((element) => {
      console.log(element);
      let agenDTO: PlanAgenDTO = {
        idePlanAgen: null,
        estado: null,
        varPeso: null,
        varValor: null,
        varNumeroGuia: null,
        fecObservacion: null,
        codNuevaSede: null,
        codNuevaDepen: null,
        observaciones: null,
        codCauDevo: null,
        fecCarguePla: null,
        ideAgente: this.getDatosDestinatarioInmediate(element)[0].ideAgente,
        ideDocumento: element.correspondencia.ideDocumento,
      };
      agensDTO.push(agenDTO);
    });

    let agentes: PlanAgentesDTO = {
      pagente: agensDTO
    };

    let planilla: PlanillaDTO = {
      idePlanilla: null,
      nroPlanilla: null,
      fecGeneracion: null,
      codTipoPlanilla: null,
      codFuncGenera: this.funcionarioLog.id.toString(),
      codSedeOrigen: this.dependenciaSelected.codSede,
      codDependenciaOrigen: this.dependenciaSelected.codigo,
      codSedeDestino: this.form.get("dependencia").value.codSede,
      codDependenciaDestino: this.form.get("dependencia").value.codigo,
      codClaseEnvio: null,
      codModalidadEnvio: null,
      pagentes: agentes,
    };

    return planilla;
  };

  showEditarPlanillaDialog() {
    this.editarPlanillaDialogVisible = true;
  }

  hideEditarPlanillaDialog() {
    this.editarPlanillaDialogVisible = false;
  }

  editarPlanilla() {

  }

}
