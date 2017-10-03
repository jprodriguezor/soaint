import {PpdTrazDocumentoDTO} from './PpdTrazDocumentoDTO';
import {AgentDTO} from './agentDTO';

export interface RedireccionDTO {
  agentes: Array<AgentDTO>;
  traza: PpdTrazDocumentoDTO;
}
