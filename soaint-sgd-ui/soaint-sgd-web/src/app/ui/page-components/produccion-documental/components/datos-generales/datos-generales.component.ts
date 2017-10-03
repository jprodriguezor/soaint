import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'pd-datos-generales',
  templateUrl: './datos-generales.component.html'
})

export class PDDatosGeneralesComponent implements OnInit{

  form: FormGroup;


  constructor(private formBuilder: FormBuilder){}


  initForm() {
    this.form = this.formBuilder.group({
      'usuarioResponsable': [null],
      'fechaCreacion': [null],
      'sedeAdministrativa': [{value: null}],
      'dependenciaGrupo': [{value: null}],
    });
  }

  ngOnInit(): void {
    this.initForm();
  }
}

