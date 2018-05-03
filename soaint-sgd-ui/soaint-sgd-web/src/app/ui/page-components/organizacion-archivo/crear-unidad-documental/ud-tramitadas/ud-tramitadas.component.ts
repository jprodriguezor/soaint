import {Component, Input, OnInit} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {UnidadDocumentalApiService} from "../../../../../infrastructure/api/unidad-documental.api";

@Component({
  selector: 'app-ud-tramitadas',
  templateUrl: './ud-tramitadas.component.html',
})
export class UdTramitadasComponent implements OnInit {

  @Input() solicitudesProcesadas:Observable<any[]>;

  constructor(private _udService:UnidadDocumentalApiService) { }

  ngOnInit() {

     }

}
