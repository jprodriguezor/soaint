const triggerProcess = (data) => {
  console.log(data);
};

export const MENU_OPTIONS = [
  {label: 'Workspace', icon: 'dashboard', routerLink: ['/home']},
  // {label: 'Productos', icon: 'build', routerLink: ['/productos']},
  {
    label: 'Procesos', icon: 'dashboard', expanded: true,
    items: [
      {label: 'Datos generales',  icon: 'assignment', command: (event) => { triggerProcess({data: 'daniel'}) }}
    ]
  },
];
