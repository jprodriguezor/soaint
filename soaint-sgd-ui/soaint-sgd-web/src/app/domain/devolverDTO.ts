import {PpdTrazDocumentoDTO} from './PpdTrazDocumentoDTO';
import {AgentDTO} from './agentDTO';
import {ItemDevolucionDTO} from './itemDevolucionDTO';

export interface DevolverDTO {
  itemsDevolucion: Array<ItemDevolucionDTO>;
  traza: PpdTrazDocumentoDTO;
}
