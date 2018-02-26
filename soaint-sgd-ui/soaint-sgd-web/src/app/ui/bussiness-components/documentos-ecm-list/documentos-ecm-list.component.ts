import {Component, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild, ViewEncapsulation} from '@angular/core';
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
export class DocumentosECMListComponent implements OnChanges {


  @Input()
  versionar = false;

  @Input()
  idDocumentECM: string;

  docSrc:string="";


  documentsList: any;
  uploadUrl: String;
  editable = true;

  constructor(private _store: Store<State>, private _api: ApiBase) {
  }

   ngOnChanges(): void {

    this.loadDocumentos();
  }

  loadDocumentos() {
    console.log(this.idDocumentECM);

    if(this.idDocumentECM!== undefined){
      // const idDocumentECM = this.comunicacion.ppdDocumentoList[0].ideEcm;
      // console.log('ID del ecm');
      // console.log(this.comunicacion.ppdDocumentoList);
      const endpoint = `${environment.pd_gestion_documental.obtenerAnexo}` + '/' + this.idDocumentECM;
      console.log(endpoint);

      this._api.post(endpoint).subscribe(response => {

        this.documentsList = response.metadatosDocumentosDTOList;

      });

      /*map(value => {
        console.log(value);
      });*/
    }
  }

  setDataDocument(data: any) {
    this.idDocumentECM = data.comunicacion;
    this.versionar = data.versionar;
  }

  showDocument(idDocumento:string){

   this.docSrc =  environment.obtenerDocumento + idDocumento;
  }

  hideDocument(){

    this.docSrc = '';
  }

}
