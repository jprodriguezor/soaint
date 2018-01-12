import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {Subscription} from 'rxjs/Subscription';
import {TareaDTO} from '../../../domain/tareaDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';

@Component({
  selector: 'app-gestionar-devoluciones',
  templateUrl: './gestionar-devoluciones.component.html',
  styleUrls: ['./gestionar-devoluciones.component.scss'],
})
export class GestionarDevolucionesComponent implements OnInit {

  constructor(private _store: Store<State>, private _sandbox: RadicarComunicacionesSandBox, private _constSandbox: ConstanteSandbox, private formBuilder: FormBuilder) {
  }

  form: FormGroup;
  editable = true;

  task: TareaDTO;

  // Unsubscribers
  activeTaskUnsubscriber: Subscription;
  ngOnInit() {

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      this.restore();
    });

    this.initForm();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'nroRadicado': [null],
    });
  }

  restore() {
    if (this.task) {
      this._sandbox.quickRestore(this.task.idInstanciaProceso, this.task.idTarea).take(1).subscribe(response => {
        const results = response.payload;
        if (!results) {
          return;
        }
        console.log("datos de la tarea de devolucion");

        console.log(results);
      });
    }
  }

}
