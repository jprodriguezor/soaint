import {ChangeDetectorRef, Component, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {ProduccionDocumentalApiService} from 'app/infrastructure/api/produccion-documental.api';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {FuncionarioDTO} from 'app/domain/funcionarioDTO';
import {ProyectorDTO} from 'app/domain/ProyectorDTO';
import {TareaDTO} from 'app/domain/tareaDTO';
import {getActiveTask} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {Subscription} from 'rxjs/Subscription';
import {createSelector} from 'reselect';
import {EntradaProcesoDTO} from '../../../domain/EntradaProcesoDTO';
import {PROCESS_DATA} from './providers/ProcessData';
import {Sandbox as DependenciaGrupoSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Sandbox as FuncionarioSandbox} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';

@Component({
  selector: 'produccion-documental-multiple',
  templateUrl: './produccion-documental-multiple.component.html',
  styleUrls: ['produccion-documental.component.css'],
  encapsulation: ViewEncapsulation.None,
})

export class ProduccionDocumentalMultipleComponent implements OnInit {

  task: TareaDTO;

  form: FormGroup;
  validations: any = {};

  numeroRadicado = '';

  listaProyectores: ProyectorDTO[] = [];
  sedesAdministrativas$: Observable<ConstanteDTO[]>;
  dependencias: Array<any> = [];
  funcionarios$: Observable<FuncionarioDTO[]>;
  tiposPlantilla$: Observable<ConstanteDTO[]>;

  subscribers: Array<Subscription> = [];

  authPayload: { usuario: string, pass: string } | {};
  authPayloadUnsubscriber: Subscription;

  constructor(private _store: Store<RootState>,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private _funcionarioSandBox: FuncionarioSandbox,
              private _taskSandBox: TaskSandBox,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private formBuilder: FormBuilder) {

    this.authPayloadUnsubscriber = this._store.select(createSelector((s: RootState) => s.auth.profile, (profile) => {
      return profile ? {usuario: profile.username, pass: profile.password} : {};
    })).subscribe((value) => {
      this.authPayload = value;
    });
  }

  proyectar() {
    const entradaProceso: any = {
      idDespliegue: this.task.idDespliegue,
      idProceso: this.task.idProceso,
      idTarea: Number.parseInt(this.task.idTarea),
      instanciaProceso: Number.parseInt(this.task.idInstanciaProceso),
      estados: ['LISTO'],
      parametros: {
        numeroRadicado: this.numeroRadicado,
        proyectores: this.listaProyectores,
        idDespliegue: PROCESS_DATA.produccion_documental.idDespliegue,
        idProceso: PROCESS_DATA.produccion_documental.idProceso
      }
    };

    const payload: EntradaProcesoDTO = Object.assign(entradaProceso, this.authPayload);

    this._produccionDocumentalApi.ejecutarProyeccionMultiple(payload).subscribe(() => {
      this.form.disable();

      this._taskSandBox.completeTaskDispatch({
        idProceso: this.task.idProceso,
        idDespliegue: this.task.idDespliegue,
        idTarea: this.task.idTarea,
        parametros: {}
      });
    });
  }

  agregarProyector() {
    if (!this.form.valid) {
      return false;
    }

    const proyectores = this.listaProyectores;
    const proyector: ProyectorDTO = {
      sede: this.form.get('sede').value,
      dependencia: this.form.get('dependencia').value,
      funcionario: this.form.get('funcionario').value,
      tipoPlantilla: this.form.get('tipoPlantilla').value
    };

    if (this.checkProyeccion(proyector)) {
      console.log('Ya existe la proyección');
      return false;
    }

    proyectores.push(proyector);
    this.listaProyectores = [...proyectores];
    this.form.reset();
    this.funcionarios$ = Observable.of([]);

    return true;
  }

  eliminarProyector(index) {
    if (index > -1) {
      const proyectores = this.listaProyectores;
      proyectores.splice(index, 1);

      this.listaProyectores = [...proyectores];
    }
  }

  checkProyeccion(newProyector: ProyectorDTO) {
    let exists = false;
    this.listaProyectores.forEach((current: ProyectorDTO, index) => {
      if (current.sede.id === newProyector.sede.id &&
        current.dependencia.id === newProyector.dependencia.id &&
        current.funcionario.id === newProyector.funcionario.id &&
        current.tipoPlantilla.id === newProyector.tipoPlantilla.id) {
        exists = true;
      }
    });

    return exists;
  }

  dependenciaChange() {
    this.funcionarios$ = this._funcionarioSandBox.loadAllFuncionariosByRol({codDependencia: this.form.get('dependencia').value.codigo, rol: 'Proyector'}).map(res => {
      return res.funcionarios
    });
  }

  initForm() {
    this.form = this.formBuilder.group({
      'sede': [{value: null}, Validators.required],
      'dependencia': [{value: null}, Validators.required],
      'funcionario': [{value: null}, Validators.required],
      'tipoPlantilla': [{value: null}, Validators.required],
    });
  }

  ngOnInit(): void {
    this.sedesAdministrativas$ = this._produccionDocumentalApi.getSedes({});
    this.tiposPlantilla$ = this._produccionDocumentalApi.getTiposPlantilla({});
    this._store.select(getActiveTask).take(1).subscribe(activeTask => {
      this.task = activeTask;
      if (this.task && this.task.variables.numeroRadicado) {
        this.numeroRadicado = this.task.variables.numeroRadicado;
      }

    });
    this.initForm();
    this.listenForErrors();
    this.listenForChanges();
  }

  listenForChanges() {
    this.subscribers.push(this.form.get('sede').valueChanges.distinctUntilChanged().subscribe((sede) => {
      this.form.get('dependencia').reset();
      if (sede) {
        const depedenciaSubscription: Subscription = this._dependenciaGrupoSandbox.loadData({codigo: sede.id}).subscribe(dependencias => {
          this.dependencias = dependencias.organigrama;
          depedenciaSubscription.unsubscribe();
        });
      }
    }));
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('sede');
    this.bindToValidationErrorsOf('dependencia');
    this.bindToValidationErrorsOf('funcionario');
    this.bindToValidationErrorsOf('tipoPlantilla');
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

  save(): Observable<any> {
    return Observable.of(true).delay(5000);
  }
}
