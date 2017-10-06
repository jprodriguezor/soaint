import {Component, Input, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";
import {ConstanteDTO} from "../../../../../domain/constanteDTO";

@Component({
  selector: 'pd-datos-contacto',
  templateUrl: 'datos-contacto.component.html'
})

export class PDDatosContactoComponent implements OnInit{

  form: FormGroup;

  @Input() tipoComunicacion : ConstanteDTO;


  constructor(private formBuilder: FormBuilder){}


  initForm() {
      this.form = this.formBuilder.group({
        'fisica': [{value: false}, Validators.required],
        'electronica': [{value: false}, Validators.required],
        'responseToRem': [{value: false}, Validators.required]
      });
  }

  ngOnInit(): void {


      this.initForm();
  }
}

