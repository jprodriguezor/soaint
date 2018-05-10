import { Injectable } from '@angular/core';
import {ApiBase} from "./api-base";
import {Observable} from "rxjs/Observable";
import {environment} from "../../../environments/environment";

@Injectable()
export class FuncionariosService {

  constructor(private _api:ApiBase) { }

  getFuncionarioById(id):Observable<any>{

    return this._api.post(environment.buscarFuncionarios_endpoint,{id:id}).map(data => data.funcionarios[0]);
  }

}
