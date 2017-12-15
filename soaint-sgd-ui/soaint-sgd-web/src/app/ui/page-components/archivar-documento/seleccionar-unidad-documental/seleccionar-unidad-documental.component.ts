import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../../shared/validation-messages';

@Component({
  selector: 'app-seleccionar-unidad-documental',
  templateUrl: './seleccionar-unidad-documental.component.html',
  styleUrls: ['./seleccionar-unidad-documental.component.scss']
})
export class SeleccionarUnidadDocumentalComponent implements OnInit, OnDestroy {

  form: FormGroup;

  series: Array<any> = [];

  subseries: Array<any> = [];

  validations: any = {};

  constructor(private formBuilder: FormBuilder) {
    this.initForm();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'serie': [null, Validators.required],
      'subserie': [null, Validators.required],
      'identificador': [null, Validators.required],
      'nombre': [null, Validators.required],
      'descriptor1': [null],
      'descriptor2': [null],
      'observaciones': [null]
    });
  }

  ngOnDestroy(): void {

  }

  ngOnInit(): void {

  }

  listenForErrors() {
    this.bindToValidationErrorsOf('tipoComunicacion');
    this.bindToValidationErrorsOf('tipologiaDocumental');
    this.bindToValidationErrorsOf('numeroFolio');
    this.bindToValidationErrorsOf('asunto');
    this.bindToValidationErrorsOf('tipoAnexosDescripcion');
    this.bindToValidationErrorsOf('empresaMensajeria');
    this.bindToValidationErrorsOf('numeroGuia');
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
    ac.valueChanges.subscribe(() => {
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

