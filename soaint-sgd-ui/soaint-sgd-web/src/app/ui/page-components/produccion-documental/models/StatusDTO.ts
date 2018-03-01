import {ConstanteDTO} from '../../../../domain/constanteDTO';
import {AnexoDTO, VersionDocumentoDTO} from './DocumentoDTO';
import {ProyectorDTO} from '../../../../domain/ProyectorDTO';
import {DestinatarioDTO} from '../../../../domain/destinatarioDTO';
import {ObservacionDTO} from './ObservacionDTO';

export interface StatusDTO {
  datosGenerales: {
    tipoComunicacion: ConstanteDTO,
    listaVersionesDocumento: VersionDocumentoDTO[],
    listaAnexos: AnexoDTO[]
  },
  datosContacto: {
    distribucion: string,
    responderRemitente: boolean,
    hasDestinatarioPrincipal: boolean,
    issetListDestinatarioBackend: boolean,
    listaDestinatariosInternos: DestinatarioDTO[],
    listaDestinatariosExternos: DestinatarioDTO[]
  },
  gestionarProduccion: {
    startIndex: number,
    cantObservaciones: number,
    listaProyectores: ProyectorDTO[],
    listaObservaciones: ObservacionDTO[]
  }
}

export interface VariablesTareaDTO {
  aprobado?: number,
  requiereAjustes?: number,
  listaProyector?: string[],
  listaAprobador?: string[],
  listaRevisor?: string[]
}
