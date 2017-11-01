import {ChangeDetectorRef, Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {createSelector} from 'reselect';
import {getActiveTask} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {PdMessageService} from './providers/PdMessageService';
import {Subscription} from 'rxjs/Subscription';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {TaskForm} from 'app/shared/interfaces/task-form.interface';
import {Observable} from 'rxjs/Observable';
import {TareaDTO} from 'app/domain/tareaDTO';
import {TaskTypes} from 'app/shared/type-cheking-clasess/class-types';
import {EntradaProcesoDTO} from '../../../domain/EntradaProcesoDTO';

@Component({
  selector: 'produccion-documental',
  templateUrl: './produccion-documental.component.html',
  styleUrls: ['produccion-documental.component.css'],
})

export class ProduccionDocumentalComponent implements OnInit, OnDestroy, TaskForm {
    task: TareaDTO;
    type = TaskTypes.TASK_FORM;
    requiereRevision = true;

    @ViewChild('datosGenerales') datosGenerales;
    @ViewChild('datosContacto') datosContacto;
    @ViewChild('gestionarProduccion') gestionarProduccion;

    tipoComunicacionSelected: ConstanteDTO;
    subscription: Subscription;

    revisar = false;
    aprobar = false;
    tabIndex = 0;

    authPayload: { usuario: string, pass: string } |  {};
    authPayloadUnsubscriber: Subscription;

    constructor(private _store: Store<RootState>,
                private _taskSandBox: TaskSandBox,
                private pdMessageService: PdMessageService) {
        this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => { this.tipoComunicacionSelected = tipoComunicacion; });
        this.authPayloadUnsubscriber = this._store.select(createSelector((s: RootState) => s.auth.profile, (profile) => {
            return profile ? {usuario: profile.username, pass: profile.password} : {};
        })).subscribe((value) => {
            this.authPayload = value;
        });
    }

    enviarParaRevision() {

        this.datosGenerales.form.disable();
        this.datosContacto.form.disable();
        this.gestionarProduccion.form.disable();

        this._taskSandBox.completeTaskDispatch({
            idProceso: this.task.idProceso,
            idDespliegue: this.task.idDespliegue,
            idTarea: this.task.idTarea,
            parametros: {}
        });
    }

    updateTabIndex(event) {
        this.tabIndex = event.index;
    }

    ngOnInit(): void {
        this._store.select(getActiveTask).take(1).subscribe(activeTask => {
            this.task = activeTask;
        });
    }

    ngOnDestroy(): void {
    }

    save(): Observable<any> {
        return Observable.of(true).delay(5000);
    }
}
