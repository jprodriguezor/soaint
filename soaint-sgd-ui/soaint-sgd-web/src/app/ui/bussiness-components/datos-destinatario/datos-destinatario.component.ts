import {Component, OnInit} from '@angular/core';
import {ListaSeleccionSimple} from '../../../domain/lista.seleccion.simple';
import {TipoDestinatarioService} from '../../../infrastructure/api/tipo.destinatario.service';
import {DatosGeneralesModel} from '../datos-generales/datos-generales.model';

@Component({
  selector: 'app-datos-destinatario',
  templateUrl: './datos-destinatario.component.html'
})
export class DatosDestinatarioComponent implements OnInit {

  tipoDestinatarioSeleccionado: ListaSeleccionSimple;

  tiposDestinatariosFiltrados: Array<ListaSeleccionSimple>;

  model: DatosGeneralesModel;

  constructor(private _tipoDestinatarioApi: TipoDestinatarioService) {
  }

  ngOnInit() {
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

}
