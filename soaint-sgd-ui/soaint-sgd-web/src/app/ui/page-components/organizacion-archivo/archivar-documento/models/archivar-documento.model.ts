import index from "@angular/cli/lib/cli";
import {UnidadDocumentalDTO} from "../../../../../domain/unidadDocumentalDTO";

export  class ArchivarDocumentoModel{

  constructor(private _unidadDocumental:UnidadDocumentalDTO =null,private _documentos:any[] = []){}

  get UnidadDocumental(){ return this._unidadDocumental}

  set UnidadDocumental(value: any){ this._unidadDocumental = value }

  get Documentos():any[]{ return this._documentos}

  set Documentos(value:any[]){this._documentos = value}


   getUnidadDocumentalParaSalvar():UnidadDocumentalDTO{

     return {
       id:this.UnidadDocumental.id,
       listaDocumentos:this.Documentos.map( doc => {
         delete  doc.identificador;
         delete doc.isAttachment;
         return doc;
       })
     }
   }
}
