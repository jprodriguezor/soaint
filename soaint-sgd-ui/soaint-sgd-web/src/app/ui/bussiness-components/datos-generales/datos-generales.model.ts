import {ListaSeleccionSimple} from '../../../domain/lista.seleccion.simple';
export class DatosGeneralesModel {

  tiposComunicacion: Array<ListaSeleccionSimple> = [];

  mediosRecepcion: Array<ListaSeleccionSimple> = [];

  tipologiasDocumentales: Array<ListaSeleccionSimple> = [];

  unidadesTiempo: Array<ListaSeleccionSimple> = [];

  tiposDestinatarios: Array<ListaSeleccionSimple> = [];

  constructor() {
  }

}
