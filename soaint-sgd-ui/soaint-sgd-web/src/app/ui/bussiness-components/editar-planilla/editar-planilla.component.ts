import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-editar-planilla',
  templateUrl: './editar-planilla.component.html',
  styleUrls: ['./editar-planilla.component.css']
})
export class EditarPlanillaComponent implements OnInit {

  form: FormGroup;

  estadoEntregaSuggestions: any[];

  constructor(private formBuilder: FormBuilder) {
    this.initForm();
  }

  ngOnInit() {
  }

  initForm() {
    this.form = this.formBuilder.group({
      estadoEntrega: [null],
      fechaEntrega: [null],
      observaciones: [null]
    });
  }

}
