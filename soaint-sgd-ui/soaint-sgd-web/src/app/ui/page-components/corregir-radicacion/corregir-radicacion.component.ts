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
import { CorregirRadicacionStateService } from './corregir-radication.state.service';

@Component({
  selector: 'app-corregir-radicacion',
  templateUrl: './corregir-radicacion.component.html',
  styleUrls: ['./corregir-radicacion.component.scss'],
})
export class CorregirRadicacionComponent implements OnInit {

  @ViewChild('datosGenerales') actualizarDatosGenerales;

  @ViewChild('datosRemitente') datosRemitente;

  @ViewChild('datosDestinatario') datosDestinatario;

  editable = false;

  tabIndex = 0;

  state: CorregirRadicacionStateService

  constructor(private stateService: CorregirRadicacionStateService) {
    this.state = this.stateService;
  }

  ngOnInit() {
    this.state.Init();
  }


}
