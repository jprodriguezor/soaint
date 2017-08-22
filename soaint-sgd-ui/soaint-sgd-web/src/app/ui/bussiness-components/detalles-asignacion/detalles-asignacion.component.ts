import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Sandbox as AsiganacionDTOSandbox} from "../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox";
import {ComunicacionOficialDTO} from "../../../domain/comunicacionOficialDTO";


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
      this.refreshView();
    });
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

}
