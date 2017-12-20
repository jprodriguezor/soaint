import {PlanAgentesDTO} from "./PlanAgentesDTO";

export interface PlanillaDTO {
  idePlanilla: number;
  nroPlanilla: string;
  fecGeneracion: string;
  codTipoPlanilla: string;
  codFuncGenera: string;
  codSedeOrigen: string;
  codDependenciaOrigen: string;
  codSedeDestino: string;
  codDependenciaDestino: string;
  codClaseEnvio: string;
  codModalidadEnvio: string;
  pagentes: PlanAgentesDTO;
  ideEcm: string;
 
}
