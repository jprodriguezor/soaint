import {ListaSeleccionSimple} from '../../../domain/lista.seleccion.simple';
export class DatosDestinatarioModel {

  tiposDestinatarios: Array<ListaSeleccionSimple> = [];

  sedesAdministrativas: Array<ListaSeleccionSimple> = [];

  dependenciasGrupos: Array<ListaSeleccionSimple> = [];

  constructor() {
  }

}
