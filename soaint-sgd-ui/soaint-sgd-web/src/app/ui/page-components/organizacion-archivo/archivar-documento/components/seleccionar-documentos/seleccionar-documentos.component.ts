import {Component, Input, OnInit} from '@angular/core';
import {ArchivarDocumentoModel} from "../../models/archivar-documento.model";
import {ConfirmationService} from "primeng/primeng";
import {UnidadDocumentalApiService} from "../../../../../../infrastructure/api/unidad-documental.api";
import {SolicitudCreacioUdModel} from "../../models/solicitud-creacio-ud.model";

@Component({
  selector: 'app-seleccionar-documentos',
  templateUrl: './seleccionar-documentos.component.html',
  styleUrls: ['./seleccionar-documentos.component.css'],
  providers:[ConfirmationService]
})
export class SeleccionarDocumentosComponent implements OnInit {

  @Input() archivarDocumentoModel:ArchivarDocumentoModel;


  constructor(private _confirmationService:ConfirmationService) { }

  ngOnInit() {
  }

  confirmArchivarDocumentos(){

    this._confirmationService.confirm({
      message: '¿Está seguro de archivar el documento en la carpeta XXXXX?',
      header: 'Confirmacion',
      icon: 'fa fa-question-circle',
      accept: () => {

        //this._udService.archivarDocumento({});

      },
      reject: () => {

      }
    });
  }

}
