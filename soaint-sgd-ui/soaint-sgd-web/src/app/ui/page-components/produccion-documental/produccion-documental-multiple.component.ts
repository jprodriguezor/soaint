import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";
import {ProduccionDocumentalApiService} from "app/infrastructure/api/produccion-documental.api";
import {Observable} from "rxjs/Observable";
import {ConstanteDTO} from "app/domain/constanteDTO";
import {VALIDATION_MESSAGES} from "../../../shared/validation-messages";

@Component({
  selector: 'produccion-documental-multiple',
  templateUrl: './produccion-documental-multiple.component.html',
  styleUrls: ['produccion-documental.component.css'],
})

export class ProduccionDocumentalMultipleComponent implements OnInit{


  constructor(private _produccionDocumentalApi : ProduccionDocumentalApiService,
              private formBuilder: FormBuilder) {  }

  form: FormGroup;
  validations: any = {};

  sedesAdministrativas$ : Observable<ConstanteDTO[]>;
  dependencias$ : Observable<ConstanteDTO[]>;
  funcionarios$ : Observable<ConstanteDTO[]>;



  initForm() {
    this.form = this.formBuilder.group({
      //Datos generales
      'sede': [{value: null}],
      'dependencia': [{value: null}],
      'funcionario': [{value: null}],
      'tipoPlantilla': [{value:null}],
    });
  }

  ngOnInit(): void {
    this.sedesAdministrativas$ = this._produccionDocumentalApi.getSedes({});
    this.dependencias$ = this._produccionDocumentalApi.getDependencias({});
    this.funcionarios$ = this._produccionDocumentalApi.getFuncionarios({});

    this.initForm();
  }



  listenForErrors() {
    this.bindToValidationErrorsOf('tipoComunicacion');
  }

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);
    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    }
  }

  bindToValidationErrorsOf(control: string) {
    const ac = this.form.get(control);
    ac.valueChanges.subscribe(value => {
      if ((ac.touched || ac.dirty) && ac.errors) {
        const error_keys = Object.keys(ac.errors);
        const last_error_key = error_keys[error_keys.length - 1];
        this.validations[control] = VALIDATION_MESSAGES[last_error_key];
      } else {
        delete this.validations[control];
      }
    });
  }
}
