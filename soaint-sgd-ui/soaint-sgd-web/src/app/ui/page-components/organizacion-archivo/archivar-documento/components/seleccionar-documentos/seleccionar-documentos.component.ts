import {ChangeDetectorRef, Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ArchivarDocumentoModel} from "../../models/archivar-documento.model";
import {ConfirmationService, FileUpload} from "primeng/primeng";
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
import {TipologiaDocumentaService} from "../../../../../../infrastructure/api/tipologia-documenta.service";
import {oa_dataSource} from "../../../data-source";
import {PushNotificationAction} from "../../../../../../infrastructure/state-management/notifications-state/notifications-actions";

@Component({
  selector: 'app-seleccionar-documentos',
  templateUrl: './seleccionar-documentos.component.html',
  styleUrls: ['./seleccionar-documentos.component.css'],
  providers:[ConfirmationService]
})
export class SeleccionarDocumentosComponent implements OnInit,OnDestroy {

  @Input() archivarDocumentoModel:ArchivarDocumentoModel;

    documentos:any[];

  documentosArchivados:any[];

  documentosAdjuntos:any[] = [];

  documentPreview:boolean = false;

  dependenciaSelected:DependenciaDTO;

  idEcm:any;

  nombreDocumento:string;

  idDocumento:string;

  tipologiaDocumental:string;

  subscriptions:Subscription[] = [];

  tipologiaDocumentales$:Observable<any[]>;

  constructor(private _confirmationService:ConfirmationService,
              private _udService:UnidadDocumentalApiService,
              private _store:Store<RootState>,
              private _tipologiaService:TipologiaDocumentaService,
              private changeDetectorRef:ChangeDetectorRef
  ) { }

  ngOnInit() {

    this.FillListasDocumentos();

    this.tipologiaDocumentales$ = this._tipologiaService.getTiplogiasDocumentales();
  }

  confirmArchivarDocumentos(){

    this._confirmationService.confirm({
      message: `¿Está seguro de archivar el documento en la carpeta ${this.archivarDocumentoModel.UnidadDocumental.nombreUnidadDocumental}?`,
      header: 'Confirmacion',
      icon: 'fa fa-question-circle',
      accept: () => {

        const validDocuments = this.archivarDocumentoModel
                               .Documentos
                               .every( document => !isNullOrUndefined(document.tipologiaDocumental));

        if(!validDocuments){

          this._store.dispatch(new PushNotificationAction({
            severity: 'error', summary: 'Algun documento que desea archivar no tiene la tipologia documental. Revise e agregela'
          }));

          return;
        }

      this._udService.archivarDocumento(this.archivarDocumentoModel.getUnidadDocumentalParaSalvar())
          .subscribe(this.FillListasDocumentos);
      },
      reject: () => {

      }
    });
  }

  private FillListasDocumentos(){

    const observable = this._store.select(getSelectedDependencyGroupFuncionario);

    this.setDocumentosPorArvichar(observable);

    this.subscriptions.push(observable.switchMap(dependencia =>  this._udService
      .listarDocumentosArchivadosPorDependencia(dependencia.codigo))
      .subscribe( docs => {
        this.documentosArchivados = docs;
        this.changeDetectorRef.detectChanges();
      }));

    this.changeDetectorRef.detectChanges();
  }

  private setDocumentosPorArvichar(observable:Observable<DependenciaDTO>){

    this.subscriptions.push(
      observable.switchMap(dependencia => {

        return this._udService.listarDocumentosPorArchivar(dependencia.codigo)
          .map( response => response.documentoDTOList.map( doc => {


            doc.identificador = !isNullOrUndefined(doc.nroRadicado) && doc.nroRadicado != 'null' ? doc.nroRadicado : doc.idDocumento;
            doc.isAttachment = false;
            return doc;
          }),(outer,inner) => inner);
      })
        .subscribe(documentos => {  this.documentos = documentos; this.changeDetectorRef.detectChanges();})
    );
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

  uploadDocuments(event:any,uploader:FileUpload){

    console.log("enter to method");

    this.subscriptions.push(
      this._store.select(getSelectedDependencyGroupFuncionario).subscribe( dependecia => {

        const formData = new FormData();


        console.log("catchin dependencia");

        event.files.forEach( file => {
          formData.append('file',file,file.name);
        });

       // formData.append('files[]',event.files);

        formData.append("codigoDependencia",dependecia.codigo);

        this.subscriptions.push(
          this._udService.subirDocumentosParaArchivar(formData)
            //.mapTo(oa_dataSource.documentos_adjuntos_por_archivar)
            .subscribe( documents => {

              if(documents.codMensaje == 1224){

                this._store.dispatch(new PushNotificationAction({
                  severity: 'error', summary: 'DOCUMENTO DUPLICADO, NO PUEDE ADJUNTAR EL DOCUMENTO'
                }));
              }

              else{
                const docs = documents.documentoDTOList;

                if(!isNullOrUndefined(docs) && docs.length > 0)
                  this.documentosAdjuntos =  this.documentosAdjuntos.concat(docs);

                this.changeDetectorRef.detectChanges();
              }

            },null,() => {
              uploader.clear();
              this.changeDetectorRef.detectChanges();
            }));
      })
    );


  }

  eliminarDocumento(idEcm,index){

    // usar api documento ecm

    this.documentosAdjuntos.splice(index,1);
  }

  agregarAdjuntos(){

  this.documentos = this.documentosAdjuntos
                    .map( doc => Object.assign({isAttachment:true,identificador:doc.idDocumento},doc))
                    .concat(this.documentos) ;

  this.documentosAdjuntos = [];

  this.changeDetectorRef.detectChanges();
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
      case "tipologiaDocumental":  this.documentos[index][field] = this.tipologiaDocumental;
        break;
    }
  }

  DropSubscriptions(){
    this.subscriptions.forEach( subscription => subscription.unsubscribe());

  }

  ngOnDestroy(): void {

    this.DropSubscriptions();
  }

  IsSelected(id):boolean{

    return this.archivarDocumentoModel.Documentos.some( document => document.idDocumento == id);
  }

}
