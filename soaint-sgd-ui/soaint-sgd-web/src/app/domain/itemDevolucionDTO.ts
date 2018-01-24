import {AgentDTO} from './agentDTO';
import {CorrespondenciaDTO} from "./correspondenciaDTO";

export interface ItemDevolucionDTO {
  agente: AgentDTO;
  causalDevolucion: string;
  correspondencia: CorrespondenciaDTO;
  funDevuelve: String;


}
