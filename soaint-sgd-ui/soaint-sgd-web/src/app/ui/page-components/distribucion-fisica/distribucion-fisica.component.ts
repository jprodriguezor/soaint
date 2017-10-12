import {Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {Sandbox as DistribucionFisicaSandbox} from '../../../infrastructure/state-management/distrubucionFisicaDTO-state/distrubucionFisicaDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {
  getArrayData as getFuncionarioArrayData,
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {getArrayData as DistribucionArrayData} from '../../../infrastructure/state-management/distrubucionFisicaDTO-state/distrubucionFisicaDTO-selectors';
import {Sandbox as FuncionarioSandBox} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {Subscription} from 'rxjs/Subscription';
import {AgentDTO} from '../../../domain/agentDTO';
import {
  getAgragarObservacionesDialogVisible,
  getDetailsDialogVisible,
  getJustificationDialogVisible,
  getRejectDialogVisible
} from 'app/infrastructure/state-management/asignacionDTO-state/asignacionDTO-selectors';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {getTipologiaDocumentalArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/tipologia-documental-selectors';
import {RadicacionEntradaDTV} from "../../../shared/data-transformers/radicacionEntradaDTV";
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {DocumentoDTO} from "../../../domain/documentoDTO";
import {PlanillasApiService} from "../../../infrastructure/api/planillas.api";
import {PlanillaDTO} from "../../../domain/PlanillaDTO";
import {PlanAgentesDTO} from "../../../domain/PlanAgentesDTO";
import {PlanAgenDTO} from "../../../domain/PlanAgenDTO";
import {escape} from "querystring";
import {Sandbox as ProcessSandbox} from "../../../infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox";

@Component({
  selector: 'app-distribucion-fisica',
  templateUrl: './distribucion-fisica.component.html',
  styleUrls: ['./distribucion-fisica.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DistribucionFisicaComponent implements OnInit, OnDestroy {

  form: FormGroup;

  comunicaciones$: Observable<ComunicacionOficialDTO[]>;

  selectedComunications: ComunicacionOficialDTO[] = [];

  start_date: Date = new Date();

  end_date: Date = new Date();

  dependencia: any;

  planillaGenerada: PlanillaDTO;

  numeroPlanillaDialogVisible: boolean = false;

  funcionariosSuggestions$: Observable<FuncionarioDTO[]>;

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
              private _distribucionFisicaApi: DistribucionFisicaSandbox,
              private _funcionarioSandbox: FuncionarioSandBox,
              private _constSandbox: ConstanteSandbox,
              private _dependenciaSandbox: DependenciaSandbox,
              private _planillaService: PlanillasApiService,
              private _processSandbox: ProcessSandbox,
              private formBuilder: FormBuilder) {
    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
    this.dependenciaSelected$.subscribe((result) => {
      this.dependenciaSelected = result;
    });
    this.comunicaciones$ = this._store.select(DistribucionArrayData);
    this.funcionariosSuggestions$ = this._store.select(getFuncionarioArrayData);
    this.start_date.setHours(this.start_date.getHours() - 24);
    this.funcionarioSubcription = this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });
    this.comunicacionesSubcription = this._store.select(DistribucionArrayData).subscribe(() => {
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
      'nroRadicado': [null],
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
    this._distribucionFisicaApi.loadDispatch({
      fecha_ini: this.convertDate(this.start_date),
      fecha_fin: this.convertDate(this.end_date),
      cod_dependencia: this.form.get('dependencia').value ? this.form.get('dependencia').value.codigo : '',
      cod_tipologia_documental: this.form.get('tipologia').value ? this.form.get('tipologia').value.codigo : '',
      nro_radicado: this.form.get('nroRadicado').value ? this.form.get('nroRadicado').value : '',
    });
  }

  generarDatosExportar(): PlanillaDTO {
    let agensDTO: PlanAgenDTO[] = [];

    this.selectedComunications.forEach((element) => {
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
      ideEcm: null
    };

    return planilla;
  };

  generarPlanilla() {
    const planilla = this.generarDatosExportar();
    this._planillaService.generarPlanillas(planilla).subscribe((result) => {
      this.planillaGenerada = result;
      this.numeroPlanillaDialogVisible = true;
      this.listarDistribuciones();
    });
  }

  exportarPlanilla(formato) {
    //104000000000005
    this._planillaService.exportarPlanilla({
      nroPlanilla: this.planillaGenerada.nroPlanilla,
      // nroPlanilla: '104000000000005',
      formato: formato
    }).subscribe((result) => {

      let pdf = 'data:application/octet-stream;base64,' + result.base64EncodedFile;
      let dlnk: any = document.getElementById('dwnldLnk');
      dlnk.href = pdf;
      dlnk.download = 'planilla.' + formato.toLowerCase();
      dlnk.click();

      // window.open('data:application/pdf;base64,' + atob(result.base64EncodedFile));
    });
  }

  hideNumeroPlanillaDialog() {
    this.numeroPlanillaDialogVisible = false;
  }

  downloadName: string;

}
