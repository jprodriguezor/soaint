import {
  ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {OrganigramaDTO} from '../../../domain/organigramaDTO';
import {Observable} from 'rxjs/Observable';
import {AgentDTO} from '../../../domain/agentDTO';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {Subscription} from 'rxjs/Subscription';
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as AsiganacionDTOSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {RadicacionEntradaDTV} from '../../../shared/data-transformers/radicacionEntradaDTV';
import {environment} from '../../../../environments/environment';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';

@Component({
  selector: 'app-documentos-tramite',
  templateUrl: './documentos-tramite.component.html',
  styleUrls: ['./documentos-tramite.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class DocumentosTramiteComponent implements OnInit {


  nroRadicado: string;

  comunicacion: ComunicacionOficialDTO = {};

  constantes: ConstanteDTO[];

  dependencias: OrganigramaDTO[];

  remitente$: Observable<AgentDTO>;

  contactos$: Observable<ConstanteDTO[]>;

  destinatarios$: Observable<AgentDTO[]>;

  radicacionEntradaDTV: any;

  activeTaskUnsubscriber: Subscription;

  funcionarioSubcription: Subscription;

  funcionarioLog: FuncionarioDTO;

  dependenciaSelected$: Observable<DependenciaDTO>;

  dependenciaSelected: DependenciaDTO;

  docSrc: any;

  task: any;

  @ViewChild('popupAgregarObservaciones') popupAgregarObservaciones;

  constructor(private _store: Store<RootState>,
              private _changeDetectorRef: ChangeDetectorRef,
              private _asiganacionSandbox: AsiganacionDTOSandbox) {
    this.funcionarioSubcription = this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });
    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
    this.dependenciaSelected$.subscribe((result) => {
      this.dependenciaSelected = result;
    });
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      if (this.task)
        this.nroRadicado = this.task.variables.numeroRadicado;
    });
  }

  ngOnInit() {
    this.loadComunication();
  }

  loadObservationsData() {
    this.popupAgregarObservaciones.form.reset();
    this.popupAgregarObservaciones.setData({
      idDocumento: this.comunicacion.correspondencia.ideDocumento,
      idFuncionario: this.funcionarioLog.id,
      codOrgaAdmin: this.dependenciaSelected.codigo,
    });
    this.popupAgregarObservaciones.loadObservations();
  }

  setNroRadicado(nroRadicado: string) {
    this.nroRadicado = nroRadicado;
  }

  loadDocumento() {
    this.docSrc = environment.obtenerDocumento + this.comunicacion.ppdDocumentoList[0].ideEcm;
  }

  preview(file) {
    const self = this;
    let myblob = new Blob([file], {
      type: 'application/pdf'
    });
    const reader = new FileReader();
    reader.addEventListener('load', () => {
      self.docSrc = reader.result;
      self._changeDetectorRef.detectChanges();
    }, false);
    reader.readAsArrayBuffer(myblob);
  }

  loadComunication() {
    this._asiganacionSandbox.obtenerComunicacionPorNroRadicado(this.nroRadicado).subscribe((result) => {
      this.comunicacion = result;
      this.loadObservationsData();
      this.loadDocumento();
      this.loadConstantsByCodes();

      this.radicacionEntradaDTV = new RadicacionEntradaDTV(this.comunicacion);
      this.remitente$ = this.radicacionEntradaDTV.getDatosRemitente();
      this.contactos$ = this.radicacionEntradaDTV.getDatosContactos();
      this.destinatarios$ = this.radicacionEntradaDTV.getDatosDestinatarios();

    });
  }

  getConstantsCodes() {
    let result = '';
    console.log(this.comunicacion);
    this.comunicacion.agenteList.forEach((item) => {
      result += item.codTipAgent + ',';
      result += item.codEnCalidad + ',';
      result += item.indOriginal + ',';
      result += item.codTipoPers + ',';
      result += item.codTipDocIdent + ',';
    });
    this.comunicacion.anexoList.forEach((item) => {
      result += item.codAnexo + ',';
    });
    this.comunicacion.ppdDocumentoList.forEach((item) => {
      result += item.codTipoDoc + ',';
    });
    result += this.comunicacion.correspondencia.codTipoCmc + ',';
    result += this.comunicacion.correspondencia.codMedioRecepcion + ',';
    result += this.comunicacion.correspondencia.codUnidadTiempo + ',';
    result += this.comunicacion.correspondencia.codTipoDoc + ',';
    return result;
  }

  onTabChange() {
    this.refreshView();
  }

  getDependenciesCodes() {
    let result = '';
    this.comunicacion.agenteList.forEach((item) => {
      result += item.codDependencia + ',';
    });
    return result;
  }

  loadConstantsByCodes() {
    this._asiganacionSandbox.obtnerConstantesPorCodigos(this.getConstantsCodes()).subscribe((response) => {
      this.constantes = response.constantes;
      this._asiganacionSandbox.obtnerDependenciasPorCodigos(this.getDependenciesCodes()).subscribe((result) => {
        this.constantes.push(...result.dependencias);
        this.refreshView();
      });
    });
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

}