import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';


@Component({
  selector: 'app-editar-planilla',
  templateUrl: './editar-planilla.component.html',
  styleUrls: ['./editar-planilla.component.css']
})
export class EditarPlanillaComponent implements OnInit {

  form: FormGroup;

  @Input()
  planilla: any;

  showDependencia = false;

  estadoEntregaSuggestions: any[] = [
    {nombre: 'ENTREGADO', codigo: 'EN'},
    {nombre: 'DEVUELTO', codigo: 'DV'},
    {nombre: 'ANULADO', codigo: 'AN'},
    {nombre: 'PENDIENTE', codigo: 'PD'},
    //{nombre: 'REDIRECCIONAR', codigo: 'RE'},
  ];

  dependencias: DependenciaDTO[] = [];

  constructor(private formBuilder: FormBuilder,
              private _store: Store<RootState>,
              private _dependenciaSandbox: DependenciaSandbox) {
    this.initForm();
  }

  ngOnInit() {
    this.listarDependencias();
  }

  resetData() {
    this.form.reset();
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
    this.showDependencia = this.form.get('estadoEntrega').value && this.form.get('estadoEntrega').value.codigo === 'DV';
  }

  findDependency(code): string {
    const result = this.dependencias.find((element) => element.codigo === code);
    return result ? result.nombre : '';
  }

  findSede(code): string {
    console.log(code);
    const result = this.dependencias.find((element) => element.codSede === code);
    return result ? result.nomSede : '';
  }

}
