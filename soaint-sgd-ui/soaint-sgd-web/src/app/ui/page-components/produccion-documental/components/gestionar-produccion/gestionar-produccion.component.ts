import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
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
import {StatusDTO} from "../../models/StatusDTO";

@Component({
  selector: 'pd-gestionar-produccion',
  templateUrl: './gestionar-produccion.component.html'
})

export class PDGestionarProduccionComponent implements OnInit {

    form: FormGroup;
    validations: any = {};

  dependenciaSelected: ConstanteDTO;

  listaProyectores: Array<{ sede: ConstanteDTO, dependencia: ConstanteDTO, rol: RolDTO, funcionario: ConstanteDTO }> = [];

    sedesAdministrativas$: Observable<ConstanteDTO[]>;
    dependencias: Array<any> = [];
    roles$: Observable<RolDTO[]>;
    funcionarios$: Observable<FuncionarioDTO[]>;

    subscribers: Array<Subscription> = [];


  constructor(private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private _changeDetectorRef: ChangeDetectorRef,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private _funcionarioSandBox: FuncionarioSandbox,
              private formBuilder: FormBuilder) {
    this.initForm();
  }

  dependenciaChange(event) {
      this.dependenciaSelected = event.value;
      this.funcionarios$ = this._funcionarioSandBox.loadAllFuncionariosByRol({codDependencia: this.form.get('dependencia').value.codigo, rol: 'Proyector'}).map(res => {
          return res.funcionarios
      });
  }

  eliminarProyector(index) {
    if (index > -1) {
      const documentos = this.listaProyectores;
      documentos.splice(index, 1);

      this.listaProyectores = [...documentos];
      this.refreshView();
    }
  }

  agregarProyector() {
    if (!this.form.valid) {
      return false;
    }

    const documentos = this.listaProyectores;
    const documento = {
      sede: this.form.get('sede').value,
      dependencia: this.form.get('dependencia').value,
      funcionario: this.form.get('funcionario').value,
      rol: this.form.get('rol').value
    };

    if (this.checkProyector(documento)) {
      console.log('Ya existe el documento');
      return false;
    }

    documentos.push(documento);
    this.listaProyectores = [...documentos];
    this.form.reset();
    this.funcionarios$ = Observable.of([]);
    this.refreshView();

    return true;
  }

  checkProyector(newDocumento: { sede: ConstanteDTO, dependencia: ConstanteDTO, rol: RolDTO, funcionario: ConstanteDTO }) {
    let exists = false;
    this.listaProyectores.forEach((current: { sede: ConstanteDTO, dependencia: ConstanteDTO, rol: RolDTO, funcionario: ConstanteDTO }, index) => {
      if (current.sede.id === newDocumento.sede.id &&
        current.dependencia.id === newDocumento.dependencia.id &&
        current.funcionario.id === newDocumento.funcionario.id &&
        current.rol.id === newDocumento.rol.id) {
        exists = true;
      }
    });

    return exists;
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

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }
}

