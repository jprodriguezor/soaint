import { Component, OnInit, Input, OnChanges, Output, EventEmitter} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import { StateUnidadDocumentalService } from 'app/infrastructure/service-state-management/state.unidad.documental';
import { UnidadDocumentalDTO } from '../../../domain/unidadDocumentalDTO';


@Component({
  selector: 'app-popup-unidad-documental-notas',
  templateUrl: './popup-unidad-documental-notas.component.html',
})
export class PopupUnidadDocumentalNotasComponent implements OnInit {

  form: FormGroup;  
  validations: any = {};

  @Input() state: StateUnidadDocumentalService;
  private _index: number;
  unidadSeleccionada: UnidadDocumentalDTO = {};
  @Output() completado: EventEmitter<boolean> = new EventEmitter();

  constructor(
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    this.form = this.fb.group({
      serie: null,
      identificador: null,
      observaciones: [null, [Validators.required]],
    });

  }

  @Input() set index(value: number) {    
       this._index = value;
       this.unidadSeleccionada = this.state.ListadoUnidadDocumental[this._index];
       this.ActualizarFormulario();    
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

  ActualizarFormulario() {
    if(this.form) {
      this.form.reset();
      this.form.controls['serie'].setValue(this.unidadSeleccionada.nombreSubSerie);
      this.form.controls['identificador'].setValue(this.unidadSeleccionada.id);
      this.form.controls['observaciones'].setValue(this.unidadSeleccionada.observacion);
    }
  }

  Guardar() {
    this.unidadSeleccionada.observacion = this.form.controls['observaciones'].value;
    this.state.GuardarObservacion(this.unidadSeleccionada, this.index);
    this.completado.emit(true);
  }



}
