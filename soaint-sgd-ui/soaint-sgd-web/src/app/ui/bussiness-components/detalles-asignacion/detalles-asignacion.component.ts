import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Sandbox as AsiganacionDTOSandbox} from "../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox";
import {ComunicacionOficialDTO} from "../../../domain/comunicacionOficialDTO";
import {ConstanteDTO} from "../../../domain/constanteDTO";


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

  constantesDest: ConstanteDTO[];

  constructor(private _changeDetectorRef: ChangeDetectorRef, private _asiganacionSandbox: AsiganacionDTOSandbox) {
  }

  setNroRadicado(nroRadicado: string) {
    this.nroRadicado = nroRadicado;
  }

  ngOnInit() {

  }

  loadComunication() {
    this._asiganacionSandbox.obtenerComunicacionPorNroRadicado(this.nroRadicado).subscribe((result) => {
      this.comunicacion = result;
      console.log(this.comunicacion);
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

  loadConstantsByCodes() {
    this._asiganacionSandbox.obtnerConstantesPorCodigos(this.getConstantsCodes()).subscribe((response) => {
      this.constantes = response.constantes;
      this._asiganacionSandbox.obtnerDependenciaPorCodigo(this.comunicacion.agenteList[0].codDependencia).subscribe((result) => {
        this.constantes.push(result);
        this.refreshView();
      });
    });
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

}
