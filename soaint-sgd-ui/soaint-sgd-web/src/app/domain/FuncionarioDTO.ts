import {OrganigramaDTO} from './OrganigramaDTO';

export interface FuncionarioDTO {
  ideFunci: number;
  codTipDocIdent: string;
  nroIdentificacion: string;
  nomFuncionario: string;
  valApellido1: string;
  valApellido2: string;
  codCargo: string;
  corrElectronico: string;
  codOrgaAdmi: string;
  loginName: string;
  estado: string;
  sede: OrganigramaDTO;
  dependencia: OrganigramaDTO;
}
