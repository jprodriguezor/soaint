import {DependenciaDTO} from './dependenciaDTO';
import {FuncionarioDTO} from './funcionarioDTO';
import {ConstanteDTO} from './constanteDTO';
import {RolDTO} from "./rolesDTO";

export interface ProyectorDTO {
  sede: ConstanteDTO;
  dependencia: DependenciaDTO;
  funcionario: FuncionarioDTO;
  tipoPlantilla?: ConstanteDTO;
  rol?: RolDTO;
}
