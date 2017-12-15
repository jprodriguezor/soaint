import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {ProduccionDocumentalApiService} from 'app/infrastructure/api/produccion-documental.api';
import {Observable} from 'rxjs/Observable';
import {Subscription} from 'rxjs/Subscription';
import {PdMessageService} from '../../providers/PdMessageService';
import {TareaDTO} from '../../../../../domain/tareaDTO';
import {DESTINATARIO_PRINCIPAL} from '../../../../../shared/bussiness-properties/radicacion-properties';
import {VALIDATION_MESSAGES} from '../../../../../shared/validation-messages';
import {Sandbox as DependenciaGrupoSandbox} from '../../../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {LoadAction as SedeAdministrativaLoadAction} from '../../../../../infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {getArrayData as dependenciaGrupoArrayData} from '../../../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {getArrayData as sedeAdministrativaArrayData} from '../../../../../infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {State} from '../../../../../infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {FuncionarioDTO} from '../../../../../domain/funcionarioDTO';
import {Sandbox as FuncionariosSandbox} from '../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {getArrayData as getFuncionarioArrayData} from '../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {ConfirmationService} from 'primeng/components/common/api';


@Component({
  selector: 'pd-datos-contacto',
  templateUrl: 'datos-contacto.component.html'
})

export class PDDatosContactoComponent implements OnInit, OnDestroy {
  form: FormGroup;
  tipoComunicacionSelected: ConstanteDTO;
  tipoPersonaSelected: ConstanteDTO;
  subscription: Subscription;

  validations: any = {};

  @Input()
  taskData: TareaDTO;

  tipoPersona: any;

  sedesAdministrativas$: Observable<ConstanteDTO[]>;
  dependencias$: Observable<ConstanteDTO[]>;
  tiposPersona$: Observable<ConstanteDTO[]>;
  tiposDestinatario$: Observable<ConstanteDTO[]>;
  actuanEnCalidad$: Observable<ConstanteDTO[]>;
  tiposDocumento$: Observable<ConstanteDTO[]>;
  funcionarios$: Observable<FuncionarioDTO[]>;

  destinatarios: any[] = [];

  @Output()
  onChangeSedeAdministrativa: EventEmitter<any> = new EventEmitter();

  canInsert = false;

  responseToRem = false;

  constructor(private formBuilder: FormBuilder,
              private _store: Store<State>,
              private confirmationService: ConfirmationService,
              private _funcionarioSandbox: FuncionariosSandbox,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private pdMessageService: PdMessageService) {
    this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => {
      this.tipoComunicacionSelected = tipoComunicacion;
    });

    this.funcionarios$ = this._store.select(getFuncionarioArrayData);

    this.initForm();
    this.listenForChanges();
    this.listenForErrors();

