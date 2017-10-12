import {DependenciaDTO} from "./dependenciaDTO";
import {FuncionarioDTO} from "./funcionarioDTO";

export interface ProyeccionDocumentoDTO {
  sede: string;
  dependencia: DependenciaDTO;
  funcionario: FuncionarioDTO;
  tipoPlantilla: string;
}
