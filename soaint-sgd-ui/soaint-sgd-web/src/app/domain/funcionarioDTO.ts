import {RolDTO} from './rolesDTO';
import {DependenciaDTO} from './dependenciaDTO';

export interface FuncionarioDTO {
  id: number;
  codTipDocIdent: string;
  nroIdentificacion: string;
  nombre: string;
  valApellido1: string;
  valApellido2: string;
  codCargo: string;
  corrElectronico: string;
  codOrgaAdmi: string;
  loginName: string;
  estado: string;
  roles: RolDTO[];
  password?: string;
  dependencias: DependenciaDTO[];
}
