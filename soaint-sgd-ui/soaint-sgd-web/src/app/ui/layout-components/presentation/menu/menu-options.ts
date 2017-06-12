export const MENU_OPTIONS = [
  {label: 'Workspace', icon: 'dashboard', routerLink: ['/home']},
  {label: 'Productos', icon: 'build', routerLink: ['/productos']},
  {
    label: 'Procesos', icon: 'dashboard',
    items: [
      {label: 'Datos generales', icon: 'assignment', routerLink: ['/radicar-comunicaciones']}
    ]
  },
];
