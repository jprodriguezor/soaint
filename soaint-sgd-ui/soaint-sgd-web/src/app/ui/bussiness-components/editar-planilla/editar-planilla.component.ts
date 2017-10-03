import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {DependenciaDTO} from "../../../domain/dependenciaDTO";
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Observable} from "rxjs/Observable";
import {getDataobj} from "../../../infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-selectors";
import {Store} from "@ngrx/store";
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';


@Component({
  selector: 'app-editar-planilla',
  templateUrl: './editar-planilla.component.html',
  styleUrls: ['./editar-planilla.component.css']
})
export class EditarPlanillaComponent implements OnInit {

  form: FormGroup;

  planilla$: Observable<any>;

  showDependencia: boolean = false;

  estadoEntregaSuggestions: any[] = [
    {nombre: 'ENTREGADO', codigo: 'EN'},
    {nombre: 'DEVUELTO', codigo: 'DV'},
    {nombre: 'ANULADO', codigo: 'AN'},
    {nombre: 'PENDIENTE', codigo: 'PD'},
  ];

  dependencias: DependenciaDTO[] = [];

  constructor(private formBuilder: FormBuilder,
              private _store: Store<RootState>,
              private _dependenciaSandbox: DependenciaSandbox) {
    this.initForm();
    this.planilla$ = this._store.select(getDataobj);
  }

  ngOnInit() {
    this.listarDependencias();
  }

  initForm() {
    this.form = this.formBuilder.group({
      estadoEntrega: [null],
      fechaEntrega: [null],
      dependenciaDestino: [null],
      observaciones: [null]
    });
  }

  listarDependencias() {
    this._dependenciaSandbox.loadDependencies({}).subscribe((results) => {
      this.dependencias = results.dependencias;
    });
  }

  estadoEntregaChange() {
    this.showDependencia = this.form.get('estadoEntrega').value && this.form.get('estadoEntrega').value.codigo == "DV";
  }

  findDependency(code): string {
    const result = this.dependencias.find((element) => element.codigo == code);
    return result ? result.nombre : '';
  }

  findSede(code): string {
    const result = this.dependencias.find((element) => element.codSede == code);
    return result ? result.nomSede : '';
  }

}
