import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {getArrayData as dependenciaGrupoArrayData} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {OrganigramaDTO} from '../../../domain/organigramaDTO';

@Component({
  selector: 'app-popup-justificacion',
  templateUrl: './popup-justificacion.component.html',
})
export class PopupJustificacionComponent implements OnInit {

  form: FormGroup;

  validations: any = {};

  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;

  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;

  @Output()
  onChangeSedeAdministrativa: EventEmitter<any> = new EventEmitter();

  @Output()
  onRedirectComunication: EventEmitter<{ justificacion: string, sedeAdministrativa: OrganigramaDTO, dependenciaGrupo: OrganigramaDTO }> = new EventEmitter();

  constructor(private _store: Store<State>,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private formBuilder: FormBuilder) {
    this.initForm();
    this.listenForChanges();
    this.listenForErrors();
  }


  ngOnInit(): void {
    this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData);
    this.dependenciaGrupoSuggestions$ = this._store.select(dependenciaGrupoArrayData);
    this._store.dispatch(new SedeAdministrativaLoadAction());
  }

  initForm() {
    this.form = this.formBuilder.group({
      'justificacion': [{value: null, disabled: false}, Validators.required],
      'sedeAdministrativa': [{value: null, disabled: false}, Validators.required],
      'dependenciaGrupo': [{value: null, disabled: false}, Validators.required],
    });
  }

  listenForChanges() {
    this.form.get('sedeAdministrativa').valueChanges.subscribe((value) => {
      if (value) {
        this.onChangeSedeAdministrativa.emit(value);
        this.form.get('dependenciaGrupo').reset();
        this._dependenciaGrupoSandbox.loadDispatch({codigo: value.id});
      }
    });
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('sedeAdministrativa');
    this.bindToValidationErrorsOf('dependenciaGrupo');
    this.bindToValidationErrorsOf('justificacion');
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

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);
    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    }
  }

  redirectComunications() {
    this.onRedirectComunication.emit({
      justificacion: this.form.get('justificacion').value,
      sedeAdministrativa: this.form.get('sedeAdministrativa').value,
      dependenciaGrupo: this.form.get('dependenciaGrupo').value,
    });
  }
}
