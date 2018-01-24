import {Component, EventEmitter, OnInit, Output} from '@angular/core';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {Store} from '@ngrx/store';
import {getCausalDevolucionArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/causal-devolucion-selectors';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';


@Component({
  selector: 'app-popup-reject',
  templateUrl: './popup-reject.component.html',
})
export class PopupRejectComponent implements OnInit {

  form: FormGroup;

  validations: any = {};

  causalesDevolicion$: Observable<ConstanteDTO[]>;

  @Output()
  onRejectComunication: EventEmitter<any> = new EventEmitter();


  constructor(private formBuilder: FormBuilder, private _asignacionSandbox: AsignacionSandbox,
              private _contantesSandbox: ConstanteSandbox, private _store: Store<State>) {
    this.initForm();
    this.listenForErrors();
  }


  ngOnInit(): void {
    this.causalesDevolicion$ = this._store.select(getCausalDevolucionArrayData);
    this._contantesSandbox.loadCausalDevolucionDispatch();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'observacion': [{value: null, disabled: false}, Validators.required],
      'causalDevolucion': [{value: null, disabled: false}, Validators.required]
    });
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('causalDevolucion');
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
    this._asignacionSandbox.setVisibleRejectDialogDispatch(false);
  }

  devolverComunicaciones() {
    this.onRejectComunication.emit(this.form.value);
  }

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);
    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    }
  }

}
