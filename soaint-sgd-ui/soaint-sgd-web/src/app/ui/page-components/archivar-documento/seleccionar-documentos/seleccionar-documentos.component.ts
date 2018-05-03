import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../../shared/validation-messages';

@Component({
  selector: 'app-seleccionar-documentos',
  templateUrl: './seleccionar-documentos.component.html',
  styleUrls: ['./seleccionar-documentos.component.scss']
})
export class SeleccionarDocumentosComponent implements OnInit, OnDestroy {

  form: FormGroup;

  validations: any = {};

  constructor(private formBuilder: FormBuilder) {
    this.initForm();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'nroSolicitud1': [null],
      'nroSolicitud2': [null]
    });
  }

  ngOnDestroy(): void {

  }

  ngOnInit(): void {

  }

  listenForErrors() {
    this.bindToValidationErrorsOf('nroSolicitud1');
    this.bindToValidationErrorsOf('nroSolicitud2');
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

