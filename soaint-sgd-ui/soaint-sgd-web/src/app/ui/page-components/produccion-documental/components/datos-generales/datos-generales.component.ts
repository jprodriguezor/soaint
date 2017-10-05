import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";
import {Observable} from "rxjs/Observable";
import {ConstanteDTO} from "../../../../../domain/constanteDTO";
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {
  getTipoAnexosArrayData,
  getTipoComunicacionArrayData,
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {VALIDATION_MESSAGES} from "../../../../../shared/validation-messages";

@Component({
  selector: 'pd-datos-generales',
  templateUrl: './datos-generales.component.html'
})

export class PDDatosGeneralesComponent implements OnInit{

  form: FormGroup;
  validations: any = {};

  tipoComunicacionSuggestions$: Observable<any[]>;
  tipoAnexosSuggestions$: Observable<ConstanteDTO[]>;

  constructor(private _store: Store<State>, private _constSandbox: ConstanteSandbox, private formBuilder: FormBuilder){}


  initForm() {
    this.form = this.formBuilder.group({
      //Datos generales
      'usuarioResponsable': [null],
      'fechaCreacion': [new Date()],
      'sedeAdministrativa': [{value: null}],
      'dependenciaGrupo': [{value: null}],

      //Radicado asociado
      'fechaRadicacion': [new Date()],
      'noRadicado': [null],

      //Producir documento
      'tipoComunicacion': [{value: null}],
      'tipoPlantilla': [{value:null}],
      'elaborarDocumento': [null],

      //Anexos
      'soporte':"electronico",
      'tipoAnexo': [{value: null}],
      'descripcion': [null],
    });
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('tipoComunicacion');
  }

  ngOnInit(): void {
    this.tipoComunicacionSuggestions$ = this._store.select(getTipoComunicacionArrayData);
    this.tipoAnexosSuggestions$ = this._store.select(getTipoAnexosArrayData);

    this._constSandbox.loadDatosGeneralesDispatch();

    this.initForm();

    this.listenForErrors();
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

