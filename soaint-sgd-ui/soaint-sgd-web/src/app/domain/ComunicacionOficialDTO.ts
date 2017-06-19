import {ContactoDTO} from './ContactoDTO';
import {ReferidoDTO} from './ReferidoDTO';
import {AnexoDTO} from './AnexoDTO';
import {DocumentoDTO} from './DocumentoDTO';
import {AgentDTO} from './AgentDTO';
import {CorrespondenciaDTO} from './correspondenciaDTO';


export interface ComunicacionOficialDTO {
  correspondencia?: CorrespondenciaDTO,
  agenteList?: Array<AgentDTO>,
  ppdDocumento?: DocumentoDTO,
  anexoList?: Array<AnexoDTO>
  referidoList?: Array<ReferidoDTO>
  datosContactoList?: Array<ContactoDTO>
}
