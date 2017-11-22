import {ROUTES_PATH} from '../../app.route-names';

export const MENU_OPTIONS = [
  {label: 'Vista Corporativa', icon: 'dashboard', routerLink: ['/' + ROUTES_PATH.dashboard]},
  {label: 'Tareas', icon: 'list', routerLink: ['/' + ROUTES_PATH.workspace]},
  {label: 'Procesos', icon: 'work', routerLink: ['/' + ROUTES_PATH.processList]},
  {label: 'Asignar comunicaciones', icon: 'subject', routerLink: ['/' + ROUTES_PATH.asignacionComunicaciones]},
  {label: 'Carga masiva', icon: 'subject', routerLink: ['/' + ROUTES_PATH.cargaMasiva]},
  {label: 'Distribución física', icon: 'subject', routerLink: ['/' + ROUTES_PATH.distribucionFisica]},
  {label: 'Seleccionar UD', icon: 'subject', routerLink: ['/' + ROUTES_PATH.seleccionarUnidadDocumental]},
  {label: 'Seleccionar Documentos', icon: 'subject', routerLink: ['/' + ROUTES_PATH.seleccionarDocumentos]},
];


export const PROCESS_OPTION = {
  label: 'Procesos', icon: 'dashboard', expanded: true,
  items: []
};
