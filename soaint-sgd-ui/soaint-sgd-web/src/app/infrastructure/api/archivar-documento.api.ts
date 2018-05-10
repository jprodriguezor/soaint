import {Injectable} from "@angular/core";
import {ApiBase} from "./api-base";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs/Observable";

@Injectable()
export class ArchivarDocumentoApiService{

   constructor(private _api:ApiBase){
   }

   guardarEstadoTarea(payload?):Observable<any>{
    return  this._api.post(environment.taskStatus_endpoint,payload);
   }

   getTaskData(idProceso,idTraea): Observable<any>{

     return this._api.list(`${environment.restablecer_archivar_documento}${idProceso}/${idTraea}`)
   }
}
