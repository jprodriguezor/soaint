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

}
