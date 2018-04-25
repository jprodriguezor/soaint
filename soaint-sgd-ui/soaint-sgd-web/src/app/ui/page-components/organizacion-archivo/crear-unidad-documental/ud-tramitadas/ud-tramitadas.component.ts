import {Component, Input, OnInit} from '@angular/core';
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-ud-tramitadas',
  templateUrl: './ud-tramitadas.component.html',
})
export class UdTramitadasComponent implements OnInit {

  @Input() unidadesDocumentales$?:Observable<any[]>;

  constructor() { }

  ngOnInit() {
  }

}
