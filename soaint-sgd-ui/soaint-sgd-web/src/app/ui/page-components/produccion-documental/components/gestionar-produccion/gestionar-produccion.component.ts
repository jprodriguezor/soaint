import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'pd-gestionar-produccion',
  templateUrl: './gestionar-produccion.component.html'
})

export class PDGestionarProduccionComponent implements OnInit{

  form: FormGroup;
  listaDocumentos: Array<{ sede: string, dependencia: string, rol: string, funcionario: string }> = [];


  constructor(private formBuilder: FormBuilder){}


  initForm() {
      this.form = this.formBuilder.group({
          'sede': [{value: null}],
          'dependencia': [{value: null}],
          'rol': [{value: null}],
          'funcionario': [{value: null}]
      });
  }

  ngOnInit(): void {
      this.initForm();
  }
}

