import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Sandbox as CominicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {FormBuilder, FormGroup} from '@angular/forms';
import {getArrayData as getFuncionarioArrayData} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {getArrayData as ComunicacionesArrayData} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-selectors';
import {Sandbox} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {AsignacionDTO} from '../../../domain/AsignacionDTO';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';

@Component({
  selector: 'app-asignacion-comunicaciones',
  templateUrl: './asignacion-comunicaciones.component.html',
  styleUrls: ['./asignacion-comunicaciones.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class AsignarComunicacionesComponent implements OnInit {

  form: FormGroup;

  comunicaciones$: Observable<ComunicacionOficialDTO[]>;

  estadosCorrespondencia: [{ label: string, value: string }];

  selectedComunications: ComunicacionOficialDTO[] = [];

  asignationType: string = 'manual';

  start_date: Date = new Date();

  end_date: Date = new Date();

  estadoCorrespondencia: any;

  funcionariosSuggestions$: Observable<FuncionarioDTO[]>;

  funcionarioSelected: FuncionarioDTO;

  constructor(private _store: Store<RootState>, private _comunicacionOficialApi: CominicacionOficialSandbox,
              private _asignacionSandbox: AsignacionSandbox, private _funcionarioSandbox: Sandbox, private formBuilder: FormBuilder) {
    this.comunicaciones$ = this._store.select(ComunicacionesArrayData);
    this.funcionariosSuggestions$ = this._store.select(getFuncionarioArrayData);
    this.start_date.setHours(this.start_date.getHours() - 24);
  }

  ngOnInit() {
    this._funcionarioSandbox.loadAllFuncionariosDispatch();
    this.llenarEstadosCorrespondencias();
    this.listarComunicaciones();
    this.initForm();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'funcionario': [null]
    });
  }

  llenarEstadosCorrespondencias() {
    this.estadosCorrespondencia = [
      {
        label: 'SIN ASIGNAR',
        value: 'SA'
      }
    ];
    this.estadoCorrespondencia = this.estadosCorrespondencia[0];
  }

  convertDate(inputFormat) {
    function pad(s) {
      return (s < 10) ? '0' + s : s;
    }

    let d = new Date(inputFormat);
    return [pad(d.getFullYear()), pad(d.getMonth() + 1), d.getDate()].join('-');
  }

  assignComunications() {
    this._asignacionSandbox.assignDispatch({
      asignaciones: this.createAsignacionesDto()
    });
  }

  createAsignacionesDto(): AsignacionDTO[] {
    let asignaciones: AsignacionDTO[] = [];
    this.selectedComunications.forEach((value) => {
      asignaciones.push({
        ideAsignacion: null,
        fecAsignacion: null,
        ideFunci: this.funcionarioSelected.id,
        codDependencia: value.agenteList[0].codDependencia,
        codTipAsignacion: 'TA',
        observaciones: null,
        codTipCausal: null,
        codTipProceso: null,
        ideAsigUltimo: null,
        numRedirecciones: null,
        nivLectura: null,
        nivEscritura: null,
        fechaVencimiento: null,
        idInstancia: null,
        ideAgente: value.agenteList[0].ideAgente,
        ideDocumento: value.correspondencia.ideDocumento,
        nroRadicado: value.correspondencia.nroRadicado,
        loginName: this.funcionarioSelected.loginName
      })
    });
    return asignaciones;
  }

  listarComunicaciones() {
    this._comunicacionOficialApi.loadDispatch({
      fecha_ini: this.convertDate(this.start_date),
      fecha_fin: this.convertDate(this.end_date),
      cod_estado: this.estadoCorrespondencia.value
    });
  }
}

