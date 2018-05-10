import {Component, Input, OnInit} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {UnidadDocumentalApiService} from "../../../../../../../infrastructure/api/unidad-documental.api";
import  {State as RootState} from "../../../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {getSelectedDependencyGroupFuncionario} from "../../../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";

@Component({
  selector: 'app-lista-documentos-archivados',
  templateUrl: './lista-documentos-archivados.component.html',
})
export class ListaDocumentosArchivadosComponent implements OnInit {

   listaDocumentos$:Observable<any[]> = Observable.empty();

  constructor(private _udService:UnidadDocumentalApiService,private _store:Store<RootState> ) { }

  ngOnInit() {

    this.listaDocumentos$ = this._store.select(getSelectedDependencyGroupFuncionario)
                              .switchMap( dependencia => this._udService.listarDocumentosArchivadosPorDependencia(dependencia.codigo), (outer,inner) => inner)

  }

}
