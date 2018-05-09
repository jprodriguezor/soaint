import {Injectable} from "@angular/core";
import {ApiBase} from "./api-base";
import {environment} from "../../../environments/environment";

@Injectable()
export class ArchivarDocumentoApiService{

   constructor(private _api:ApiBase){
   }

   guardarEstadoTarea(payload?){
     this._api.post(environment.taskStatus_endpoint,payload);
   }
}
