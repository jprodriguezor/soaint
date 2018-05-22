import { Injectable } from '@angular/core';
import {ApiBase} from "./api-base";
import {ComunicacionOficialDTO} from "../../domain/comunicacionOficialDTO";
import {environment} from "../../../environments/environment";

@Injectable()
export class RadicacionSalidaService {

  constructor(private _api:ApiBase) { }

  radicar(comunicacion:ComunicacionOficialDTO){

    return this._api.post(environment.radicarSalida_endpoint,comunicacion);
  }

  quickSave(payload: any) {
    return this._api.post(environment.salvarCorrespondenciaEntrada_endpoint, payload);
  }

  quickRestore(idproceso: string, idtarea: string) {
    const endpoint = environment.restablecerCorrespondenciaEntrada_endpoint;
    return this._api.list(`${endpoint}/${idproceso}/${idtarea}`);
  }

  uploadTemplate(payload:FormData){

    return this._api.sendFile(environment.upload_template,payload,[]);
  }

}
