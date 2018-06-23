import { Component, OnInit, Input, OnChanges, Output, EventEmitter} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import { StateUnidadDocumentalService } from 'app/infrastructure/service-state-management/state.unidad.documental';
import { UnidadDocumentalDTO } from '../../../domain/unidadDocumentalDTO';
import { CorrespondenciaDTO } from '../../../domain/correspondenciaDTO';
import { ComunicacionDataModel } from '../../page-components/distribucion-fisica-salida/models/comunicacion-data.model';

@Component({
  selector: 'app-informacion-envio',
  templateUrl: './informacion-envio.component.html',
  styleUrls: ['./informacion-envio.component.css']
})
export class InformacionEnvioComponent implements OnInit {

  form: FormGroup;  
  validations: any = {};
  
  private _comunicacionSeleccionada: ComunicacionDataModel = null;
  @Output() completado: EventEmitter<any> = new EventEmitter();
  @Output() cancelado: EventEmitter<any> = new EventEmitter();


  constructor(
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    this.form = this.fb.group({
      nro_guia: null,
      peso: [null, [Validators.required, Validators.pattern('^(([0-9]*)|(([0-9]*)\.([0-9]*)))$')]],
      valor: [null, [Validators.required]],
    });
  }

  OnBlurEvents(control: string) {
    this.SetValidationMessages(control);
  }

  SetValidationMessages(control: string) {
      const formControl = this.form.get(control);
      if (formControl.touched && formControl.invalid) {
        const error_keys = Object.keys(formControl.errors);
        const last_error_key = error_keys[error_keys.length - 1];
        this.validations[control] = VALIDATION_MESSAGES[last_error_key];
      } else {
          this.validations[control] = '';
      }
  }

@Input() set comunicacionSeleccionada(value: ComunicacionDataModel) {    
    this._comunicacionSeleccionada = value;
    this.ActualizarFormulario();    
 }

 ActualizarFormulario() {
  if(this.form) {
    this.form.reset();
    this.form.controls['nro_guia'].setValue(this._comunicacionSeleccionada.envio_nroguia);
    this.form.controls['peso'].setValue(this._comunicacionSeleccionada.envio_peso);
    this.form.controls['valor'].setValue(this._comunicacionSeleccionada.envio_valor);
  }
}

 Guardar() {
  this._comunicacionSeleccionada.envio_nroguia = this.form.controls['nro_guia'].value;
  this._comunicacionSeleccionada.envio_peso = this.form.controls['peso'].value;
  this._comunicacionSeleccionada.envio_valor = this.form.controls['valor'].value;
  this.completado.emit(this._comunicacionSeleccionada);
 }
 Cancelar() {
  this.cancelado.emit(null);
 }


}
