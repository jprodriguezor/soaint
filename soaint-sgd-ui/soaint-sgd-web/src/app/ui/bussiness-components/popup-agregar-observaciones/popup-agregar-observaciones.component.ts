import {Component, OnInit} from '@angular/core';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import {PpdTrazDocumentoDTO} from '../../../domain/PpdTrazDocumentoDTO';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';


@Component({
  selector: 'app-popup-agregar-observaciones',
  templateUrl: './popup-agregar-observaciones.component.html',
})
export class PopupAgregarObservacionesComponent implements OnInit {

  form: FormGroup;

  validations = {};

  observaciones: PpdTrazDocumentoDTO[];

  constructor(private formBuilder: FormBuilder, private _asignacionSandbox: AsignacionSandbox) {
    this.initForm();
    this.listenForErrors();
  }


  ngOnInit(): void {
  }

  initForm() {
    this.form = this.formBuilder.group({
      'observacion': [{value: null, disabled: false}, Validators.required]
    });
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('observacion');
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

  hideDialog() {
    this._asignacionSandbox.setVisibleAddObservationsDialogDispatch(false);
  }

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);
    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    }
  }

  addObservation() {

  }
}