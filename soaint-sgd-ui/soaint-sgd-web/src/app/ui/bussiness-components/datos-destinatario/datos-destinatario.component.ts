import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/operator/filter';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {
  getDependenciaGrupoArrayData,
  getSedeAdministrativaArrayData,
  getTipoDestinatarioArrayData
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-datos-destinatario',
  templateUrl: './datos-destinatario.component.html'
})
export class DatosDestinatarioComponent implements OnInit {

  form: FormGroup;
  tipoDestinatarioControl: AbstractControl;
  sedeAdministrativaControl: AbstractControl;
  dependenciaGrupoControl: AbstractControl;


  tipoDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;


  constructor(private _store: Store<State>, private _sandbox: Sandbox, private formBuilder: FormBuilder) {
    this.initForm();
  }

  ngOnInit(): void {
    this.tipoDestinatarioSuggestions$ = this._store.select(getTipoDestinatarioArrayData);
    this.sedeAdministrativaSuggestions$ = this._store.select(getSedeAdministrativaArrayData);
    this.dependenciaGrupoSuggestions$ = this._store.select(getDependenciaGrupoArrayData);
  }

  initForm() {
    this.tipoDestinatarioControl = new FormControl(null, Validators.required);
    this.sedeAdministrativaControl = new FormControl(null, Validators.required);
    this.dependenciaGrupoControl = new FormControl(null, Validators.required);
    this.form = this.formBuilder.group({
      'tipoDestinatario': this.tipoDestinatarioControl,
      'sedeAdministrativa': this.sedeAdministrativaControl,
      'dependenciaGrupo': this.dependenciaGrupoControl,
    });
  }

  onFilterTipoDestinatario($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoDestinatario', query);
  }

  onDropdownClickTipoDestinatario($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoDestinatario');
  }

  onFilterSedeAdministrativa($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('sedeAdministrativa', query);
  }

  onDropdownClickSedeAdministrativa($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('sedeAdministrativa');
  }

  onFilterDependenciaGrupo($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('dependenciaGrupo', query);
  }

  onDropdownClickDependenciaGrupo($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('dependenciaGrupo');
  }


}
