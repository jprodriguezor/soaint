import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'pd-destinatario',
  templateUrl: 'destinatario.component.html'
})

export class PDDatosDestinatarioComponent implements OnInit{

  form: FormGroup;
  tipoComunicacion : number = 1; //1.Externa 2.Interna
  tipoPersona : number = 2; //1.Natural 2.Juridica


  constructor(private formBuilder: FormBuilder){}


  initForm() {
      this.form = this.formBuilder.group({
        'tipoDestinatarioText': [null],
        'tipoDestinatarioList': [{value:null}],
        'tipoDocumentoText': [null],
        'tipoDocumentoList': [{value:null}],
        'tipoPersona': [{value:null}],
        'nombreApellidos': [null],
        'nit': [null],
        'razonSocial': [null],
        'actuaCalidad': [{value:null}],


        'sedeAdministrativa': [{value: false}],
        'dependencia': [{value: false}],
        'funcionario': [{value: false}]
      });
  }

  ngOnInit(): void {


      this.initForm();
  }
}

