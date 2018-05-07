import {Component, Input, OnInit} from '@angular/core';
import {ArchivarDocumentoModel} from "../../models/archivar-documento.model";
import {ConfirmationService} from "primeng/primeng";
import {UnidadDocumentalApiService} from "../../../../../../infrastructure/api/unidad-documental.api";
import {Observable} from "rxjs/Observable";
import {Store} from "@ngrx/store";
import  {State as RootState} from "../../../../../../infrastructure/redux-store/redux-reducers";
import {getAuthenticatedFuncionario} from "../../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";

@Component({
  selector: 'app-seleccionar-documentos',
  templateUrl: './seleccionar-documentos.component.html',
  styleUrls: ['./seleccionar-documentos.component.css'],
  providers:[ConfirmationService]
})
export class SeleccionarDocumentosComponent implements OnInit {

  @Input() archivarDocumentoModel:ArchivarDocumentoModel;

  documentos$:Observable<any[]>;

  documentosArchivados$:Observable<any[]>;

  documentPreview:boolean = false;

  idEcm:any;

  constructor(private _confirmationService:ConfirmationService,
              private _udService:UnidadDocumentalApiService,
              private _store:Store<RootState>
  ) { }

  ngOnInit() {
    this.documentos$ =  this._udService.listarDocumentosPorArchivar()
      .map( response => this.groupByDependencie(response.documentoDTOList));
  }

  private groupByDependencie( docs: any[]):any[]{

    if(docs.length === 0)
      return [];

    const documentos = docs.sort( (a,b) => {

      if(a.codigoDependencia == b.codigoDependencia)
        return 0;

      return  a.codigoDependencia >= b.codigoDependencia ? 1 : -1;
    });

    let documentDependency = [];


    let currentDependency = documentos[0].codigoDependencia;

    documentos.forEach( (doc,index) => {

      if(index === 0 || doc.codigoDependencia != currentDependency){

        documentDependency.push([doc]);

        if(index === 0)
          return;

        currentDependency = doc.codigoDependencia;
        return;
      }

      documentDependency[documentDependency.length - 1].push(doc);

    });

    return documentDependency;

  }

  confirmArchivarDocumentos(){

    this._confirmationService.confirm({
      message: '¿Está seguro de archivar el documento en la carpeta XXXXX?',
      header: 'Confirmacion',
      icon: 'fa fa-question-circle',
      accept: () => {

        this._udService.archivarDocumento(this.archivarDocumentoModel)
          .subscribe(() => {

            this.documentos$ =  this._udService.listarDocumentosPorArchivar()
                                    .map( response => this.groupByDependencie(response.documentoDTOList));

            this.documentosArchivados$ =  this._store.select(getAuthenticatedFuncionario)
                                              .switchMap( func => this._udService.listarDocumentosArchivadosPorFuncionario(func.id),(outer,inner) => inner)

          });
      },
      reject: () => {

      }
    });
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

}
