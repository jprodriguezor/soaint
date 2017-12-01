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

@Component({
  selector: 'produccion-documental',
  templateUrl: './produccion-documental.component.html',
  styleUrls: ['produccion-documental.component.css'],
})

export class ProduccionDocumentalComponent implements OnInit, OnDestroy, TaskForm {

  task: TareaDTO;
  type = TaskTypes.TASK_FORM;
  variablesTarea: any;

  @ViewChild('datosGenerales') datosGenerales;
  @ViewChild('datosContacto') datosContacto;
  @ViewChild('gestionarProduccion') gestionarProduccion;

  tipoComunicacionSelected: ConstanteDTO;
  subscription: Subscription;

  revisar = false;
  aprobar = false;
  tabIndex = 0;

  authPayload: { usuario: string, pass: string } | {};
  authPayloadUnsubscriber: Subscription;

  constructor(private _store: Store<RootState>,
              private _taskSandBox: TaskSandBox,
              private pdMessageService: PdMessageService) {
    this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => {
      this.tipoComunicacionSelected = tipoComunicacion;
    });
    this.authPayloadUnsubscriber = this._store.select(createSelector((s: RootState) => s.auth.profile, (profile) => {
      return profile ? {usuario: profile.username, pass: profile.password} : {};
    })).subscribe((value) => {
      this.authPayload = value;
    });
  }

  getDatosProduccionDocumental() {
    return {
      datosGenerales: {
        tipoComunicacion: this.datosGenerales.form.get('tipoComunicacion').value,
        tipoPlantilla: this.datosGenerales.form.get('tipoPlantilla').value,
        listaVersionesDocumento: this.datosGenerales.listaVersionesDocumento,
        listaAnexos: this.datosGenerales.listaAnexos
      },
      datosContacto: {
        tipoDestinatarioText: this.datosContacto.form.get('tipoDestinatarioText').value,
        tipoDestinatarioList: this.datosContacto.form.get('tipoDestinatarioList').value,
        tipoDocumentoText: this.datosContacto.form.get('tipoDocumentoText').value,
        tipoDocumentoList: this.datosContacto.form.get('tipoDocumentoList').value,
        tipoPersona: this.datosContacto.form.get('tipoPersona').value,
        nombreApellidos: this.datosContacto.form.get('nombreApellidos').value,
        nit: this.datosContacto.form.get('nit').value,
        razonSocial: this.datosContacto.form.get('razonSocial').value,
        actuaCalidad: this.datosContacto.form.get('actuaCalidad').value,
        sedeAdministrativa: this.datosContacto.form.get('sedeAdministrativa').value,
        dependencia: this.datosContacto.form.get('dependencia').value,
        funcionario: this.datosContacto.form.get('funcionario').value,
      },
      gestionarProduccion: {
        listaDocumentos: this.gestionarProduccion.listaDocumentos
      }
    };
  }

  completarTarea() {

    this.datosGenerales.form.disable();
    this.datosContacto.form.disable();
    this.gestionarProduccion.form.disable();

    this._taskSandBox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: Object.assign(this.variablesTarea, {
        datosPD: JSON.stringify(this.getDatosProduccionDocumental())
      })
    });
  }

  fillData() {
    const data = JSON.parse(this.task.variables.datosPD);
    this.datosGenerales.form.get('tipoComunicacion').setValue(data.tipoComunicacion);
    this.datosGenerales.form.get('tipoPlantilla').setValue(data.tipoPlantilla);
  }

  updateTabIndex(event) {
    this.tabIndex = event.index;
  }

  ngOnInit(): void {
    this._store.select(getActiveTask).take(1).subscribe(activeTask => {
      this.task = activeTask;
    });

    this.variablesTarea = {
      requiereRevision: 1,
      requiereAjustes: 1,
      aprobado: 1,
      usuarioRevisor: this.task.variables.usuarioProyector,
      usuarioAprobador: this.task.variables.usuarioProyector
    };

    if (this.task.variables.hasOwnProperty('datosPD')) {
      this.fillData();
    }
  }

  ngOnDestroy(): void {
  }

  save(): Observable<any> {
    return Observable.of(true).delay(5000);
  }
}
