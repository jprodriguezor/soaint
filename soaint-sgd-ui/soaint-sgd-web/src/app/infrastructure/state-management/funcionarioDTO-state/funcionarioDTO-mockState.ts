import {State} from './funcionarioDTO-reducers';
const dependence = {
  id: 123,
  codigo: 'SRD',
  nombre: 'Dependencia Prueba',
  ideSede: 1,
  codSede: '1040',
  nomSede: 'Sede de prueba',
  estado: 'active',
};
const funcionario = {
  id: 1,
  codTipDocIdent: '123',
  nroIdentificacion: '123',
  nombre: 'Daniel',
  valApellido1: 'Barrios',
  valApellido2: 'Cardoso',
  codCargo: 'DSR',
  corrElectronico: 'dbarrios1907@gmail.com',
  codOrgaAdmi: 'QUW',
  loginName: 'daniel.barrios',
  estado: 'active',
  roles: [{rol: 'admin'}],
  dependencias: [dependence]
};

export const mockState: State = {
  ids: [1],
  entities: {
    1: funcionario
  },
  authenticatedFuncionario: funcionario,
  selectedDependencyGroup: dependence
};
