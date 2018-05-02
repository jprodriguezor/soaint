import {Component, Input, OnInit} from '@angular/core';
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-lista-documentos-archivados',
  templateUrl: './lista-documentos-archivados.component.html',
})
export class ListaDocumentosArchivadosComponent implements OnInit {

  @Input() listaDocumentos$:Observable<any[]> = Observable.empty();

  constructor() { }

  ngOnInit() {
  }

}
