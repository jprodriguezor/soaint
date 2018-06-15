import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
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

  @Output()
  completado: EventEmitter<any> = new EventEmitter<{}>()

  maxDateValue: Date = new Date();

  estadoEntregaSuggestions: any[] = [
    {nombre: 'ENTREGADO', codigo: 'EN'},
    {nombre: 'DEVUELTO', codigo: 'DV'}
    //{nombre: 'ANULADO', codigo: 'AN'},
    //{nombre: 'PENDIENTE', codigo: 'PD'},
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
      estadoEntrega: [null, [Validators.required]],
      fechaEntrega: [null,  [Validators.required]],
      dependenciaDestino: [null],
      observaciones: [null,  [Validators.required]]
    });
  }

  listarDependencias() {
    this._dependenciaSandbox.loadDependencies({}).subscribe((results) => {
      this.dependencias = results.dependencias;
    });
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

  editarPlanilla() {
    this.completado.emit({});
  }


}
