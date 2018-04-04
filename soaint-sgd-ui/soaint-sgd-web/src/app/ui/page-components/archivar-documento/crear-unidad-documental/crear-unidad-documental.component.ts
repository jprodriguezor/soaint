import { Component, OnInit } from '@angular/core';
import {FormGroup} from "@angular/forms";
import {CreateUDActionType, EventChangeActionArgs} from "./crear-unidad-documental";
import {SolicitudCreacionUDDto} from "../../../../domain/solicitudCreacionUDDto";

@Component({
  selector: 'app-crear-unidad-documental',
  templateUrl: './crear-unidad-documental.component.html',
})
export class CrearUnidadDocumentalComponent implements OnInit {

  $action = CreateUDActionType;

  currentAction:CreateUDActionType;

  solicitudSelected:SolicitudCreacionUDDto;

  visiblePopup:boolean = false;

  constructor() { }

  ngOnInit() {
  }

  selectAction(evt:EventChangeActionArgs){

    this.currentAction = evt.action;

    this.solicitudSelected = evt.solicitud;
  }

}
