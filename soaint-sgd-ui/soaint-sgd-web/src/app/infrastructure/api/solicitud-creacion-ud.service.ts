import { Injectable } from '@angular/core';
import {Sandbox as TaskSandbox} from "../state-management/tareasDTO-state/tareasDTO-sandbox";
import {getActiveTask} from "../state-management/tareasDTO-state/tareasDTO-selectors";
import {TareaDTO} from "../../domain/tareaDTO";
import {State as RootState} from "../redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {SolicitudCreacionUDDto} from "../../domain/solicitudCreacionUDDto";
import {UnidadDocumentalApiService} from "./unidad-documental.api";
import {Observable} from "rxjs/Observable";
import {environment} from "../../../environments/environment";
import {ApiBase} from "./api-base";
import {oa_dataSource} from "../../ui/page-components/organizacion-archivo/data-source";

@Injectable()
export class SolicitudCreacionUdService {

private task:TareaDTO;

  constructor(private _taskSandbox:TaskSandbox,private _store:Store<RootState>,private _api:ApiBase) {

    this._store.select(getActiveTask).subscribe(activeTask => {this.task = activeTask});

  }
  solicitarUnidadDocumental(listaSolicitudes:any){


    if(this.task.idProceso == "process.archivar-documento"){

      return this._api.post(environment.crear_solicitud_ud,listaSolicitudes)
        .subscribe(() => {

          this._taskSandbox.completeTaskDispatch({
            idProceso: this.task.idProceso,
            idDespliegue: this.task.idDespliegue,
            idTarea: this.task.idTarea,
            parametros: {
              creacionUD: 1,
            }
          });
        });



    }
  }

  crearSolicitudCreacionUD(payload:any):Observable<any>{

   // return  Observable.of(oa_dataSource.crear_solicitud_ud);

    return this._api.post(environment.crear_solicitud_ud,payload);
  }

  listarSolicitudes(payload:any):Observable<any>{
    return this._api.list(environment.listar_solicitud_ud,payload);
  }

  actualizarSolicitudes(payload:any):Observable<any>{
    return this._api.put(environment.actualizar_solicitud_ud,payload);
  }
}
