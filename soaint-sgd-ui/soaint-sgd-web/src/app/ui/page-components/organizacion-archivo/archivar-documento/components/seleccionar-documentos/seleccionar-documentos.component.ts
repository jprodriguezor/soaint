import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ArchivarDocumentoModel} from "../../models/archivar-documento.model";
import {ConfirmationService} from "primeng/primeng";
import {UnidadDocumentalApiService} from "../../../../../../infrastructure/api/unidad-documental.api";
import {Observable} from "rxjs/Observable";
import {Store} from "@ngrx/store";
import  {State as RootState} from "../../../../../../infrastructure/redux-store/redux-reducers";
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {isNullOrUndefined} from "util";
import {TASK_PRODUCIR_DOCUMENTO} from "../../../../../../infrastructure/state-management/tareasDTO-state/task-properties";
import {AnexoDTO} from "../../../../produccion-documental/models/DocumentoDTO";
import {combineLatest} from "rxjs/observable/combineLatest";
import {Subscription} from "rxjs/Subscription";
import {DependenciaDTO} from "../../../../../../domain/dependenciaDTO";
import {environment} from "../../../../../../../environments/environment";

@Component({
  selector: 'app-seleccionar-documentos',
  templateUrl: './seleccionar-documentos.component.html',
  styleUrls: ['./seleccionar-documentos.component.css'],
  providers:[ConfirmationService]
})
export class SeleccionarDocumentosComponent implements OnInit,OnDestroy {

  @Input() archivarDocumentoModel:ArchivarDocumentoModel;

    documentos:any[];

  documentosArchivados$:Observable<any[]>;

  documentosAdjuntos:any[];

  documentPreview:boolean = false;

  dependenciaSelected:DependenciaDTO;

  idEcm:any;

  nombreDocumento:string;

  idDocumento:string;

  subscriptions:Subscription[] = [];

  constructor(private _confirmationService:ConfirmationService,
              private _udService:UnidadDocumentalApiService,
              private _store:Store<RootState>
  ) { }

  ngOnInit() {

    this.FillListasDocumentos();
  }

  confirmArchivarDocumentos(){

    this._confirmationService.confirm({
      message: `¿Está seguro de archivar el documento en la carpeta ${this.archivarDocumentoModel.UnidadDocumental.nombreUnidadDocumental}?`,
      header: 'Confirmacion',
      icon: 'fa fa-question-circle',
      accept: () => {
      this._udService.archivarDocumento(this.archivarDocumentoModel.getUnidadDocumentalParaSalvar())
          .subscribe(this.FillListasDocumentos);
      },
      reject: () => {

      }
    });
  }

  private FillListasDocumentos(){

    const observable = this._store.select(getSelectedDependencyGroupFuncionario).share();

    this.setDocumentosPorArvichar(observable);

    this.documentosArchivados$ = observable.switchMap(dependencia => this._udService
      .listarDocumentosArchivadosPorDependencia(dependencia));
  }

  private setDocumentosPorArvichar(observable:Observable<DependenciaDTO>){

    observable.switchMap(dependencia => {

      return this._udService.listarDocumentosPorArchivar(dependencia.codigo)
        .map( response => response.documentoDTOList.map( doc => {

          doc.identificador = !isNullOrUndefined(doc.nroRadicado) && doc.nroRadicado != 'null' ? doc.nroRadicado : doc.idDocumento;
          doc.isAttachment = true;
          return doc;
        }),(outer,inner) => inner);
    })
      .subscribe(documentos => this.documentos = documentos);

  }

  toggleDocuments(event:any,checked:boolean){

    if(checked)
     this.archivarDocumentoModel.Documentos.push(event.data);
    else{

      const index  = this.archivarDocumentoModel.Documentos
                     .findIndex( document => document.idDocumento == event.data.idDocumento);

      this.archivarDocumentoModel.Documentos.splice(index,1);
    }
  }

  showDocumento(idDocumento){

    this.idEcm = idDocumento;

    this.documentPreview = true;
  }

  hidePdf(){

    this.documentPreview = false;
  }

  uploadDocuments(event:any){

    console.log("enter to method");

      this._store.select(getSelectedDependencyGroupFuncionario).subscribe( dependecia => {

        const formData = new FormData();


        console.log("catchin dependencia");

        formData.append('files[]',event.files);

        formData.append("codigoDependencia",dependecia.codigo);

        this._udService.subirDocumentosParaArchivar(formData).subscribe();
      });
  }

  eliminarDocumento(idEcm,index){

    // usar api documento ecm

    this.documentosAdjuntos.splice(index,1);
  }

  agregarAdjuntos(){

  this.documentos = this.documentosAdjuntos.map( doc => Object.assign({isAttachment:true,identificador:doc.idDocumento},doc));

  }

  onErrorUpload(event){

    console.log("Hola");
  }

  updateDocumentMeta(index,field){

    switch (field){
      case "nombreDocumento" : this.documentos[index][field] = this.nombreDocumento;
        break;
      case "idDocumento" : this.documentos[index][field] = this.idDocumento;
        break;
    }
  }

  ngOnDestroy(): void {

    //this.subscriptions.forEach( subscription => subscription.unsubscribe());
  }

}
