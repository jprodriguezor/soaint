import { Component, OnInit } from '@angular/core';
import {CreateUDActionType, EventChangeActionArgs} from "./crear-unidad-documental";
import {SolicitudCreacionUDDto} from "../../../../domain/solicitudCreacionUDDto";
import {Observable} from "rxjs/Observable";
import {UnidadDocumentalApiService} from "../../../../infrastructure/api/unidad-documental.api";
import {State as RootState} from "../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {getSelectedDependencyGroupFuncionario} from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";

@Component({
  selector: 'app-crear-unidad-documental',
  templateUrl: './crear-unidad-documental.component.html',
})
export class CrearUnidadDocumentalComponent implements OnInit {

  $action = CreateUDActionType;

  currentAction:CreateUDActionType;

  solicitudSelected:SolicitudCreacionUDDto;

  solicitudesProcesadas$:Observable<any[]> = Observable.empty();



  visiblePopup:boolean = false;

  constructor(private _udService:UnidadDocumentalApiService,private _store:Store<RootState>) { }

  ngOnInit() {

    this.actualizarSolicitudesTramitadas()
  }

  selectAction(evt:EventChangeActionArgs){

    this.currentAction = evt.action;

    this.solicitudSelected = evt.solicitud;
  }

  actualizarSolicitudesTramitadas(){

    this.solicitudesProcesadas$ = this._store.select(getSelectedDependencyGroupFuncionario)
      .switchMap(
        dependencia =>  this._udService.listarUnidadesDocumentales({ codigoDependencia:dependencia.codigo }),
        (outerValue,innerValue) => innerValue );


  }

  closePopup(){}

}
