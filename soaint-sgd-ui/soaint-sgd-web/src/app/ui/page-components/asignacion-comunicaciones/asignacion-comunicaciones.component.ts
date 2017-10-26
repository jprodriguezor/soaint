import {Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {Sandbox as CominicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {State as RootState, State} from 'app/infrastructure/redux-store/redux-reducers';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {FormBuilder, FormGroup} from '@angular/forms';
import {createSelector} from 'reselect';
import {getArrayData as getFuncionarioArrayData, getAuthenticatedFuncionario, getSelectedDependencyGroupFuncionario} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {getArrayData as ComunicacionesArrayData} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-selectors';
import {Sandbox} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {AsignacionDTO} from '../../../domain/AsignacionDTO';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {Subscription} from 'rxjs/Subscription';
import {AgentDTO} from '../../../domain/agentDTO';
import {OrganigramaDTO} from '../../../domain/organigramaDTO';
import {getAgragarObservacionesDialogVisible, getDetailsDialogVisible, getJustificationDialogVisible, getRejectDialogVisible} from 'app/infrastructure/state-management/asignacionDTO-state/asignacionDTO-selectors';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';
import {RedireccionDTO} from '../../../domain/redireccionDTO';
import {DroolsRedireccionarCorrespondenciaApi} from '../../../infrastructure/api/drools-redireccionar-correspondencia.api';
import {PushNotificationAction} from 'app/infrastructure/state-management/notifications-state/notifications-actions';
import {WARN_REDIRECTION} from 'app/shared/lang/es';
import 'rxjs/add/operator/toArray';
import 'rxjs/add/observable/from';
import 'rxjs/add/operator/concatMap';
import 'rxjs/add/observable/forkJoin';
import {tassign} from 'tassign';
import {PpdTrazDocumentoDTO} from '../../../domain/PpdTrazDocumentoDTO';

@Component({
  selector: 'app-asignacion-comunicaciones',
  templateUrl: './asignacion-comunicaciones.component.html',
  styleUrls: ['./asignacion-comunicaciones.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class AsignarComunicacionesComponent implements OnInit, OnDestroy {

  form: FormGroup;

  comunicaciones$: Observable<ComunicacionOficialDTO[]>;

  estadosCorrespondencia: Array<{ label: string, value: string }>;

  selectedComunications: ComunicacionOficialDTO[] = [];

  selectedFuncionarios: FuncionarioDTO[] = [];

  asignationType = 'auto';

  start_date: Date = new Date();

  end_date: Date = new Date();

  estadoCorrespondencia: any;

  funcionariosSuggestions$: Observable<FuncionarioDTO[]>;

  justificationDialogVisible$: Observable<boolean>;

  detailsDialogVisible$: Observable<boolean>;

  agregarObservacionesDialogVisible$: Observable<boolean>;

  rejectDialogVisible$: Observable<boolean>;

  dependenciaSelected$: Observable<DependenciaDTO>;

  dependenciaSelected: DependenciaDTO;

  funcionarioSelected: FuncionarioDTO;

  funcionarioLog: FuncionarioDTO;

  funcionarioSubcription: Subscription;

  comunicacionesSubcription: Subscription;

  redireccionesFallidas: Array<RedireccionDTO>;

  @ViewChild('popupjustificaciones') popupjustificaciones;

  @ViewChild('popupAgregarObservaciones') popupAgregarObservaciones;

  @ViewChild('popupReject') popupReject;

  @ViewChild('detallesView') detallesView;

  authPayload: { usuario: string, pass: string } | {};
  authPayloadUnsubscriber: Subscription;

  constructor(private _store: Store<RootState>,
              private _comunicacionOficialApi: CominicacionOficialSandbox,
              private _asignacionSandbox: AsignacionSandbox,
              private _funcionarioSandbox: Sandbox,
              private ruleCheckRedirectionNumber: DroolsRedireccionarCorrespondenciaApi,
              private formBuilder: FormBuilder) {
    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
    this.dependenciaSelected$.subscribe((result) => {
      this.dependenciaSelected = result;
    });
    this.comunicaciones$ = this._store.select(ComunicacionesArrayData);
    this.funcionariosSuggestions$ = this._store.select(getFuncionarioArrayData);
    this.justificationDialogVisible$ = this._store.select(getJustificationDialogVisible);
    this.detailsDialogVisible$ = this._store.select(getDetailsDialogVisible);
    this.agregarObservacionesDialogVisible$ = this._store.select(getAgragarObservacionesDialogVisible);
    this.rejectDialogVisible$ = this._store.select(getRejectDialogVisible);
    this.start_date.setHours(this.start_date.getHours() - 24);
    this.funcionarioSubcription = this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });
    this.comunicacionesSubcription = this._store.select(ComunicacionesArrayData).subscribe(() => {
      this.selectedComunications = [];
    });

    this.authPayloadUnsubscriber = this._store.select(createSelector((s: State) => s.auth.profile, (profile) => {
      return profile ? {usuario: profile.username, pass: profile.password} : {};
    })).subscribe((value) => {
      this.authPayload = value;
    });

    this.initForm();
  }

  getCodEstadoLabel(codEstado: string): string {
    const estado = this.estadosCorrespondencia.find((item) => {
      return item.value === codEstado;
    });
    return estado.label;
  }

  ngOnInit() {
    this._funcionarioSandbox.loadAllFuncionariosDispatch();
    this.llenarEstadosCorrespondencias();
    this.listarComunicaciones();
  }

  ngOnDestroy() {
    this.funcionarioSubcription.unsubscribe();
    this.comunicacionesSubcription.unsubscribe();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'funcionario': [null],
      'estadoCorrespondencia': [null],
      'nroRadicado': [null],
    });
  }

  llenarEstadosCorrespondencias() {
    this.estadosCorrespondencia = [
      {
        label: 'SIN ASIGNAR',
        value: 'SA'
      },
      {
        label: 'ASIGNADA',
        value: 'AS'
      }
    ];
    this.form.get('estadoCorrespondencia').setValue(this.estadosCorrespondencia[0].value);
  }

  convertDate(inputFormat) {
    function pad(s) {
      return (s < 10) ? '0' + s : s;
    }

    const d = new Date(inputFormat);
    return [pad(d.getFullYear()), pad(d.getMonth() + 1), d.getDate()].join('-');
  }

  assignComunications() {
    this._asignacionSandbox.assignDispatch({
      asignaciones: {
        asignaciones: this.asignationType === 'auto' ? this.createAsignacionesAuto() : this.createAsignaciones()
      },
      traza: {
        ideFunci: this.funcionarioLog.id
      }
    });
  }

  reassignComunications() {
    this._asignacionSandbox.reassignDispatch(tassign({
      asignaciones: {
        asignaciones: this.asignationType === 'auto' ? this.createAsignacionesAuto() : this.createAsignaciones()
      },
      idFunc: this.funcionarioLog.id
    }, this.authPayload));
  }

  redirectComunications(justificationValues: { justificacion: string, sedeAdministrativa: OrganigramaDTO, dependenciaGrupo: OrganigramaDTO }) {

    this.checkRedirectionsAgentes('numRedirecciones', justificationValues).subscribe(checks => {

      const failChecks = [];
      const agentesSuccess = [];

      checks.forEach(value => {
        if (!value.success) {
          failChecks.push(value.agente);
        } else {
          agentesSuccess.push(value.agente);
        }
      });

      if (failChecks.length > 0) {
        this._store.dispatch(new PushNotificationAction({
          severity: 'warn',
          summary: WARN_REDIRECTION
        }));

        this.redireccionesFallidas = failChecks;

      } else {
        this._asignacionSandbox.redirectDispatch(this.createRecursiveRedirectsPayload(agentesSuccess, justificationValues));
      }
    });

  }

  devolverComunicaciones(justificationValues: { justificacion: string, sedeAdministrativa: OrganigramaDTO, dependenciaGrupo: OrganigramaDTO }) {

    // const checks = this.checkRedirectionsAgentes('numDevoluciones', justificationValues);

    // if (checks.failChecks.length > 0) {
    //   this._store.dispatch(new PushNotificationAction({
    //     severity: 'warn',
    //     summary: WARN_REDIRECTION
    //   }));
    //
    // } else {
    //   this._store.dispatch(new PushNotificationAction({
    //     severity: 'success',
    //     summary: SUCCESS_REDIRECTION
    //   }));
    //
    //   alert('Aún no se ha definido como se debe proceder');
    //   // checks.successChecks.forEach(payload => {
    //   //   // this._asignacionSandbox.redirectDispatch(payload);
    //   //
    //   // });
    // }
    //
    // this.redireccionesFallidas = checks.failChecks;
  }

  sendRedirect() {
    this.popupjustificaciones.redirectComunications();
  }

  processComunications() {
    this._asignacionSandbox.assignDispatch({
      asignaciones: {
        asignaciones: this.createAsignaciones(this.funcionarioLog.id, this.funcionarioLog.loginName),
      },
      traza: {
        ideFunci: this.funcionarioLog.id
      }
    });
  }

  showRedirectDialog() {
    this.popupjustificaciones.form.reset();
    this._asignacionSandbox.setVisibleJustificationDialogDispatch(true);
  }

  hideJustificationPopup() {
    this._asignacionSandbox.setVisibleJustificationDialogDispatch(false);
  }


  showDetailsDialog(nroRadicado: string): void {
    this.detallesView.setNroRadicado(nroRadicado);
    this.detallesView.loadComunication();
    this._asignacionSandbox.setVisibleDetailsDialogDispatch(true);
  }

  hideDetailsDialog() {
    this._asignacionSandbox.setVisibleDetailsDialogDispatch(false);
  }

  showAddObservationsDialog(idDocuemento: number) {
    this.popupAgregarObservaciones.form.reset();
    this.popupAgregarObservaciones.setData({
      idDocumento: idDocuemento,
      idFuncionario: this.funcionarioLog.id,
      codOrgaAdmin: this.dependenciaSelected.codigo
    });
    this.popupAgregarObservaciones.loadObservations();
    this._asignacionSandbox.setVisibleAddObservationsDialogDispatch(true);
  }

  hideAddObservationsPopup() {
    this._asignacionSandbox.setVisibleAddObservationsDialogDispatch(false);
  }

  showRejectDialog() {
    this.popupReject.form.reset();
    this._asignacionSandbox.setVisibleRejectDialogDispatch(true);
  }

  hideRejectDialog() {
    this._asignacionSandbox.setVisibleRejectDialogDispatch(false);
  }

  createAsignacionesAuto(): any {
    const asignaciones: AsignacionDTO[] = [];
    let funcIndex = 0;
    this.selectedComunications.forEach((value, index) => {
      if (!this.selectedFuncionarios[funcIndex]) {
        funcIndex = 0;
      }
      asignaciones.push({
        ideAsignacion: null,
        fecAsignacion: null,
        ideFunci: this.selectedFuncionarios[funcIndex].id,
        codDependencia: value.agenteList[0].codDependencia,
        codTipAsignacion: 'TA',
        observaciones: null,
        codTipCausal: null,
        codTipProceso: null,
        ideAsigUltimo: null,
        nivLectura: null,
        nivEscritura: null,
        fechaVencimiento: null,
        alertaVencimiento: null,
        idInstancia: null,
        ideAgente: value.agenteList[0].ideAgente,
        ideDocumento: value.correspondencia.ideDocumento,
        nroRadicado: value.correspondencia.nroRadicado,
        fecRadicado: value.correspondencia.fecRadicado,
        loginName: this.selectedFuncionarios[funcIndex].loginName
      });
      funcIndex++;
    });
    return asignaciones
  }

  createAsignaciones(idFuncionario?, loginNameFuncionario?): AsignacionDTO[] {
    const asignaciones: AsignacionDTO[] = [];
    this.selectedComunications.forEach((value) => {
      asignaciones.push({
        ideAsignacion: null,
        fecAsignacion: null,
        alertaVencimiento: null,
        ideFunci: idFuncionario || this.funcionarioSelected.id,
        codDependencia: value.agenteList[0].codDependencia,
        codTipAsignacion: 'TA',
        observaciones: null,
        codTipCausal: null,
        codTipProceso: null,
        ideAsigUltimo: null,
        nivLectura: null,
        nivEscritura: null,
        fechaVencimiento: null,
        idInstancia: null,
        ideAgente: value.agenteList[0].ideAgente,
        ideDocumento: value.correspondencia.ideDocumento,
        nroRadicado: value.correspondencia.nroRadicado,
        fecRadicado: value.correspondencia.fecRadicado,
        loginName: loginNameFuncionario || this.funcionarioSelected.loginName
      })
    });
    return asignaciones;
  }

  createAgentes(justificationValues: { justificacion: string, sedeAdministrativa: OrganigramaDTO, dependenciaGrupo: OrganigramaDTO }): AgentDTO[] {
    const agentes: AgentDTO[] = [];
    this.selectedComunications.forEach((value) => {
      const agente = value.agenteList[0];
      agente.codSede = justificationValues.sedeAdministrativa.codigo;
      agente.codDependencia = justificationValues.dependenciaGrupo.codigo;
      delete agente['_$visited'];
      agentes.push(agente)
    });
    return agentes;
  }

  checkRedirectionsAgentes(key, justification): Observable<any[]> {

    const failChecks = [];
    const successChecks = [];

    return Observable.from(this.selectedComunications)
      .flatMap(value => {
        const agente = value.agenteList[0];
        agente.codSede = justification.sedeAdministrativa.codigo;
        agente.codDependencia = justification.dependenciaGrupo.codigo;
        delete agente['_$visited'];
        return this.ruleCheckRedirectionNumber.check(agente[key]).map(ruleCheck => {
          return {
            success: ruleCheck,
            agente: agente
          };
        });
      }).toArray();
  }

  createRecursiveRedirectsPayload(agentes, justification): RedireccionDTO {
    return {
      agentes: [...agentes],
      traza: {
        id: null,
        observacion: justification.justificacion,
        ideFunci: this.funcionarioLog.id,
        codDependencia: this.dependenciaSelected.codigo,
        estado: null,
        fecha: null,
        ideDocumento: null
      }
    };
  }

  hideAndCleanDevolucionesFallidasDialog() {
    this.redireccionesFallidas = null;
  }


  listarComunicaciones() {
    this._comunicacionOficialApi.loadDispatch({
      fecha_ini: this.convertDate(this.start_date),
      fecha_fin: this.convertDate(this.end_date),
      cod_estado: this.form.get('estadoCorrespondencia').value,
      nro_radicado: this.form.get('nroRadicado').value ? this.form.get('nroRadicado').value : '',
    });
  }
}

