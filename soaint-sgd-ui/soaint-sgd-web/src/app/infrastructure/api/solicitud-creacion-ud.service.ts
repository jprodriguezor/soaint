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
  solicitarUnidadDocumental(listaSolicitudes:any):Observable<any>{

      return this._api.post(environment.crear_solicitud_ud,listaSolicitudes);
  }

  crearSolicitudCreacionUD(payload:any):Observable<any>{

  // return  Observable.of(oa_dataSource.crear_solicitud_ud);

    console.log(payload);

    return this._api.post(environment.crear_solicitud_ud,payload);
  }

  listarSolicitudes(payload:any):Observable<any>{
    return this._api.list(environment.listar_solicitud_ud,payload)
      .map(response => response.solicitudesUnidadDocumentalDTOS);
  }

  actualizarSolicitudes(payload:any):Observable<any>{
    return this._api.put(environment.actualizar_solicitud_ud,payload);
  }
}
