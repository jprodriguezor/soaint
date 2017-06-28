const triggerProcess = (data) => {
  console.log(data);
};

export const MENU_OPTIONS = [
  {label: 'Vista Corporativa', icon: 'dashboard', routerLink: ['/home']},
  {label: 'Tareas', icon: 'list', routerLink: ['/workspace']},
  {label: 'Procesos', icon: 'work', routerLink: ['/process']},
  {label: 'Asignación de comunicaciones', icon: 'work', routerLink: ['/asignacion-comunicaciones']}
];

export const PROCESS_OPTION = {
  label: 'Procesos', icon: 'dashboard', expanded: true,
  items: []
};
