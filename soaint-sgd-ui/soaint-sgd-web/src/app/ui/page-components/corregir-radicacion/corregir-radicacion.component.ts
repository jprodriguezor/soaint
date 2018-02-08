import { Component, OnInit, ViewChild } from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormControl} from '@angular/forms';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {Subscription} from 'rxjs/Subscription';
import {Observable} from 'rxjs/Observable';
import {TareaDTO} from '../../../domain/tareaDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Sandbox as AsiganacionDTOSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {RadicacionEntradaDTV} from '../../../shared/data-transformers/radicacionEntradaDTV';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {getArrayData as getFuncionarioArrayData, getAuthenticatedFuncionario, getSelectedDependencyGroupFuncionario} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';
import {ROUTES_PATH} from '../../../app.route-names';
import {go} from '@ngrx/router-store';
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Sandbox as CominicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';

@Component({
  selector: 'app-corregir-radicacion',
  templateUrl: './corregir-radicacion.component.html',
  styleUrls: ['./corregir-radicacion.component.scss'],
})
export class CorregirRadicacionComponent implements OnInit {


  @ViewChild('actualizarDatosGenerales') actualizarDatosGenerales;

  @ViewChild('datosRemitente') datosRemitente;

  @ViewChild('datosDestinatario') datosDestinatario;

  editable = false;

  tabIndex = 0;

  comunicacion: ComunicacionOficialDTO = {};

  task: TareaDTO;
  activeTaskUnsubscriber: Subscription;

  radicacion: ComunicacionOficialDTO;

  radicacionEntradaDTV: any;

  constructor(private _store: Store<State>, private _sandbox: RadicarComunicacionesSandBox, private _asiganacionSandbox: AsiganacionDTOSandbox, private _comunicacionOficialApi: CominicacionOficialSandbox,private _taskSandBox: TaskSandBox, private formBuilder: FormBuilder) {
     //this.initForm();
  }

  ngOnInit() {
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      this.restore();
    });
  }

  abort() {
    this._taskSandBox.abortTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      instanciaProceso: this.task.idInstanciaProceso
    });
  }

  save(): Observable<any> {
    const payload: any = {
      destinatario: this.datosDestinatario.form.value,
      generales: this.actualizarDatosGenerales.form.value,
      remitente: this.datosRemitente.form.value,
      descripcionAnexos: this.actualizarDatosGenerales.descripcionAnexos,
      radicadosReferidos: this.actualizarDatosGenerales.radicadosReferidos,
      agentesDestinatario: this.datosDestinatario.agentesDestinatario
    };

    if (this.datosRemitente.datosContactos) {
      payload.datosContactos = this.datosRemitente.datosContactos.contacts;
      // payload.contactInProgress = this.datosRemitente.datosContactos.form.value
    }

    const tareaDto: any = {
      idTareaProceso: this.task.idTarea,
      idInstanciaProceso: this.task.idInstanciaProceso,
      payload: payload
    };

    return this._sandbox.quickSave(tareaDto);
  }

  restore() {
    if (this.task) {
      console.log(this.task);

      /*this._sandbox.quickRestore(this.task.idInstanciaProceso, this.task.idTarea).subscribe(response => {

        console.log(response);

        const results = response.payload;
        if (!results) {
          return;
        }
        console.log(results);
        // generales
        this.datosGenerales.form.patchValue(results.generales);
        this.datosGenerales.descripcionAnexos = results.descripcionAnexos;
        this.datosGenerales.radicadosReferidos = results.radicadosReferidos;

      });*/

      this._asiganacionSandbox.obtenerComunicacionPorNroRadicado(this.task.variables.numeroRadicado).subscribe((result) => {
        this.comunicacion = result;
        console.log(this.comunicacion);

        this.radicacionEntradaDTV = new RadicacionEntradaDTV(this.comunicacion);
        console.log(this.radicacionEntradaDTV);
        this.datosRemitente.form.patchValue(this.radicacionEntradaDTV.getDatosRemitente());
        this.actualizarDatosGenerales.form.patchValue(this.radicacionEntradaDTV.getRadicacionEntradaFormData());
        //this.contactos$ = this.radicacionEntradaDTV.getDatosContactos();
        //this.destinatarios$ = this.radicacionEntradaDTV.getDatosDestinatarios();

      });
    }
  }
  actualizarComunicacion(){
    console.log(this.comunicacion);


    this._comunicacionOficialApi.actualizarComunicacion(this.comunicacion);

    this._taskSandBox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: {
      }
    });
    this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));

  }

  ngOnDestroy() {

  }

}
