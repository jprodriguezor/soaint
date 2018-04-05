import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-form-envio',
  templateUrl: './form-envio.component.html',
})
export class FormEnvioComponent implements OnInit {

  form:FormGroup;

  constructor(private fb:FormBuilder) {

    this.form = this.fb.group({
      clase_envio:[null,Validators.required],
      modalidad_correo:[null,Validators.required]
    });
  }

  ngOnInit() {
  }

}
