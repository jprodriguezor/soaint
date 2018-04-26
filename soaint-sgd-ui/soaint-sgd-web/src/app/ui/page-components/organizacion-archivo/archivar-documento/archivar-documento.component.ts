import { Component, OnInit } from '@angular/core';
import {ArchivarDocumentoModel} from "./models/archivar-documento.model";
import {SolicitudCreacioUdModel} from "./models/solicitud-creacio-ud.model";
import {SolicitudCreacionUdService} from "../../../../infrastructure/api/solicitud-creacion-ud.service";

@Component({
  selector: 'app-archivar-documento',
  templateUrl: './archivar-documento.component.html',
  styleUrls: ['./archivar-documento.component.css']
})
export class ArchivarDocumentoComponent implements OnInit {

   archivarDocumentoModel:ArchivarDocumentoModel = new ArchivarDocumentoModel();

   solicitudUDModel: SolicitudCreacioUdModel;

   currentPage:number = 1;

   enableButtonNext:boolean = true;



  constructor(private _solicitudService:SolicitudCreacionUdService) {

    this.solicitudUDModel = new SolicitudCreacioUdModel(this._solicitudService);
  }

  ngOnInit() {

    }

  next(){

    this.currentPage++;
  }

  prev(){

    this.currentPage --;
  }

  save(){

    if(this.enableButtonNext){

      this.archivarDocumentoModel.Archivar();
    }
    else{

      this.solicitudUDModel.Solicitar();
    }

  }

  toggleEnableButtonNext(section:string){

    this.enableButtonNext = section == "bUnidadDocumental";
  }

  buttonSaveIsEnabled():boolean
  {

    if(this.enableButtonNext)
      return true;
    return this.solicitudUDModel.Solicitudes.length > 0;

  }
}
