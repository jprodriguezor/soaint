import {Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild, ViewEncapsulation} from '@angular/core';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import 'rxjs/add/operator/single';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {ApiBase} from '../../../infrastructure/api/api-base';
import {environment} from '../../../../environments/environment';
import {LoadingService} from '../../../infrastructure/utils/loading.service';


@Component({
  selector: 'app-documentos-ecm-list',
  templateUrl: './documentos-ecm-list.component.html',
  styleUrls:['./documentos-ecm-list.css']
})
export class DocumentosECMListComponent implements OnInit, OnChanges {


  @Input()
  versionar = false;

  @Input()
  idDocumentECM: string;

  docSrc = '';
  isLoading = false;

  documentsList: any;
  uploadUrl: String;
  editable = true;

  constructor(private _store: Store<State>, private _api: ApiBase, public loadingService: LoadingService) {
  }

  ngOnInit() {
   // this.loadDocumentos();
  }

  ngOnChanges(): void {
    this.loadDocumentos();
  }

  loadDocumentos() { console.log(this.idDocumentECM);
    if (this.idDocumentECM !== undefined) {
      // const idDocumentECM = this.comunicacion.ppdDocumentoList[0].ideEcm;
      // console.log('ID del ecm');
      // console.log(this.comunicacion.ppdDocumentoList);
      const endpoint = `${environment.obtenerDocumento_asociados_endpoint}` + '/' + this.idDocumentECM;
      this._api.list(endpoint).subscribe(response => {
        this.documentsList = [];
        if (response.codMensaje === '0000') {
          this.documentsList = response.documentoDTOList;
        }
      });
    }
  }

  setDataDocument(data: any) {
    this.idDocumentECM = data.comunicacion;
    this.versionar = data.versionar;
  }

  showDocument(idDocumento: string) {
    this.loadingService.presentLoading();
    this.docSrc = environment.obtenerDocumento + idDocumento;
  }

  hideDocument() {
    this.docSrc = '';
  }

  docLoaded() {
    this.loadingService.dismissLoading();
  }

}
