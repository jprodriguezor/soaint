import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Sandbox as AsiganacionDTOSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {OrganigramaDTO} from '../../../domain/organigramaDTO';
import {environment} from '../../../../environments/environment';
import {AgentDTO} from '../../../domain/agentDTO';
import {Observable} from 'rxjs/Observable';
import {RadicacionEntradaDTV} from '../../../shared/data-transformers/radicacionEntradaDTV';


@Component({
  selector: 'app-detalles-asignacion',
  templateUrl: './detalles-asignacion.component.html',
  styleUrls: ['./detalles-asignacion.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class DetallesAsignacionComponent implements OnInit {

  tabIndex = 0;

  nroRadicado: string;

  comunicacion: ComunicacionOficialDTO = {};

  constantes: ConstanteDTO[];

  municipios: any[];

  dependencias: OrganigramaDTO[];

  remitente$: Observable<AgentDTO>;

  contactos$: Observable<ConstanteDTO[]>;

  destinatarios$: Observable<AgentDTO[]>;

  radicacionEntradaDTV: any;

  docSrc: any;

  ideEcm: string;

  constructor(private _changeDetectorRef: ChangeDetectorRef, private _asiganacionSandbox: AsiganacionDTOSandbox) {
  }

  setNroRadicado(nroRadicado: string) {
    this.nroRadicado = nroRadicado;
  }

  ngOnInit() {

  }

  loadDocumento() {
    this.ideEcm = this.comunicacion.ppdDocumentoList[0].ideEcm;
    this.docSrc = environment.obtenerDocumento + this.ideEcm;
  }

  preview(file) {
    const self = this;
    const myblob = new Blob([file], {
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

  getDepartamentosCode() {
    let result = '';
    this.comunicacion.datosContactoList.forEach((item) => {
      result += item.codMunicipio + ',';
    });

    console.log(result);

     return result;
  }

  loadConstantsByCodes() {
    Observable.combineLatest(
      this._asiganacionSandbox.obtnerConstantesPorCodigos(this.getConstantsCodes()),
      this._asiganacionSandbox.obtnerDependenciasPorCodigos(this.getDependenciesCodes()),
      this._asiganacionSandbox.obtenerMunicipiosPorCodigos(this.getDepartamentosCode()),
      (constantes, dependencias, municipios) => {
        return {
          constantes: constantes.constantes,
          dependencias: dependencias.dependencias,
          municipios: municipios
        }
      }
    ).subscribe((data) => {
      this.constantes = [...data.constantes, ...data.dependencias];
      this.municipios = data.municipios;
      this.refreshView();
    });
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

}
