import {ROUTES_PATH} from '../../app.route-names';

export const MENU_OPTIONS = [
  {label: 'Vista Corporativa', icon: 'dashboard', routerLink: ['/' + ROUTES_PATH.dashboard]},
  {label: 'Tareas', icon: 'list', routerLink: ['/' + ROUTES_PATH.workspace]},
  {label: 'Procesos', icon: 'work', routerLink: ['/' + ROUTES_PATH.processList]},
  {label: 'Asignar comunicaciones', icon: 'subject', routerLink: ['/' + ROUTES_PATH.asignacionComunicaciones]},
  {label: 'Documentos en tramite', icon: 'subject', routerLink: ['/' + ROUTES_PATH.documentosTramite]}
];

export const PROCESS_OPTION = {
  label: 'Procesos', icon: 'dashboard', expanded: true,
  items: []
};
