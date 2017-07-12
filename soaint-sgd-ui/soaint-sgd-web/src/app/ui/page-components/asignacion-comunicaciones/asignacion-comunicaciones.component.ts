import {Component, OnInit} from '@angular/core';
import {Sandbox as CominicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {CorrespondenciaDTO} from '../../../domain/correspondenciaDTO';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {FormBuilder, FormGroup} from '@angular/forms';
import {getArrayData as getFuncionarioArrayData} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {getArrayData as ComunicacionesArrayData} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-selectors';
import {Sandbox} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';

@Component({
  selector: 'app-asignacion-comunicaciones',
  templateUrl: './asignacion-comunicaciones.component.html'
})
export class AsignarComunicacionesComponent implements OnInit {

  form: FormGroup;

  comunicaciones$: Observable<CorrespondenciaDTO[]>;

  estadosCorrespondencia: [{ label: string, value: string }];

  start_date: Date = new Date();

  end_date: Date = new Date();

  estadoCorrespondencia: any;

  funcionariosSuggestions$: Observable<FuncionarioDTO[]>;

  funcionarioSelected$: Observable<any>;

  constructor(private _store: Store<RootState>, private _comunicacionOficialApi: CominicacionOficialSandbox,
              private _funcionarioSandbox: Sandbox, private formBuilder: FormBuilder) {
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
        value: 'A'
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

  listarComunicaciones() {
    this._comunicacionOficialApi.loadDispatch({
      fecha_ini: this.convertDate(this.start_date),
      fecha_fin: this.convertDate(this.end_date),
      cod_estado: this.estadoCorrespondencia.value
    });
  }
}

