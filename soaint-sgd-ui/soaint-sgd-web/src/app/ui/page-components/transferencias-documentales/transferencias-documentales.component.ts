import { Component, OnInit, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';
import { TaskForm } from '../../../shared/interfaces/task-form.interface';
import { TareaDTO } from '../../../domain/tareaDTO';
import { VariablesTareaDTO } from '../produccion-documental/models/StatusDTO';
import { StateUnidadDocumentalService } from 'app/infrastructure/service-state-management/state.unidad.documental';
import { Store } from '@ngrx/store';
import { State } from 'app/infrastructure/redux-store/redux-reducers';
import { Sandbox } from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import { Sandbox as TaskSandBox } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import { getActiveTask } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import { isNullOrUndefined } from 'util';
import { UnidadDocumentalAccion } from 'app/ui/page-components/unidades-documentales/models/enums/unidad.documental.accion.enum';
import { UnidadDocumentalDTO } from '../../../domain/unidadDocumentalDTO';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-transferencias-documentales',
  templateUrl: './transferencias-documentales.component.html',
  styleUrls: ['./transferencias-documentales.component.css']
})
export class TransferenciasDocumentalesComponent implements TaskForm, OnInit, OnDestroy {

  // contiene:
  // formulario, configuración y validación
  // operaciones sobre unidades documentales como: abrir, cerrar, reactivar, aprobar, rechazar
  State: StateUnidadDocumentalService;
  
  // tarea
  task: TareaDTO;
  taskVariables: VariablesTareaDTO = {};
  status: number = null;
  // form
  formTransferencia: FormGroup;
  validations: any = {};
  subscribers: Array<Subscription> = [];
  constructor(    
    private state: StateUnidadDocumentalService,
    private _store: Store<State>,
    private _taskSandBox: TaskSandBox,
    private _detectChanges: ChangeDetectorRef,
    private fb: FormBuilder,
    private route: ActivatedRoute,

  ) {
    this.State = this.state;
  }

  ngOnInit() {
    this.State.ListadoUnidadDocumental = [];
    this.InitForm();
    this.SetListadoSubscriptions(); // solucion para el problema de actualización del componente datatable de primeng
    this.InitPropiedadesTarea();
    
  }

  InitForm() {
    this.formTransferencia = this.fb.group({
     fondo: [null, Validators.required],
     subfondo: [null],
     seccion: [''],
     tipoTransferencia: [''],
     funcionarioResponsable: [''],
     fechaAprobacion: [''],
     subseccion: [''],
     nroTransferencia: [''],
     cargoFuncionario: [''],
     lugarVerificacion: [''],
    });
 }

 InitPropiedadesTarea() {
    this._store.select(getActiveTask).subscribe((activeTask) => {
        this.task = activeTask; 
        this.route.params.subscribe( params => {
          this.status = parseInt(params.status, 10) || null;
          this.State.Listar({
            codigoDependencia: this.task.variables.codDependencia,
          });
          if(this.status === 2) {
            this.formTransferencia.controls['lugarVerificacion'].setValidators([Validators.required]);
            this.formTransferencia.updateValueAndValidity();
          }
      });     
    });
  }

  SetValidationMessages(control: string) {
    const formControl = this.formTransferencia.get(control);
    if (formControl.touched && formControl.invalid) {
      const error_keys = Object.keys(formControl.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    } else {
        this.validations[control] = '';
    }
  }

    SetListadoSubscriptions() {
      this.subscribers.push(this.State.ListadoActualizado$.subscribe(()=>{        
        this._detectChanges.detectChanges();
      }));
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

  guardarEstadoTarea() {
    const payload: any = {
      
          };
          const tareaDto: any = {
            idTareaProceso: this.task.idTarea,
            idInstanciaProceso: this.task.idInstanciaProceso,
            payload: payload
          };
          this._taskSandBox.guardarEstadoTarea(tareaDto).subscribe(() => {});
  }

  ngOnDestroy() {
    this.subscribers.forEach(obs => {
      obs.unsubscribe();
    });
  }


}