    Observable.combineLatest(
      this.form.get('tipoDestinatario').valueChanges,
      this.form.get('sedeAdministrativa').valueChanges,
      this.form.get('dependencia').valueChanges,
      this.form.get('funcionario').valueChanges
    ).do(() => this.canInsert = false)
      .filter(([tipo, sede, grupo, func]) => tipo && sede && grupo && func)
      .zip(([tipo, sede, grupo, func]) => {
        return {tipo: tipo, sede: sede, grupo: grupo, funcionario: func}
      })
      .subscribe(value => {
        this.canInsert = true
      });
  }


  tipoPersonaChange(event) {
    this.tipoPersonaSelected = event.value;
  }

  ngOnInit(): void {
    this.sedesAdministrativas$ = this._store.select(sedeAdministrativaArrayData);
    this.dependencias$ = this._store.select(dependenciaGrupoArrayData);
    this._store.dispatch(new SedeAdministrativaLoadAction());

    this._funcionarioSandbox.loadAllFuncionariosByRolDispatch({
      rol: 'RECEPTOR'
    });

    this.tiposPersona$ = this._produccionDocumentalApi.getTiposPersona({});
    this.tiposDestinatario$ = this._produccionDocumentalApi.getTiposDestinatario({});
    this.actuanEnCalidad$ = this._produccionDocumentalApi.getActuaEnCalidad({});
    this.tiposDocumento$ = this._produccionDocumentalApi.getTiposDocumento({});

    this.tiposPersona$.subscribe((results) => {
      if (results.length > 0) {
        this.tipoPersonaSelected = results[0];
        this.form.get('tipoPersona').setValue(results[0]);
      }
    });

    this.tiposDestinatario$.subscribe((results) => {
      if (results.length > 0) {
        this.form.get('tipoDestinatario').setValue(results.find((tipoDestinatario) => {
          return tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL;
        }));
      }
    });
  }

  initForm() {
    this.form = this.formBuilder.group({
      // Datos destinatario
      'tipoDestinatario': [null, Validators.required],
      'tipoDocumentoText': [null],
      'tipoDocumentoList': [{value: null}],
      'tipoPersona': [{value: null}],
      'nombreApellidos': [null],
      'nit': [null],
      'razonSocial': [null],
      'actuaCalidad': [{value: null}],
      'sedeAdministrativa': [null, Validators.required],
      'dependencia': [null, Validators.required],
      'funcionario': [null, Validators.required],
      'responderRemitente': [null],
      'electronica': [null],
      'fisica': [null],
      'destinatarioPrincipal': [{value: null, disabled: false}, Validators.required],
    });
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('sedeAdministrativa');
    this.bindToValidationErrorsOf('dependencia');
    this.bindToValidationErrorsOf('tipoDestinatario');
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

  listenForChanges() {
    this.form.get('sedeAdministrativa').valueChanges.subscribe((value) => {
      if (value) {
        this.onChangeSedeAdministrativa.emit(value);
        this.form.get('dependencia').reset();
        this._dependenciaGrupoSandbox.loadDispatch({codigo: value.id});
      }
    });

    this.form.get('dependencia').valueChanges.subscribe((value) => {
      if (value) {
        this.onChangeSedeAdministrativa.emit(value);
        this.form.get('funcionario').reset();
        this._funcionarioSandbox.loadAllFuncionariosDispatch({
          codDependencia: value.codigo
        });
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

  ngOnDestroy() {
    // unsubscribe to ensure no memory leaks
    this.subscription.unsubscribe();
  }

  confirmSubstitucionDestinatarioPrincipal() {
    this.confirmationService.confirm({
      message: '<p style="text-align: center"> Está seguro desea substituir el destinatario principal?</p>',
      accept: () => {
        this.substitudeAgenteDestinatario();
      }
    });
  }

  substitudeAgenteDestinatario() {

    const tipo = this.form.get('tipoDestinatario');
    const sede = this.form.get('sedeAdministrativa');
    const grupo = this.form.get('dependencia');
    const func = this.form.get('funcionario');

    const insertVal = [
      {
        tipoDestinatario: tipo.value,
        sedeAdministrativa: sede.value,
        dependenciaGrupo: grupo.value,
        funcionario: func.value
      }
    ];

    this.destinatarios = [
      ...insertVal,
      ...this.destinatarios.filter(
        value => value.tipoDestinatario.codigo !== DESTINATARIO_PRINCIPAL
      )
    ];

    tipo.reset();
    sede.reset();
    grupo.reset();
    func.reset();
  }

  adicionarDestinatario() {
    const tipo = this.form.get('tipoDestinatario');

    if (tipo.value.codigo === DESTINATARIO_PRINCIPAL && this.destinatarios.filter(value => value.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL).length > 0) {
      return this.confirmSubstitucionDestinatarioPrincipal();
    }

    const sede = this.form.get('sedeAdministrativa');
    const grupo = this.form.get('dependencia');
    const func = this.form.get('funcionario');

    const insertVal = [
      {
        tipoDestinatario: tipo.value,
        sedeAdministrativa: sede.value,
        dependenciaGrupo: grupo.value,
        funcionario: func.value
      }
    ];

    if (tipo.value.codigo === DESTINATARIO_PRINCIPAL) {
      this.form.get('destinatarioPrincipal').setValue({
        tipoDestinatario: tipo.value,
        sedeAdministrativa: sede.value,
        dependenciaGrupo: grupo.value,
        funcionario: func.value
      });
    }

    this.destinatarios = [
      ...insertVal,
      ...this.destinatarios.filter(
        value => value.sedeAdministrativa !== sede.value || value.dependenciaGrupo !== grupo.value
      )
    ];

    tipo.reset();
    sede.reset();
    grupo.reset();
    func.reset();
  }
}

