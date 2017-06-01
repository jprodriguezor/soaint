import {Component, OnInit} from '@angular/core';
import {DatosGeneralesModel} from './datos-generales.model';
import {ListaSeleccionSimple} from '../../../domain/lista.seleccion.simple';
import {MedioRecepcionService} from '../../../infrastructure/api/medio.recepcion.service';
import {TipoComunicacionService} from '../../../infrastructure/api/tipo.comunicacion.service';
import {TipologiaDocumentalService} from '../../../infrastructure/api/tipologia.documental.service';
import {UnidadTiempoService} from '../../../infrastructure/api/unidad.tiempo.service';

@Component({
  selector: 'app-datos-generales',
  templateUrl: './datos-generales.component.html'
})
export class DatosGeneralesComponent implements OnInit {

  tipoComunicacionSeleccionado: ListaSeleccionSimple;
  medioRecepcionSeleccionado: ListaSeleccionSimple;
  tipologiaDocumentalSeleccionada: ListaSeleccionSimple;
  unidadTiempoSeleccionada: ListaSeleccionSimple;

  model: DatosGeneralesModel;

  tiposComunicacionFiltrados: Array<ListaSeleccionSimple>;

  mediosRecepcionFiltrados: Array<ListaSeleccionSimple>;

  tipologiasDocumentalesFiltradas: Array<ListaSeleccionSimple>;

  unidadesTiempoFiltradas: Array<ListaSeleccionSimple>;


  checkboxValues: string[] = [];

  constructor(private _tipoComunicacionApi: TipoComunicacionService, private _medioRecepcionApi: MedioRecepcionService,
              private _tipologiaDocumentalApi: TipologiaDocumentalService, private _unidadTiempoApi: UnidadTiempoService) {
  }


  ngOnInit(): void {
  }

  filtrarTiposComunicacion(event) {
    this.tiposComunicacionFiltrados = [];
    for (let i = 0; i < this.model.tiposComunicacion.length; i++) {
      const brand = this.model.tiposComunicacion[i];
      if (brand.nombre.toLowerCase().indexOf(event.query.toLowerCase()) == 0) {
        this.tiposComunicacionFiltrados.push(brand);
      }
    }
  }

  filtrarMeidosRecepcion(event) {
    this.mediosRecepcionFiltrados = [];
    for (let i = 0; i < this.model.mediosRecepcion.length; i++) {
      const medioRecepcion = this.model.mediosRecepcion[i];
      if (medioRecepcion.nombre.toLowerCase().indexOf(event.query.toLowerCase()) == 0) {
        this.mediosRecepcionFiltrados.push(medioRecepcion);
      }
    }
  }

  filtrarTipologiasDocumentales(event) {
    this.tipologiasDocumentalesFiltradas = [];
    for (let i = 0; i < this.model.tipologiasDocumentales.length; i++) {
      const tipologiaDocumental = this.model.tipologiasDocumentales[i];
      if (tipologiaDocumental.nombre.toLowerCase().indexOf(event.query.toLowerCase()) == 0) {
        this.tipologiasDocumentalesFiltradas.push(tipologiaDocumental);
      }
    }
  }

  filtrarUnidadesTiempo(event) {
    this.unidadesTiempoFiltradas = [];
    for (let i = 0; i < this.model.unidadesTiempo.length; i++) {
      const unidadTiempo = this.model.unidadesTiempo[i];
      if (unidadTiempo.nombre.toLowerCase().indexOf(event.query.toLowerCase()) == 0) {
        this.unidadesTiempoFiltradas.push(unidadTiempo);
      }
    }
  }

  onTiposComunicacionDropDownClick() {
    this.tiposComunicacionFiltrados = [];

    this._tipoComunicacionApi.list().subscribe(
      data => this.tiposComunicacionFiltrados = data,
      error => console.log('Error searching in products api ' + error)
    );
  }

  onMediosRecepcionDropDownClick() {
    this.mediosRecepcionFiltrados = [];

    this._medioRecepcionApi.list().subscribe(
      data => this.mediosRecepcionFiltrados = data,
      error => console.log('Error searching in products api ' + error)
    );
  }

  onTipologiasDocumentalesDropDownClick() {
    this.tipologiasDocumentalesFiltradas = [];

    this._tipologiaDocumentalApi.list().subscribe(
      data => this.tipologiasDocumentalesFiltradas = data,
      error => console.log('Error searching in tipologiasDocumentales api ' + error)
    );
  }

  onUnidadesTiempoDropDownClick() {
    this.unidadesTiempoFiltradas = [];

    this._unidadTiempoApi.list().subscribe(
      data => this.unidadesTiempoFiltradas = data,
      error => console.log('Error searching in tipologiasDocumentales api ' + error)
    );
  }


}
