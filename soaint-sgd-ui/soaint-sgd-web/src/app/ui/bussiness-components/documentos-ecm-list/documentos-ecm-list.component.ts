import {Component, EventEmitter, Input, OnInit, Output, ViewChild, ViewEncapsulation} from '@angular/core';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import 'rxjs/add/operator/single';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {ApiBase} from '../../../infrastructure/api/api-base';
import {environment} from '../../../../environments/environment';


@Component({
  selector: 'app-documentos-ecm-list',
  templateUrl: './documentos-ecm-list.component.html'
})
export class DocumentosECMListComponent implements OnInit {

  @Input()
  versionar = false;

  @Input()
  comunicacion: ComunicacionOficialDTO;
  documentsList: any;
  uploadUrl: String;

  constructor(private _store: Store<State>, private _api: ApiBase) {
  }

  ngOnInit(): void {
  }

  loadDocumentos() {
    console.log(this.comunicacion);
    const idDocumentECM = this.comunicacion.ppdDocumentoList[0].ideEcm;
    const endpoint = `${environment.digitalizar_doc_upload_endpoint}` + '/obtenerdocumentosasociados/' + idDocumentECM;
    console.log(endpoint);
    this._api.list(endpoint).map(value => {
      console.log(value);
    });
  }

  setDataDocument(data: any) {
    this.comunicacion = data.comunicacion;
    this.versionar = data.versionar;
  }

}
