import {ConstanteDTO} from "../../../../domain/constanteDTO";
import {AnexoDTO, VersionDocumentoDTO} from "./DocumentoDTO";
import {ProyectorDTO} from "../../../../domain/ProyectorDTO";
import {DestinatarioDTO} from "./destinatarioDTO";

export interface StatusDTO {
  requiereRevision?: number,
  requiereAjustes?: number,
  aprobado?: number,
  datosGenerales: {
    tipoComunicacion: ConstanteDTO,
    listaVersionesDocumento: VersionDocumentoDTO[],
    listaAnexos: AnexoDTO[]
  },
  datosContacto: {
    distribucion: string,
    responderRemitente: boolean,
    listaDestinatarios: DestinatarioDTO[]
  },
  gestionarProduccion: {
    listaProyectores: ProyectorDTO[]
  }
}
