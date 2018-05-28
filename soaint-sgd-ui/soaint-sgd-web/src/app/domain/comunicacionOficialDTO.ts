import {ContactoDTO} from './contactoDTO';
import {ReferidoDTO} from './referidoDTO';
import {AnexoDTO} from './anexoDTO';
import {DocumentoDTO} from './documentoDTO';
import {AgentDTO} from './agentDTO';
import {CorrespondenciaDTO} from './correspondenciaDTO';


export interface ComunicacionOficialDTO {
  correspondencia?: CorrespondenciaDTO,
  agenteList?: Array<AgentDTO>,
  ppdDocumentoList?: Array<DocumentoDTO>,
  anexoList?: Array<AnexoDTO>
  referidoList?: Array<ReferidoDTO>
  datosContactoList?: Array<ContactoDTO>,
  esRemitenteReferidoDestinatario?:boolean;
}
