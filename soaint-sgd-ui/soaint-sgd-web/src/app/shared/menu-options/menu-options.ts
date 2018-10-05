import {ROUTES_PATH} from '../../app.route-names';

export const MENU_OPTIONS = [
  {label: 'Vista Corporativa', icon: 'dashboard', routerLink: ['/' + ROUTES_PATH.dashboard]},
  {label: 'Tareas', icon: 'list', routerLink: ['/' + ROUTES_PATH.workspace]},
  {label: 'Procesos', icon: 'work', routerLink: ['/' + ROUTES_PATH.processList]},
  {label: 'Asignar comunicaciones', icon: 'subject', routerLink: ['/' + ROUTES_PATH.asignacionComunicaciones]},
  {label: 'Radicación Contingencia', icon: 'subject', routerLink: ['/' + ROUTES_PATH.cargaMasiva]},
  {label: 'Distribución física', icon: 'subject', routerLink: ['/' + ROUTES_PATH.distribucionFisica]},
   {label: 'Distribución física Salida', icon: 'subject', routerLink: ['/' + ROUTES_PATH.distribucionFisicaSalida]},
  {label: 'Disposición Final', icon: 'subject', routerLink: ['/' + ROUTES_PATH.disposicionFinal]}

];


export const PROCESS_OPTION = {
  label: 'Procesos', icon: 'dashboard', expanded: true,
  items: []
};
