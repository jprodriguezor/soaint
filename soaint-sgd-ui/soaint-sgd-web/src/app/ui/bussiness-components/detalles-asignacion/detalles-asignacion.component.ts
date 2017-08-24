import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Sandbox as AsiganacionDTOSandbox} from "../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox";
import {ComunicacionOficialDTO} from "../../../domain/comunicacionOficialDTO";
import {ConstanteDTO} from "../../../domain/constanteDTO";
import {OrganigramaDTO} from '../../../domain/organigramaDTO';


@Component({
  selector: 'app-detalles-asignacion',
  templateUrl: './detalles-asignacion.component.html',
  styleUrls: ['./detalles-asignacion.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class DetallesAsignacionComponent implements OnInit {

  tabIndex: number = 0;

  nroRadicado: string;

  comunicacion: ComunicacionOficialDTO = {};

  constantes: ConstanteDTO[];

  dependencias: OrganigramaDTO[];

  docSrc: any = "";

  constructor(private _changeDetectorRef: ChangeDetectorRef, private _asiganacionSandbox: AsiganacionDTOSandbox) {
  }

  setNroRadicado(nroRadicado: string) {
    this.nroRadicado = nroRadicado;
  }

  ngOnInit() {

  }

  loadDocumento() {
    this._asiganacionSandbox.obtenerDocumento("d126e9fa-bd82-4689-9b96-3f3ca4dcf109").subscribe((result) => {
      console.info(result._body);
      this.preview(result._body);
    }, (error) => {
      console.log(error);
    });
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
      console.log(this.comunicacion);
      this.loadDocumento();
      this.loadConstantsByCodes();
    });
  }

  getConstantsCodes() {
    let result = '';
    this.comunicacion.agenteList.forEach((item) => {
      result += item.codTipAgent + ',';
      result += item.codEnCalidad + ',';
    });
    this.comunicacion.anexoList.forEach((item) => {
      result += item.codAnexo + ',';
    });
    result += this.comunicacion.correspondencia.codTipoCmc + ',';
    result += this.comunicacion.correspondencia.codMedioRecepcion + ',';
    result += this.comunicacion.correspondencia.codUnidadTiempo + ',';
    result += this.comunicacion.correspondencia.codTipoDoc + ',';
    return result;
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
