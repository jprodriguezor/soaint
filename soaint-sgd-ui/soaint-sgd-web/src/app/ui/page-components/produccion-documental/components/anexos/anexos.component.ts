import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'pd-anexos',
  templateUrl: './anexos.component.html'
})

export class PDAnexosComponent implements OnInit{

  form: FormGroup;


  constructor(private formBuilder: FormBuilder){}


  initForm() {
    this.form = this.formBuilder.group({
      'usuarioResponsable':"electronico",
      'tipoAnexo': [{value: null}],
      'descripcion': [null],
    });
  }

  ngOnInit(): void {
    this.initForm();
  }
}

