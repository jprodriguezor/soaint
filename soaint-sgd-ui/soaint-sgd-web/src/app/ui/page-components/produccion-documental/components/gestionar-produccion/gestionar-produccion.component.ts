import {ChangeDetectorRef, Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../../../../domain/constanteDTO';
import {ProduccionDocumentalApiService} from 'app/infrastructure/api/produccion-documental.api';
import {RolDTO} from '../../../../../domain/rolesDTO';
import {VALIDATION_MESSAGES} from '../../../../../shared/validation-messages';
import {Subscription} from 'rxjs/Subscription';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Sandbox as FuncionarioSandbox} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {FuncionarioDTO} from '../../../../../domain/funcionarioDTO';
import {StatusDTO} from '../../models/StatusDTO';
import {ProyectorDTO} from '../../../../../domain/ProyectorDTO';
import {Subscriber} from 'rxjs/Subscriber';
import {getActiveTask} from '../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Store} from '@ngrx/store';
import {State as RootState} from '../../../../../infrastructure/redux-store/redux-reducers';

@Component({
  selector: 'pd-gestionar-produccion',
  templateUrl: './gestionar-produccion.component.html'
})

export class PDGestionarProduccionComponent implements OnInit, OnDestroy {

  form: FormGroup;
  validations: any = {};

  dependenciaSelected: ConstanteDTO;

  listaProyectores: ProyectorDTO[] = [];
  startIndex = 0;

  sedesAdministrativas$: Observable<ConstanteDTO[]>;
  dependencias: Array<any> = [];
  roles$: Observable<RolDTO[]>;
  funcionarios$: Observable<FuncionarioDTO[]>;

  subscribers: Array<Subscription> = [];


  constructor(private _store: Store<RootState>,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private _changeDetectorRef: ChangeDetectorRef,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private _funcionarioSandBox: FuncionarioSandbox,
              private formBuilder: FormBuilder) {
    this.initForm();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'sede': [{value: null}, Validators.required],
      'dependencia': [{value: null}, Validators.required],
      'rol': [{value: null}, Validators.required],
      'funcionario': [{value: null}, Validators.required]
    });
  }

  ngOnInit(): void {
      this.sedesAdministrativas$ = this._produccionDocumentalApi.getSedes({});
      this.roles$ = this._produccionDocumentalApi.getRoles({});
      this.listenForChanges();
  }

  updateStatus(currentStatus: StatusDTO) {
    this.listaProyectores = [...currentStatus.gestionarProduccion.listaProyectores];
    this.startIndex = this.listaProyectores.length;
  }

  dependenciaChange(event) {
      this.dependenciaSelected = event.value;
      this.funcionarios$ = this._funcionarioSandBox.loadAllFuncionariosByRol({codDependencia: this.form.get('dependencia').value.codigo, rol: 'Proyector'}).map(res => res.funcionarios);
  }

  eliminarProyector(index) {
    // if (index >= this.startIndex) {
      const proyectores = this.listaProyectores;
      proyectores.splice(index, 1);
      this.listaProyectores = [...proyectores];
    // }
  }

  agregarProyector() {
    if (!this.form.valid) {
      return false;
    }

    const proyectores = this.listaProyectores;
    const proyector = {
      sede: this.form.get('sede').value,
      dependencia: this.form.get('dependencia').value,
      funcionario: this.form.get('funcionario').value,
      rol: this.form.get('rol').value
    };

    if (this.checkProyector(proyector)) {
      console.log('Ya existe el documento');
      return false;
    }

    proyectores.push(proyector);
    this.listaProyectores = [...proyectores];
    this.form.reset();
    this.funcionarios$ = Observable.of([]);
    this.refreshView();

    return true;
  }

  checkProyector(newProyector: ProyectorDTO) {
    let exists = false;
    this.listaProyectores.forEach((current: ProyectorDTO) => {
      if (current.sede.id === newProyector.sede.id &&
        current.dependencia.id === newProyector.dependencia.id &&
        current.funcionario.id === newProyector.funcionario.id &&
        current.rol.id === newProyector.rol.id) {
        exists = true;
      }
    });

    return exists;
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
    this.bindToValidationErrorsOf('rol');
    this.bindToValidationErrorsOf('funcionario');
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

  ngOnDestroy() {
    this.subscribers.forEach((sub: Subscription) => sub.unsubscribe());
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }
}

