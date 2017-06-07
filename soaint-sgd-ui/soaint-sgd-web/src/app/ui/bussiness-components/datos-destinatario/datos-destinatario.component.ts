import {Component, OnInit} from '@angular/core';
import {ListaSeleccionSimple} from '../../../domain/lista.seleccion.simple';
import {TipoDestinatarioApiService} from 'app/infrastructure/__api.include';
import {DatosDestinatarioModel} from './datos-destinatario.model';
import {SedeAdministrativaApiService} from '../../../infrastructure/api/sede-administrativa.api.service';
import {DependenciaGrupoApiService} from '../../../infrastructure/api/dependecia-grupo.api.service';

@Component({
  selector: 'app-datos-destinatario',
  templateUrl: './datos-destinatario.component.html'
})
export class DatosDestinatarioComponent implements OnInit {

  //Tipo destinatario
  tipoDestinatarioSeleccionado: ListaSeleccionSimple;

  tiposDestinatariosFiltrados: Array<ListaSeleccionSimple>;

  //Sede administrativa
  sedeAdministrativaSeleccionada: ListaSeleccionSimple;

  sedesAdministrativasFiltradas: Array<ListaSeleccionSimple>;

  //Dependencia Grupo
  dependenciaGrupoSeleccionada: ListaSeleccionSimple;

  dependenciasGruposFiltradas: Array<ListaSeleccionSimple>;

  model: DatosDestinatarioModel;

  destinatarios: Array<any> = [];

  selectedDestinatario: any;

  constructor(private _tipoDestinatarioApi: TipoDestinatarioApiService, private _sedeAdministrativaApi: SedeAdministrativaApiService,
              private _dependenciaGrupoApi: DependenciaGrupoApiService) {
  }

  ngOnInit() {

  }

  adicionarDestinatario() {
    let destinatarios = [...this.destinatarios];
    destinatarios.push({
      dependencia: this.dependenciaGrupoSeleccionada.nombre,
      tipo: this.tipoDestinatarioSeleccionado.nombre,
      sede: this.sedeAdministrativaSeleccionada.nombre,
    });
    this.destinatarios = destinatarios;
  }

  filtrarTiposDestinatarios(event) {
    this.tiposDestinatariosFiltrados = [];
    for (let i = 0; i < this.model.tiposDestinatarios.length; i++) {
      const tipoDestinatario = this.model.tiposDestinatarios[i];
      if (tipoDestinatario.nombre.toLowerCase().indexOf(event.query.toLowerCase()) == 0) {
        this.tiposDestinatariosFiltrados.push(tipoDestinatario);
      }
    }
  }

  onTiposDestinatariosDropDownClick() {
    this.tiposDestinatariosFiltrados = [];

    this._tipoDestinatarioApi.list().subscribe(
      data => this.tiposDestinatariosFiltrados = data,
      error => console.log('Error searching in products api ' + error)
    );
  }

  filtrarSedesAdministrativas(event) {
    this.sedesAdministrativasFiltradas = [];
    for (let i = 0; i < this.model.sedesAdministrativas.length; i++) {
      const sedeAdministrativa = this.model.sedesAdministrativas[i];
      if (sedeAdministrativa.nombre.toLowerCase().indexOf(event.query.toLowerCase()) == 0) {
        this.sedesAdministrativasFiltradas.push(sedeAdministrativa);
      }
    }
  }

  onSedesAdministrativasDropDownClick() {
    this.sedesAdministrativasFiltradas = [];

    this._sedeAdministrativaApi.list().subscribe(
      data => this.sedesAdministrativasFiltradas = data,
      error => console.log('Error searching in products api ' + error)
    );
  }

  // Dependencia Grupo
  filtrarDependenciasGrupos(event) {
    this.dependenciasGruposFiltradas = [];
    for (let i = 0; i < this.model.sedesAdministrativas.length; i++) {
      const dependenciaGrupo = this.model.dependenciasGrupos[i];
      if (dependenciaGrupo.nombre.toLowerCase().indexOf(event.query.toLowerCase()) == 0) {
        this.dependenciasGruposFiltradas.push(dependenciaGrupo);
      }
    }
  }

  onDependenciasGruposDropDownClick() {
    this.dependenciasGruposFiltradas = [];

    this._dependenciaGrupoApi.list().subscribe(
      data => this.dependenciasGruposFiltradas = data,
      error => console.log('Error searching in products api ' + error)
    );
  }

}
