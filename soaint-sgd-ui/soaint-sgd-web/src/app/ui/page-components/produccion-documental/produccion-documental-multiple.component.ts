import {ChangeDetectorRef, Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {ProduccionDocumentalApiService} from 'app/infrastructure/api/produccion-documental.api';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {
  getArrayData as getFuncionarioArrayData
} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {FuncionarioDTO} from 'app/domain/funcionarioDTO';
import {Sandbox} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {ProyeccionDocumentoDTO} from 'app/domain/ProyeccionDocumentoDTO';
import {TaskForm} from 'app/shared/interfaces/task-form.interface';
import {TareaDTO} from 'app/domain/tareaDTO';
import {TaskTypes} from 'app/shared/type-cheking-clasess/class-types';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {ActivatedRoute, Params} from '@angular/router';

@Component({
    selector: 'produccion-documental-multiple',
    templateUrl: './produccion-documental-multiple.component.html',
    styleUrls: ['produccion-documental.component.css'],
})

export class ProduccionDocumentalMultipleComponent implements OnInit, OnDestroy, TaskForm {
    task: TareaDTO;
    type = TaskTypes.TASK_FORM;

    form: FormGroup;
    validations: any = {};

    radicadoAsociado = null;

    dependenciaSelected: ConstanteDTO;

    listaProyectores: ProyeccionDocumentoDTO[] = [];
    sedesAdministrativas$: Observable<ConstanteDTO[]>;
    dependencias$: Observable<ConstanteDTO[]>;
    funcionarios$: Observable<FuncionarioDTO[]>;

    tiposPlantilla: ConstanteDTO[];

    constructor(private _store: Store<RootState>,
                private activatedRoute: ActivatedRoute,
                private _produccionDocumentalApi: ProduccionDocumentalApiService,
                private _changeDetectorRef: ChangeDetectorRef,
                private formBuilder: FormBuilder) {  }



    agregarProyector() {
        if (!this.form.valid) {
            return false;
        }

        const proyectores = this.listaProyectores;
        const proyector: ProyeccionDocumentoDTO = {
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
        this.refreshView();

        return true;
    }

    eliminarProyector(index) {
        if (index > -1) {
          const proyectores = this.listaProyectores;
          proyectores.splice( index, 1 );

          this.listaProyectores = [...proyectores];
        }
    }

    checkProyeccion(newProyector: ProyeccionDocumentoDTO) {
        let exists = false;
        this.listaProyectores.forEach((current: ProyeccionDocumentoDTO, index) => {
            if (current.sede.id === newProyector.sede.id &&
                current.dependencia.id === newProyector.dependencia.id &&
                current.funcionario.id === newProyector.funcionario.id &&
                current.tipoPlantilla.id === newProyector.tipoPlantilla.id) {
                exists = true;
            }
        });

        return exists;
    }

    dependenciaChange(event) {
        this.dependenciaSelected = event.value;
        this.funcionarios$ = this._produccionDocumentalApi.getFuncionariosPorDependenciaRol(this.dependenciaSelected.codigo, {} );
    }

    initForm() {
        this.form = this.formBuilder.group({
          // Datos generales
            'sede': [{value: null}, Validators.required],
            'dependencia': [{value: null}, Validators.required],
            'funcionario': [{value: null}, Validators.required],
            'tipoPlantilla': [{value: null}, Validators.required],
        });
    }

    ngOnInit(): void {
        this.activatedRoute.params.subscribe((params: Params) => {
            this.radicadoAsociado = params.hasOwnProperty('idTarea') ? {numero: 'RAD123456789'} : null;
            if (params.hasOwnProperty('variables')) {
                const obj = params['variables'];
                console.log('Obj: ' + typeof obj);
            }
        });

        this.sedesAdministrativas$ = this._produccionDocumentalApi.getSedes({});
        this.dependencias$ = this._produccionDocumentalApi.getDependencias({});
        this.tiposPlantilla = this._produccionDocumentalApi.getTiposPlantilla({});
        this._store.select(getActiveTask).take(1).subscribe(activeTask => {
            this.task = activeTask;
        });
        this.initForm();
        this.form.reset();

        this.listenForErrors();
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

    refreshView() {
        this._changeDetectorRef.detectChanges();
    }

    ngOnDestroy(): void {
    }

    save(): Observable<any> {
        console.log(this.listaProyectores);
        return undefined;
    }
}
