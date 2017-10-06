import {Component, Input, OnInit} from '@angular/core';
import {PlanillaDTO} from "../../../domain/PlanillaDTO";

@Component({
  selector: 'app-planilla-generada',
  templateUrl: './planilla-generada.component.html',
  styleUrls: ['./planilla-generada.component.css']
})
export class PlanillaGeneradaComponent implements OnInit {

  @Input()
  planilla: PlanillaDTO;

  constructor() {
  }

  ngOnInit() {
  }

}
