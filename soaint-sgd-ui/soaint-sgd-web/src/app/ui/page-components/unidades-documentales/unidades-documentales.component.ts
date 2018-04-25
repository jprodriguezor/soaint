import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { StateUnidadDocumentalService } from 'app/ui/page-components/unidades-documentales/state.unidad.documental';
import { FormBuilder } from '@angular/forms';
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

@Component({
  selector: 'app-unidades-documentales',
  templateUrl: './unidades-documentales.component.html',
  styleUrls: ['./unidades-documentales.component.css'],
})
export class UnidadesDocumentalesComponent implements TaskForm, OnInit {

  State: StateUnidadDocumentalService;

   // tarea
  task: TareaDTO;
  taskVariables: VariablesTareaDTO = {};

  constructor(
    private state: StateUnidadDocumentalService,
    private _store: Store<State>,
    private _taskSandBox: TaskSandBox,

  ) {
    this.State = this.state;
  }

  ngOnInit() {
    this.State.InitForm([
      'dependencia',
      'serie',
    ]);
    this.State.OpcionSeleccionada = 0 // abrir
    this.State.SetFormSubscriptions([
      'serie',
    ]);
    this.InitPropiedadesTarea();

  }

  InitPropiedadesTarea() {
    this._store.select(getActiveTask).subscribe((activeTask) => {
        this.task = activeTask;
        if (this.task.variables.codDependencia) {
            const codDependencia = this.task.variables.codDependencia
            this.state.formBuscar.controls['dependencia'].setValue(codDependencia);
            this.state.GetListadosSeries();
            this.state.Listar();
        }
    });
  }

  Finalizar() {
    this._taskSandBox.abortTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      instanciaProceso: this.task.idInstanciaProceso
    });
}

save(): Observable<any> {
  return  Observable.of(true).delay(5000);
}


}
