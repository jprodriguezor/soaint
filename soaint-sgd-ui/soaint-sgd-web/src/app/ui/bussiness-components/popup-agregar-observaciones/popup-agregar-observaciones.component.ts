import {ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {ObservacionDTO} from "../../../domain/observacionDTO";


@Component({
  selector: 'app-popup-agregar-observaciones',
  templateUrl: './popup-agregar-observaciones.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PopupAgregarObservacionesComponent implements OnInit {

  form: FormGroup;

  validations = {};

  observaciones: ObservacionDTO[];

  @Input()
  idDocumento: number;

  @Input()
  idFuncionario: number;

  constructor(private formBuilder: FormBuilder, private _asignacionSandbox: AsignacionSandbox, private _changeDetectorRef: ChangeDetectorRef) {
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

  loadObservations() {
    this._asignacionSandbox.obtenerObservaciones(this.idDocumento).subscribe((results) => {
      this.observaciones = [...results.observaciones];
      this._changeDetectorRef.detectChanges();
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

  setData(data: any) {
    this.idDocumento = data.idDocumento;
    this.idFuncionario = data.idFuncionario;
  }

  addObservation() {
    let observacion: ObservacionDTO = {
      observacion: this.form.get('observacion').value,
      ideFunci: this.idFuncionario,
      ideDocumento: this.idDocumento,
      codOrgaAdmin: null,
      nomFuncionario: null,
      valApellido1: null,
      valApellido2: null,
      corrElectronico: null,
      loginName: null,
      id: null,
      fecha: null,
      estado: null,
    };
    this._asignacionSandbox.registrarObservacion(observacion).subscribe(() => {
      this.form.get('observacion').setValue(null);
      this.loadObservations();
    });
  }
}
