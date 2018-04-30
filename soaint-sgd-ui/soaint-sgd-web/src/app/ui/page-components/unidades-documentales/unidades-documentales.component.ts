import { Component, OnInit, ChangeDetectorRef, IterableDiffers, DoCheck } from '@angular/core';
import { StateUnidadDocumentalService } from 'app/ui/page-components/unidades-documentales/state.unidad.documental';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { UnidadDocumentalApiService } from 'app/infrastructure/api/unidad-documental.api';
import { Store } from '@ngrx/store';
import { State } from 'app/infrastructure/redux-store/redux-reducers';
import { SerieSubserieApiService } from 'app/infrastructure/api/serie-subserie.api';
import { Sandbox } from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import { Sandbox as TaskSandBox } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import { ConfirmationService } from 'primeng/primeng';
import { TareaDTO } from '../../../domain/tareaDTO';
import { VariablesTareaDTO } from '../produccion-documental/models/StatusDTO';
import { getActiveTask } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import { TaskForm } from '../../../shared/interfaces/task-form.interface';
import { Observable } from 'rxjs/Observable';
import { ROUTES_PATH } from '../../../app.route-names';
import { go } from '@ngrx/router-store';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {Subscription} from 'rxjs/Subscription';
import { isNullOrUndefined } from 'util';
import { UnidadDocumentalAccion } from 'app/ui/page-components/unidades-documentales/models/enums/unidad.documental.accion.enum';
import { UnidadDocumentalDTO } from '../../../domain/unidadDocumentalDTO';


@Component({
  selector: 'app-unidades-documentales',
  templateUrl: './unidades-documentales.component.html',
  styleUrls: ['./unidades-documentales.component.css'],
})
export class UnidadesDocumentalesComponent implements TaskForm, OnInit, DoCheck {

  // contiene:
  // formulario, configuración y validación
  // operaciones sobre unidades documentales como: abrir, cerrar, reactivar, aprobar, rechazar
  State: StateUnidadDocumentalService;

   // tarea
  task: TareaDTO;
  taskVariables: VariablesTareaDTO = {};

  // form
  formBuscar: FormGroup;
  validations: any = {};
  subscribers: Array<Subscription> = [];
  codDependencia: string;
  OpcionSeleccionada: number;
  requiereSubserie: boolean;

  // control de cambios en array
  differ: any;

  constructor(
    private state: StateUnidadDocumentalService,
    private _store: Store<State>,
    private _taskSandBox: TaskSandBox,
    private _differs: IterableDiffers,
    private _detectChanges: ChangeDetectorRef,
    private fb: FormBuilder,

  ) {
    this.State = this.state;
    this.differ = _differs.find([]).create(null);
  }

  ngOnInit() {
    this.OpcionSeleccionada = 0 // abrir
    this.InitForm();
    this.SetFormSubscriptions();
    this.InitPropiedadesTarea();

  }

  InitForm() {
    this.formBuscar = this.fb.group({
     serie: [null, Validators.required],
     subserie: [null],
     identificador: [''],
     nombre: [''],
     descriptor1: [''],
     descriptor2: [''],
    });
 }

  OnBlurEvents(control: string) {
    this.SetValidationMessages(control);
  }

  SetValidationMessages(control: string) {
    const formControl = this.formBuscar.get(control);
    if (formControl.touched && formControl.invalid) {
      const error_keys = Object.keys(formControl.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    } else {
        this.validations[control] = '';
    }
  }

  SetFormSubscriptions() {
    const subserie = 'subserie';
    this.subscribers.push(
        this.formBuscar.get('serie').valueChanges.distinctUntilChanged().subscribe(value => {
          this.formBuscar.controls[subserie].reset();  
          this.State.GetSubSeries(value, this.codDependencia)
          .subscribe(result => {
            this.State.ListadoSubseries =  !isNullOrUndefined(result) ? result : [];
            if(this.State.ListadoSubseries.length) {
              setTimeout(() => {
                this.formBuscar.controls[subserie].setValidators([Validators.required]);
                this.formBuscar.controls[subserie].updateValueAndValidity();
              }, 0);
            } else {
              this.formBuscar.controls[subserie].clearValidators();
              this.formBuscar.controls[subserie].updateValueAndValidity();
              this.validations[subserie] = '';
            }            
           });         
        }));
    }

  ResetForm() {
    this.formBuscar.reset();
    this.State.ListadoSubseries = [];
    this.State.ListadoUnidadDocumental = [];
    this.validations = [];
    this._detectChanges.detectChanges();
  }

  HabilitarOpcion(opcion: number) {
    if (UnidadDocumentalAccion[this.OpcionSeleccionada] === UnidadDocumentalAccion[opcion]) {
        return true;
    }
    return false;
  }

  Listar() {
    this.state.Listar(this.GetPayload(this.OpcionSeleccionada));
  }

  GetPayload(opcionSeleccionada?: number): UnidadDocumentalDTO {    
    const payload: UnidadDocumentalDTO = {};    
    if (this.codDependencia) {
        payload.codigoDependencia = this.codDependencia;
    }
    if (!isNullOrUndefined(opcionSeleccionada)) {
        payload.accion = UnidadDocumentalAccion[opcionSeleccionada];
    }    
    if (this.formBuscar.controls['serie'].value) {
        payload.codigoSerie = this.formBuscar.controls['serie'].value;
    }
    if (this.formBuscar.controls['subserie'].value) {
       payload.codigoSubSerie = this.formBuscar.controls['subserie'].value;
    }
    if (this.formBuscar.controls['identificador'].value) {
       payload.codigoUnidadDocumental = this.formBuscar.controls['identificador'].value;
    }
    if (this.formBuscar.controls['nombre'].value) {
       payload.nombreUnidadDocumental = this.formBuscar.controls['nombre'].value;
    }
    if (this.formBuscar.controls['descriptor1'].value) {
       payload.descriptor1 = this.formBuscar.controls['descriptor1'].value;
    }
    if (this.formBuscar.controls['descriptor2'].value) {
       payload.descriptor1 = this.formBuscar.controls['descriptor2'].value;
    }    
    return payload;
  }

  ngDoCheck() {
    const change = this.differ.diff(this.State.ListadoUnidadDocumental);
    if (change) {
      this._detectChanges.detectChanges();
    }
  }

  InitPropiedadesTarea() {
    this._store.select(getActiveTask).subscribe((activeTask) => {
        this.task = activeTask;
        if (!this.task) {
          this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
        } else if (this.task.variables.codDependencia) {
            this.codDependencia = this.task.variables.codDependencia
            this.State.GetListadosSeries(this.codDependencia)
            .subscribe(resp => {
              this.State.ListadoSeries = resp;
            });          
            this.State.Listar(this.GetPayload(UnidadDocumentalAccion.Abrir));
        }
    });
  }

    Finalizar() {
      this._taskSandBox.completeBackTaskDispatch({
        idProceso: this.task.idProceso,
        idDespliegue: this.task.idDespliegue,
        idTarea: this.task.idTarea,
        parametros: {}
      });
    }

    save(): Observable<any> {
      return  Observable.of(true).delay(5000);
    }


}
