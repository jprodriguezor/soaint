import {ConstanteDTO} from '../../../../domain/constanteDTO';
import {AnexoDTO, VersionDocumentoDTO} from './DocumentoDTO';
import {ProyectorDTO} from '../../../../domain/ProyectorDTO';
import {DestinatarioDTO} from '../../../../domain/destinatarioDTO';
import {AgentDTO} from "../../../../domain/agentDTO";

export interface StatusDTO {
  datosGenerales: {
    tipoComunicacion: ConstanteDTO,
    listaVersionesDocumento: VersionDocumentoDTO[],
    listaAnexos: AnexoDTO[]
  },
  datosContacto: {
    distribucion: string,
    responderRemitente: boolean,
    listaDestinatarios: DestinatarioDTO[],
    remitenteExterno: AgentDTO
  },
  gestionarProduccion: {
    startIndex: number,
    listaProyectores: ProyectorDTO[]
  }
}

export interface VariablesTareaDTO {
  aprobado?: number,
  requiereAjustes?: number,
  listaProyector?: string[],
  listaAprobador?: string[],
  listaRevisor?: string[]
}
