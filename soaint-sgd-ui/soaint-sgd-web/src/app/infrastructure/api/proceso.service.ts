import { Injectable } from '@angular/core';
import {State as RootState} from "../redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {getArrayData as ProcesosArrayData} from "../state-management/procesoDTO-state/procesoDTO-selectors";
import {Observable} from "rxjs/Observable";
import {ProcesoDTO} from "../../domain/procesoDTO";
import {process_info} from "../../../environments/environment";

@Injectable()
export class ProcesoService {

  constructor(private  _store:Store<RootState>) { }

  getProcess(codigoProceso?:string):Observable<ProcesoDTO[]>{

     return  this
            ._store
            .select(ProcesosArrayData)
            .map(procesos => {
               return procesos.filter(
                 proceso =>  {
                   return process_info[proceso.codigoProceso] && process_info[proceso.codigoProceso].show && (codigoProceso=== undefined || codigoProceso == proceso.codigoProceso )
                 }
               )
            });
  }
}
