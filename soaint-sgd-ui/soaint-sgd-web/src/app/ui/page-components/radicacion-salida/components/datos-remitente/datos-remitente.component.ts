import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../../../../domain/constanteDTO';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {getArrayData as DependenciaGrupoSelector} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {getArrayData as getFuncionarioArrayData} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {Sandbox as FuncionariosSandbox} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {FuncionarioDTO} from '../../../../../domain/funcionarioDTO';

@Component({
  selector: 'rs-datos-remitente',
  templateUrl: './datos-remitente.component.html',
  styleUrls: ['./datos-remitente.component.css']
})
export class DatosRemitenteComponent implements OnInit {

  form: FormGroup;
  validations: any = {};
  visibility: any = {};
  display = false;
  editable: true;
  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;
  funcionariosSuggestions$: Observable<FuncionarioDTO[]>;

  constructor(private _store: Store<State>,
              private formBuilder: FormBuilder,
              private _funcionarioSandbox: FuncionariosSandbox,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox) {
  }

  ngOnInit() {

   this._funcionarioSandbox.loadAllFuncionariosDispatch();

    this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData).share();
    this.dependenciaGrupoSuggestions$ = this._store.select(DependenciaGrupoSelector).share().share();
    this.funcionariosSuggestions$ = this._store.select(getFuncionarioArrayData).share();

    this.initForm();
    this.listenForChanges();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'sedeAdministrativa': [{value: null, disabled: this.editable}, Validators.required],
      'dependenciaGrupo': [{value: null, disabled: this.editable}, Validators.required],
      'funcionarioGrupo': [{value: null, disabled: this.editable}, Validators.required]
    });
  }

  listenForChanges() {
    this.form.get('sedeAdministrativa').valueChanges.subscribe((value) => {
      if (value) {
        this.form.get('dependenciaGrupo').reset();
        this._dependenciaGrupoSandbox.loadDispatch({codigo: value.id});
      }
    });

    this.form.get('dependenciaGrupo').valueChanges.subscribe((value) => {
      if (value) {
        this.form.get('funcionarioGrupo').reset();
        this._funcionarioSandbox.loadAllFuncionariosDispatch({codDependencia: value.codigo});
      }
    });
  }


}
