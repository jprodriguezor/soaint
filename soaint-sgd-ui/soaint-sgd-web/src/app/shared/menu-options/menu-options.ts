import {ROUTES_PATH} from '../../app.route-names';

export const MENU_OPTIONS = [
  {label: 'Vista Corporativa', icon: 'dashboard', routerLink: ['/' + ROUTES_PATH.dashboard]},
  {label: 'Tareas', icon: 'list', routerLink: ['/' + ROUTES_PATH.workspace]},
  {label: 'Procesos', icon: 'work', routerLink: ['/' + ROUTES_PATH.processList]},
  {label: 'Asignar comunicaciones', icon: 'subject', routerLink: ['/' + ROUTES_PATH.asignacionComunicaciones]},
  {label: 'Radicacion Salida (Prueba)', icon: 'subject', routerLink: ['/' + ROUTES_PATH.radicarCofSalida]},
  {label: 'Carga Masiva Detalles', icon: 'subject', routerLink: ['/' + ROUTES_PATH.cargaMasivaDetails]},
];



export const PROCESS_OPTION = {
  label: 'Procesos', icon: 'dashboard', expanded: true,
  items: []
};
