const triggerProcess = (data) => {
  console.log(data);
};

export const MENU_OPTIONS = [
  {label: 'Workspace', icon: 'dashboard', routerLink: ['/home']},
  {label: 'Productos', icon: 'build', routerLink: ['/productos']}
];

export const PROCESS_OPTION = {
  label: 'Procesos', icon: 'dashboard', expanded: true,
  items: [
  ]
};
