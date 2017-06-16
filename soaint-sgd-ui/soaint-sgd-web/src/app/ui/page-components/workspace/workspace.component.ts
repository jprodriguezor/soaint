import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-workspace',
  templateUrl: './workspace.component.html'
})
export class WorkspaceComponent implements OnInit {

  tasks: Array<any> = [{idTarea: 1, nombre: 'User Task 1', estado: 'Reservada'}, {
    ideTarea: 2,
    nombre: 'User Task 2',
    estado: 'Completada'
  },];

  selectedTask: any;

  constructor() {
  }

  ngOnInit() {
  }

  printForm() {

  }

}

