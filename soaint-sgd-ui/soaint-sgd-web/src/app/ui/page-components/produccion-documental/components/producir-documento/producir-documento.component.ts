import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'pd-producir-documento',
  templateUrl: './producir-documento.component.html'
})

export class PDProducirDocumentoComponent implements OnInit{

  form: FormGroup;


  constructor(private formBuilder: FormBuilder){}


  initForm() {
    this.form = this.formBuilder.group({
      'tipoComunicacion': [{value: null}],
      'tipoPlantilla': [{value: null}],
      'elaborarDocumento': [null]
    });
  }

  ngOnInit(): void {
    this.initForm();
  }
}

