import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import 'rxjs/operator/filter';
import 'rxjs/add/operator/zip';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {LoadAction as DependenciaGrupoLoadAction} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-actions';
import {DESTINATARIO_PRINCIPAL} from '../../../shared/bussiness-properties/radicacion-properties';
import {ConfirmationService} from 'primeng/components/common/api';


@Component({
  selector: 'app-datos-destinatario',
  templateUrl: './datos-destinatario.component.html'
})
export class DatosDestinatarioComponent implements OnInit {

  form: FormGroup;


  selectedagenteDestinatario: any;
  canInsert = false;
  agentesDestinatario: Array<{ tipoDestinatario: ConstanteDTO, sedeAdministrativa: ConstanteDTO, dependenciaGrupo: ConstanteDTO }> = [];

  @Input() sedeAdministrativaInput: ConstanteDTO[] = [];
  @Input() tipoDestinatarioInput: ConstanteDTO[] = [];
  @Input() dependenciaGrupoInput: ConstanteDTO[] = [];

  @Input() editable = true;

  constructor(private _store: Store<State>,
              private confirmationService: ConfirmationService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {

    this.initForm();

    const grupoControl = this.form.get('dependenciaGrupo');
    grupoControl.disable();
    this.form.get('sedeAdministrativa').valueChanges.subscribe(value => {
      if (this.editable && value) {
        grupoControl.enable();
        this.form.get('dependenciaGrupo').reset();
        this._store.dispatch(new DependenciaGrupoLoadAction({codigo: value.id}));
      } else {
        grupoControl.disable();
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

    if (!this.editable) {
      this.form.disable();
    }
  }

  initForm() {
    this.form = this.formBuilder.group({
      'tipoDestinatario': [{value: null, disabled: !this.editable}],
      'sedeAdministrativa': [{value: null, disabled: !this.editable}],
      'dependenciaGrupo': [{value: null, disabled: !this.editable}],
      'destinatarioPrincipal': [{value: null, disabled: !this.editable}, Validators.required]
    });
  }

  addAgentesDestinatario() {
    const tipo = this.form.get('tipoDestinatario');

    if (tipo.value.codigo === DESTINATARIO_PRINCIPAL && this.agentesDestinatario.filter(value => value.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL).length > 0) {
      return this.confirmSubstitucionDestinatarioPrincipal();
    }

    const sede = this.form.get('sedeAdministrativa');
    const grupo = this.form.get('dependenciaGrupo');

    const insertVal = [
      {
        tipoDestinatario: tipo.value,
        sedeAdministrativa: sede.value,
        dependenciaGrupo: grupo.value
      }
    ];

    if (tipo.value.codigo === DESTINATARIO_PRINCIPAL) {
      this.form.get('destinatarioPrincipal').setValue(true);
    }

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

  substitudeAgenteDestinatario() {

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
        value => value.tipoDestinatario.codigo !== DESTINATARIO_PRINCIPAL
      )
    ];

    tipo.reset();
    sede.reset();
    grupo.reset();
  }

  deleteAgentesDestinatario(index) {

    if (this.agentesDestinatario[index].tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL) {
      this.form.get('destinatarioPrincipal').setValue(null);
    }

    const agente = [...this.agentesDestinatario];
    agente.splice(index, 1);
    this.agentesDestinatario = agente;
  }

  confirmSubstitucionDestinatarioPrincipal() {
    this.confirmationService.confirm({
      message: '<p style="text-align: center"> Está seguro desea substituir el destinatario principal?</p>',
      accept: () => {
        this.substitudeAgenteDestinatario();
      }
    });
  }

  deleteDestinatarioPrincipal() {
    this.agentesDestinatario = [...this.agentesDestinatario.filter(value => value.tipoDestinatario.codigo !== DESTINATARIO_PRINCIPAL)];
    this.form.get('destinatarioPrincipal').setValue(null);
  }

  getFormValues() {
    const values = this.form.value;
    if (values.destinatarioPrincipal === null &&
      values.tipoDestinatario !== null &&
      values.sedeAdministrativa !== null &&
      values.dependenciaGrupo !== null
    ) {
      this.addAgentesDestinatario();
    }
  }

}
