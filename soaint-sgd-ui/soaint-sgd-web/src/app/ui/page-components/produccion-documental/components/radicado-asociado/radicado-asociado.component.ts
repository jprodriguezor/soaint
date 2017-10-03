import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'pd-radicado-asociado',
  templateUrl: './radicado-asociado.component.html'
})

export class PDRadicadoAsociadoComponent implements OnInit{

  form: FormGroup;


  constructor(private formBuilder: FormBuilder){}


  initForm() {
    this.form = this.formBuilder.group({
      'fechaRadicacion': [null],
      'noRadicado': [null],
    });
  }

  ngOnInit(): void {
    this.initForm();
  }
}

