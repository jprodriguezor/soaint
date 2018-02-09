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

@Component({
  selector: 'app-gestionar-devoluciones',
  templateUrl: './gestionar-devoluciones.component.html',
  styleUrls: ['./gestionar-devoluciones.component.scss'],
})
export class GestionarDevolucionesComponent implements OnInit {

  @ViewChild('popupAgregarObservaciones') popupAgregarObservaciones;

  @ViewChild('documentosECMList') documentosECMList;


  causalDevolucion: any;
  usuariodevuelve: any;
  sedeAdministrativa: any;
  dependencia: any;

  causalText: String;

  sedeCode: String;
  dependenciaCode: String;

  dependencias: DependenciaDTO[] = [];
  disabledDevolucionRechazar: Boolean = false;
  disabledDevolucionAction: Boolean = false;

  comunicacion: ComunicacionOficialDTO = {};

  task: TareaDTO;
  activeTaskUnsubscriber: Subscription;

  constructor(private _store: Store<State>,private _dependenciaSandbox: DependenciaSandbox , private _sandbox: RadicarComunicacionesSandBox, private _constSandbox: ConstanteSandbox, private _taskSandBox: TaskSandBox, private formBuilder: FormBuilder, private _asiganacionSandbox: AsiganacionDTOSandbox) {
     this.initForm();
  }
  form = new FormGroup({
    causalDevolucion: new FormControl(),
    usuariodevuelve: new FormControl(),
    sedeAdministrativa: new FormControl(),
    dependencia: new FormControl(),
    observacion: new FormControl(),
  });

  ngOnInit() {

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      this.restore();
    });

    this.verificaVisibilidadRechazar();

    this._dependenciaSandbox.loadDependencies({}).subscribe((results) => {

      this.dependencias = results.dependencias;

      const objDependencia  = results.dependencias.find((element) => element.codigo === this.dependenciaCode);
      const objSede  = results.dependencias.find((element) => element.codSede === this.sedeCode);

      this.form.get("dependencia").setValue(objDependencia ? objDependencia.nombre : '');
      this.form.get("sedeAdministrativa").setValue(objSede ? objSede.nomSede : '');

    });

  }

  initForm() {
    this.form = this.formBuilder.group({
      'nroRadicado': [null],
      'causalDevolucion': [null],
      'usuariodevuelve': [null],
      'sedeAdministrativa': [null],
      'dependencia': [null],
    });
  }

  restore() {
    if (this.task) {
      console.log(this.task);
      if("1" == this.task.variables.causalD){
        this.causalText = "Calidad Imagen";
      }else if("2" == this.task.variables.causalD){
        this.causalText = "Datos incorrectos";
      }else if("3" == this.task.variables.causalD){
        this.causalText = "Supera los intentos permitidos de Redireccionamiento";
      }

      this.causalDevolucion =  this.causalText;
      this.usuariodevuelve = this.task.variables.funDevuelve;

      this._asiganacionSandbox.obtenerComunicacionPorNroRadicado(this.task.variables.numeroRadicado).subscribe((result) => {

        this.comunicacion = result;

        this.dependenciaCode= this.comunicacion.correspondencia.codDependencia;
        this.sedeCode =  this.comunicacion.correspondencia.codSede;

        if(this.comunicacion){

            this.popupAgregarObservaciones.form.reset();
            this.popupAgregarObservaciones.setData({
              idDocumento: this.comunicacion.correspondencia.ideDocumento,
              idFuncionario: this.comunicacion.correspondencia.codFuncRadica,
              codOrgaAdmin: this.comunicacion.correspondencia.codDependencia,
              isPopup: false
            });
            this.popupAgregarObservaciones.loadObservations();

            //para la lista de documentos
            this.documentosECMList.setDataDocument({
              comunicacion: this.comunicacion,
              versionar: false,
            });
            this.documentosECMList.loadDocumentos();
        }


      });
    }
  }

  handleCountObservaciones(count){
    if(count == 0){
      this.disabledDevolucionRechazar = true;
      this.disabledDevolucionAction = true;
    }else{
      this.disabledDevolucionRechazar = false;
      this.disabledDevolucionAction = false;
    }
    this.verificaVisibilidadRechazar();
  }

  verificaVisibilidadRechazar(){
    if("3" == this.task.variables.causalD){
      this.disabledDevolucionRechazar = true;
    }
  }

  rechazarDevolucion(){

    this._taskSandBox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: {
        devolucion: 1,
        requiereDigitalizacion: this.comunicacion.correspondencia.reqDigita,
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
        requiereDigitalizacion: this.comunicacion.correspondencia.reqDigita,
      }
    });
    this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
  }

  ngOnDestroy() {
    this.activeTaskUnsubscriber.unsubscribe();
  }

}
