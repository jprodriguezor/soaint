import {AfterContentInit, AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {TaskTypes} from '../../../shared/type-cheking-clasess/class-types';
import {TaskForm} from '../../../shared/interfaces/task-form.interface';
import {Store} from '@ngrx/store';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {Subscription} from 'rxjs/Subscription';
import {Observable} from 'rxjs/Observable';
import {TareaDTO} from '../../../domain/tareaDTO';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';

@Component({
  selector: 'app-radicar-salida',
  templateUrl: './radicar-salida.component.html',
  styleUrls: ['./radicar-salida.component.css']
})
export class RadicarSalidaComponent implements OnInit, AfterContentInit, AfterViewInit, OnDestroy, TaskForm {

  type = TaskTypes.TASK_FORM;

  @ViewChild('datosGenerales') datosGenerales;

  @ViewChild('datosDestinatario') datosDestinatario;

  editable = true;

  task: any;

  tabIndex = 0;

  private activeTaskUnsubscriber: Subscription;

  private formsTabOrder = [];

  constructor(private _store: Store<RootState>) {
  }

  ngOnInit() {

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
    });
  }

  ngAfterContentInit() {
    this.formsTabOrder.push(this.datosGenerales);
    this.formsTabOrder.push(this.datosDestinatario);
  }

  ngAfterViewInit() {
  }


  openNext() {
    this.tabIndex = (this.tabIndex === 2) ? 0 : this.tabIndex + 1;
  }

  openPrev() {
    this.tabIndex = (this.tabIndex === 0) ? 2 : this.tabIndex - 1;
  }

  updateTabIndex(event) {
    this.tabIndex = event.index;
  }

  getTask(): TareaDTO {
    return this.task;
  }

  save(): Observable<any> {
    return Observable.of(true).delay(5000);
  }

  ngOnDestroy() {
    this.activeTaskUnsubscriber.unsubscribe();
  }

  radicarComunicacion() {
  }

}
