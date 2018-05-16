import {
  ChangeDetectorRef,
  Component,
  ContentChildren,
  EventEmitter,
  Input,
  OnInit,
  Output,
  QueryList,
  ViewChildren
} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Store} from "@ngrx/store";
import  {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import  {getSelectedDependencyGroupFuncionario} from "../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {DependenciaDTO} from "../../../../../domain/dependenciaDTO";
import {getActiveTask} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {TareaDTO} from "../../../../../domain/tareaDTO";
import {Observable} from "rxjs/Observable";
import {SolicitudCreacionUDDto} from "../../../../../domain/solicitudCreacionUDDto";
import {Dropdown} from "primeng/primeng";
import {CreateUDActionType, EventChangeActionArgs} from "../crear-unidad-documental";
import {SerieService} from "../../../../../infrastructure/api/serie.service";
import {SerieDTO} from "../../../../../domain/serieDTO";
import {Subscription} from "rxjs/Subscription";
import {SupertypeSeries} from "../../shared/supertype-series";
import {SolicitudCreacionUdService} from "../../../../../infrastructure/api/solicitud-creacion-ud.service";
import {isNullOrUndefined} from "util";
import {SolicitudCreacioUdModel} from "../../archivar-documento/models/solicitud-creacio-ud.model";

@Component({
  selector: 'app-lista-solicitud-crear-ud',
  templateUrl: './lista-solicitud-crear-ud.component.html',
})
export class ListaSolicitudCrearUdComponent  implements  OnInit{

  $action = CreateUDActionType;

  action:CreateUDActionType;

  form:FormGroup;
  dependenciaSelected:DependenciaDTO;

  @Input() solicitudModel:SolicitudCreacioUdModel;

  solicitudes$:Observable<SolicitudCreacionUDDto[]> = Observable.empty();

  seriesObservable$:Observable<SerieDTO[]>;

  subseriesObservable$:Observable<any[]>;

  solicitudSelected:SolicitudCreacionUDDto;


  @ViewChildren(Dropdown) dropdowns : QueryList<Dropdown>;

  @Output() changeAction: EventEmitter<EventChangeActionArgs> = new EventEmitter;

  task:TareaDTO;

    constructor(
      private _store:Store<RootState>
      ,private solicitudService: SolicitudCreacionUdService
      ,private changeDetector:ChangeDetectorRef
    ) {

      this._store.select(getActiveTask).subscribe( activeTask => {
      this.task = activeTask;
    });

  }
   selectRow(evt){

     this.solicitudModel.selectSolicitud(evt.data);
  }

  selectAction(index,evt?){

      console.log("accion",evt.value);

  const actionEvent = Object.assign({},
     {solicitud:this.solicitudModel.SolicitudSelected},
     {action:evt.value},
     {nativeEvent:evt}
     );

  this.action = evt.value;

    this.changeAction.emit(actionEvent);
  }

ngOnInit(){

     this.solicitudService.listarSolicitudesNoTramitadas({
      codSede:this.task.variables.codSede,
      codDependencia:this.task.variables.codDependencia,
      idSolicitante: this.task.variables.idSolicitante,
      fechaSolicitud:this.task.variables.fechaSolicitud
    }).subscribe( solicitudes => {
      this.solicitudModel.Solicitudes = solicitudes;
      this.changeDetector.detectChanges();
    });
}

}
