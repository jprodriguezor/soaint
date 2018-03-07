import { Injectable } from '@angular/core';
import {Sandbox as TaskSandbox} from "../state-management/tareasDTO-state/tareasDTO-sandbox";
import {getActiveTask} from "../state-management/tareasDTO-state/tareasDTO-selectors";
import {TareaDTO} from "../../domain/tareaDTO";
import {State as RootState} from "../redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {SolicitudCreacionUDDto} from "../../domain/solicitudCreacionUDDto";
import {go} from "@ngrx/router-store";
import {ROUTES_PATH} from "../../app.route-names";


@Injectable()
export class SolicitudCreacionUdService {

private task:TareaDTO;

  constructor(private _taskSandbox:TaskSandbox,private _store:Store<RootState>) {
    this._store.select(getActiveTask).subscribe(activeTask =>{console.log(activeTask); this.task = activeTask;})
  }
  solicitarUnidadDocumental(listaSolicitudes:SolicitudCreacionUDDto[]){

    if(this.task.idProceso == "process.archivar-documento"){
      this._taskSandbox.completeTaskDispatch({
        idProceso: this.task.idProceso,
        idDespliegue: this.task.idDespliegue,
        idTarea: this.task.idTarea,
        parametros: {
          creacionUD: true,
          listaSolicitudes: listaSolicitudes,
        }
      });
      this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
    }

  }

}