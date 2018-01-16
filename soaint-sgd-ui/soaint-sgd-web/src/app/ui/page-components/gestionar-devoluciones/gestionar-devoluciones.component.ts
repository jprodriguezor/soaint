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

@Component({
  selector: 'app-gestionar-devoluciones',
  templateUrl: './gestionar-devoluciones.component.html',
  styleUrls: ['./gestionar-devoluciones.component.scss'],
})
export class GestionarDevolucionesComponent implements OnInit {

  @ViewChild('popupAgregarObservaciones') popupAgregarObservaciones;

  editable = true;
  //form = new FormGroup({});
  causalDevolucion: any;
  usuariodevuelve: any;
  sedeAdministrativa: any;
  dependencia: any;
  observacion: any;
  radicacionEntradaDTV: any;

  funcionarioLog: FuncionarioDTO;

  funcionarioSubcription: Subscription;

  dependenciaSelected$: Observable<DependenciaDTO>;

  dependenciaSelected: DependenciaDTO;

  globalDependencySubcription: Subscription;

  constructor(private _store: Store<State>, private _sandbox: RadicarComunicacionesSandBox, private _constSandbox: ConstanteSandbox, private _taskSandBox: TaskSandBox, private formBuilder: FormBuilder, private _asiganacionSandbox: AsiganacionDTOSandbox) {

    this.funcionarioSubcription = this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });

    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);

    this.initForm();
  }
  form = new FormGroup({
    causalDevolucion: new FormControl(),
    usuariodevuelve: new FormControl(),
    sedeAdministrativa: new FormControl(),
    dependencia: new FormControl(),
    observacion: new FormControl(),
  });

  comunicacion: ComunicacionOficialDTO = {};
  task: TareaDTO;
  activeTaskUnsubscriber: Subscription;

  ngOnInit() {

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      this.restore();
    });

    this.globalDependencySubcription = this.dependenciaSelected$.subscribe((result) => {
      this.dependenciaSelected = result;
    });
  }

  initForm() {
    this.form = this.formBuilder.group({
      'nroRadicado': [null],
      'causalDevolucion': [null],
      'usuariodevuelve': [null],
      'sedeAdministrativa': [null],
      'dependencia': [null],
      'observacion': [null],
    });
  }

  restore() {
    if (this.task) {
      console.log(this.task);
      this.causalDevolucion = this.task.variables.causalDevolucion;
      this.usuariodevuelve = this.task.variables.causalDevolucion;
      this.sedeAdministrativa = this.task.variables.codDependencia;
      this.dependencia = this.task.variables.codDependencia;

      this._asiganacionSandbox.obtenerComunicacionPorNroRadicado(this.task.variables.numeroRadicado).subscribe((result) => {
        this.comunicacion = result;

        this.radicacionEntradaDTV = new RadicacionEntradaDTV(this.comunicacion);

        this.popupAgregarObservaciones.form.reset();
        this.popupAgregarObservaciones.setData({
          idDocumento: this.radicacionEntradaDTV.source.correspondencia.ideDocumento,
          idFuncionario: this.funcionarioLog.id,
          codOrgaAdmin: this.dependenciaSelected.codigo
        });
        this.popupAgregarObservaciones.loadObservations();

      });

    }
  }

  rechazarDevolucion(){

    this._taskSandBox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: {
        devolucion: 1,
      }
    });

    this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
  }

  gestionarDevolucion(){

    this._taskSandBox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: {
        devolucion: 2,
      }
    });
  }
}
