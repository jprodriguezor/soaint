import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'pd-datos-destinatario-contacto',
  templateUrl: './datos-destinatario-contacto.component.html'
})

export class PDDatosDestinatarioContactoComponent implements OnInit{

  form: FormGroup;


  constructor(private formBuilder: FormBuilder){}


  initForm() {
      this.form = this.formBuilder.group({

      });
  }

  ngOnInit(): void {
      this.initForm();
  }
}

