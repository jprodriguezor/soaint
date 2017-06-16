import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import 'rxjs/operator/filter';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {
  getSedeAdministrativaArrayData,
  getTipoDestinatarioArrayData
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {
  getArrayData as dependenciaGrupoArrayData
} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors'
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {
  Sandbox as DependenciaGrupoSandbox
} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';

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

  selectSedeAdministrativa: any;


  constructor(private _store: Store<State>,
              private _constanteSandbox: ConstanteSandbox,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private formBuilder: FormBuilder) {
    this.initForm();
  }

  ngOnInit(): void {
    this.tipoDestinatarioSuggestions$ = this._store.select(getTipoDestinatarioArrayData);
    this.sedeAdministrativaSuggestions$ = this._store.select(getSedeAdministrativaArrayData);
    this.dependenciaGrupoSuggestions$ = this._store.select(dependenciaGrupoArrayData);
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
    this._constanteSandbox.filterDispatch('tipoDestinatario', query);
  }

  onDropdownClickTipoDestinatario($event) {
    // this method triggers load of suggestions
    this._constanteSandbox.loadDispatch('tipoDestinatario');
  }

  onSelectSedeAdministrativa(value) {
    this.selectSedeAdministrativa = value.codigo;
  }

  onFilterSedeAdministrativa($event) {
    const query = $event.query;
    this._constanteSandbox.filterDispatch('sedeAdministrativa', query);
  }

  onDropdownClickSedeAdministrativa($event) {
    // this method triggers load of suggestions
    this._constanteSandbox.loadDispatch('sedeAdministrativa');
  }

  onFilterDependenciaGrupo($event) {
    this._dependenciaGrupoSandbox.filterDispatch({query: $event.query, codigo: this.selectSedeAdministrativa});
  }

  onDropdownClickDependenciaGrupo($event) {
    // this method triggers load of suggestions
    if (this.selectSedeAdministrativa) {
      this._dependenciaGrupoSandbox.loadDispatch({codigo: this.selectSedeAdministrativa});
    }
  }


}
