const triggerProcess = (data) => {
  console.log(data);
};

export const MENU_OPTIONS = [
  {label: 'Vista Corporativa', icon: 'dashboard', routerLink: ['/home']},
  {label: 'Tareas', icon: 'dashboard', routerLink: ['/workspace']},
];

export const PROCESS_OPTION = {
  label: 'Procesos', icon: 'dashboard', expanded: true,
  items: [
  ]
};
