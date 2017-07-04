import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import 'rxjs/operator/filter';
import 'rxjs/add/operator/zip';
import 'rxjs/add/observable/forkJoin';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {
  getTipoDestinatarioArrayData
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {getArrayData as dependenciaGrupoArrayData} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {getDestinatarioPrincial} from 'app/infrastructure/state-management/constanteDTO-state/selectors/tipo-destinatario-selectors';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {LoadAction as DependenciaGrupoLoadAction} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-actions';

@Component({
  selector: 'app-datos-destinatario',
  templateUrl: './datos-destinatario.component.html'
})
export class DatosDestinatarioComponent implements OnInit {

  form: FormGroup;

  tipoDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;
  tipoDestinatarioSelected$: Observable<any>;

  selectedagenteDestinatario: any;
  canInsert = false;
  agentesDestinatario: Array<{ tipoDestinatario: ConstanteDTO, sedeAdministrativa: ConstanteDTO, dependenciaGrupo: ConstanteDTO }> = [];

  @Input()
  editable = true;

  constructor(private _store: Store<State>,
              private _constanteSandbox: ConstanteSandbox,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.tipoDestinatarioSuggestions$ = this._store.select(getTipoDestinatarioArrayData);
    this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData);
    this.dependenciaGrupoSuggestions$ = this._store.select(dependenciaGrupoArrayData);
    this.tipoDestinatarioSelected$ = this._store.select(getDestinatarioPrincial);

    this.initForm();

    this.form.get('sedeAdministrativa').valueChanges.subscribe(value => {
      if (this.editable && value) {
        this.form.get('dependenciaGrupo').reset();
        this._store.dispatch(new DependenciaGrupoLoadAction({codigo: value.id}));
      }
    });

    Observable.combineLatest(
      this.form.get('tipoDestinatario').valueChanges,
      this.form.get('sedeAdministrativa').valueChanges,
      this.form.get('dependenciaGrupo').valueChanges
    ).do(() => this.canInsert = false)
      .filter(([tipo, sede, grupo]) => tipo && sede && grupo)
      .zip(([tipo, sede, grupo]) => {
        return {tipo: tipo, sede: sede, grupo: grupo}
      })
      .subscribe(value => {
        this.canInsert = true
      });


  }

  initForm() {
    this.form = this.formBuilder.group({
      'tipoDestinatario': [{value: null, disabled: !this.editable}],
      'sedeAdministrativa': [{value: null, disabled: !this.editable}],
      'dependenciaGrupo': [{value: null, disabled: !this.editable}]
    });
  }

  addAgentesDestinatario() {
    const tipo = this.form.get('tipoDestinatario');
    const sede = this.form.get('sedeAdministrativa');
    const grupo = this.form.get('dependenciaGrupo');

    const insertVal = [
      {
        tipoDestinatario: tipo.value,
        sedeAdministrativa: sede.value,
        dependenciaGrupo: grupo.value
      }
    ];

    this.agentesDestinatario = [
      ...insertVal,
      ...this.agentesDestinatario.filter(
        value => value.tipoDestinatario !== tipo.value || value.sedeAdministrativa !== sede.value || value.dependenciaGrupo !== grupo.value
      )
    ];

    tipo.reset();
    sede.reset();
    grupo.reset();

  }

  deleteAgentesDestinatario(index) {
    let agente = [...this.agentesDestinatario];
    agente.splice(index, 1);
    this.agentesDestinatario = agente;
  }

}
