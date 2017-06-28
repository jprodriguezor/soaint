import {Component, Input, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import 'rxjs/operator/filter';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {
  getSedeAdministrativaArrayData,
  getTipoDestinatarioArrayData
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {getArrayData as dependenciaGrupoArrayData} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';

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

  agentesDestinatario: Array<{ tipoDestinatario: ConstanteDTO, sedeAdministrativa: ConstanteDTO, dependenciaGrupo: ConstanteDTO }> = [];


  @Input()
  editable: boolean = true;

  @Input()
  datosGenerales: any;

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

  addAgentesDestinatario() {
    let agenteDestinatario = [...this.agentesDestinatario];
    agenteDestinatario.push({
      tipoDestinatario: this.tipoDestinatarioControl.value,
      sedeAdministrativa: this.sedeAdministrativaControl.value,
      dependenciaGrupo: this.dependenciaGrupoControl.value
    });
    this.agentesDestinatario = agenteDestinatario;
    this.tipoDestinatarioControl.setValue(null);
    this.sedeAdministrativaControl.setValue(null);
    this.dependenciaGrupoControl.setValue(null);

  }

  deleteAgentesDestinatario(index) {
    let agente = [...this.agentesDestinatario];
    agente.splice(index, 1);
    this.agentesDestinatario = agente;
  }

  onSelectTipoComunicacion() {
    if (this.datosGenerales.tipoComunicacionControl.value && this.datosGenerales.tipoComunicacionControl.value.codigo != 'EI') {
      this.sedeAdministrativaControl.disable();
      this.dependenciaGrupoControl.disable();
    } else {
      this.sedeAdministrativaControl.enable();
      this.dependenciaGrupoControl.enable();
    }
  }

  initForm() {
    this.tipoDestinatarioControl = new FormControl({
      codigo: "PRINCIPA",
      nombre: "Principal"
    });
    this.sedeAdministrativaControl = new FormControl({value: null, disabled: true});
    this.dependenciaGrupoControl = new FormControl({value: null, disabled: true});
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
