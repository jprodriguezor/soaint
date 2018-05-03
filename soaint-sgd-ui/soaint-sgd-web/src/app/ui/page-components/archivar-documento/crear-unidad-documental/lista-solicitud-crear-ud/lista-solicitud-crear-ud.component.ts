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

  constructor(private fb:FormBuilder,private _store:Store<RootState>) {

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

    const controllers = this.form.controls;

        const map =[
      {origin:'fechaInicio',source:'fechaInicio'},
      {origin:'fechaFin',source:'fechaFin'},
      ];

    this.solicitudes$ = Observable.of(

     JSON.parse(this.task.variables.listaSolicitudes).filter(solicitud => {

         for(let field of map ){
            if( controllers[field.origin].value !== null  ){

             let Origin = controllers[field.origin].value,
             Source = solicitud[field.source];

               switch (field.origin){
                 case 'fechaInicio': if(Source >= (Origin as Date).getTime())
                                       return false;
                  break;
                 case 'fechaFin' : if(Source <= (Origin as Date).getTime())
                                     return false;
                  break;
                }
              }
           }
       return true;
      }
      ));
  }

  selectRow(evt){

     this.solicitudSelected = evt.data;
  }

  selectAction(evt?){

  let actionEvent = Object.assign({},
     {solicitud:this.solicitudSelected},
     {action:this.dropdowns.toArray()[this.solicitudSelected.nro].value},
     {nativeEvent:evt}
     );

    this.changeAction.emit(actionEvent);
  }

ngOnInit(){}

}
