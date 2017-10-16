import {DependenciaDTO} from './dependenciaDTO';
import {FuncionarioDTO} from './funcionarioDTO';
import {ConstanteDTO} from './constanteDTO';

export interface ProyeccionDocumentoDTO {
  sede: ConstanteDTO;
  dependencia: DependenciaDTO;
  funcionario: FuncionarioDTO;
  tipoPlantilla: ConstanteDTO;
}
