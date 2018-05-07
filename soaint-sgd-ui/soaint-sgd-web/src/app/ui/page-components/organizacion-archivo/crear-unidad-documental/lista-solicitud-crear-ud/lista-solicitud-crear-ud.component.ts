import {Component, ContentChildren, EventEmitter, OnInit, Output, QueryList, ViewChildren} from '@angular/core';
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

@Component({
  selector: 'app-lista-solicitud-crear-ud',
  templateUrl: './lista-solicitud-crear-ud.component.html',
})
export class ListaSolicitudCrearUdComponent  implements  OnInit{

  $action = CreateUDActionType;

  form:FormGroup;
  dependenciaSelected:DependenciaDTO;

  solicitudes$:Observable<SolicitudCreacionUDDto[]> = Observable.empty();

  seriesObservable$:Observable<SerieDTO[]>;

  subseriesObservable$:Observable<any[]>;

  solicitudSelected:SolicitudCreacionUDDto;

  dependenciaSelected$ : Observable<any>;

  globalDependencySubscriptor:Subscription;

  @ViewChildren(Dropdown) dropdowns : QueryList<Dropdown>;

  @Output() changeAction: EventEmitter<EventChangeActionArgs> = new EventEmitter;

  task:TareaDTO;

  currentAction?:string;

  constructor(private fb:FormBuilder,private _store:Store<RootState>,private solicitudService: SolicitudCreacionUdService) {

    this.formInit();

    this._store.select(getSelectedDependencyGroupFuncionario).subscribe( response => {

      this.dependenciaSelected = response;

      this.form.controls["sede"].setValue(response.nomSede);
      this.form.controls["dependencia"].setValue(response.nombre);

    });

    this._store.select(getActiveTask).subscribe( activeTask => {
      this.task = activeTask;
    });

  }

  private formInit(){
    this.form =  this.fb.group({
      "sede"        : [{value:null,disabled:true}],
      "dependencia" : [{value:null,disabled:true}],
      "fechaInicio" : [null],
      "fechaFin"    : [null]
    });
  }

  filtrarSolicitud(){

    let request:any = {
      codSede: this.dependenciaSelected.codSede,
      codDependencia: this.dependenciaSelected.codigo,
    };

    if(!isNullOrUndefined(this.form.get("fechaInicio").value))
      request.fechaIni = this.form.get("fechaInicio").value;

    if(!isNullOrUndefined(this.form.get("fechaFin").value))
      request.fechaFin = this.form.get("fechaFin").value;



    this.solicitudes$ = this.solicitudService.listarSolicitudes(request);
                           // .map( r => r.response.unidadesDocumental);
  }

  selectRow(evt){

     this.solicitudSelected = evt.data;
  }

  selectAction(evt?){

  const actionEvent = Object.assign({},
     {solicitud:this.solicitudSelected},
     {action:this.dropdowns.toArray()[this.solicitudSelected.nro].value},
     {nativeEvent:evt}
     );

    this.changeAction.emit(actionEvent);
  }

ngOnInit(){}

}
