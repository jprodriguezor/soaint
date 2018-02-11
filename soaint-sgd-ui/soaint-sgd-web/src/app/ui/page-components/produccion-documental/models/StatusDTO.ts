import {ConstanteDTO} from "../../../../domain/constanteDTO";
import {AnexoDTO, VersionDocumentoDTO} from "./DocumentoDTO";
import {ProyectorDTO} from "../../../../domain/ProyectorDTO";
import {DestinatarioDTO} from "../../../../domain/destinatarioDTO";

export interface StatusDTO {
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

export interface VariablesTareaDTO {
  aprobado:number,
  codDependenciaProyector:string,
  usuarioProyector:string,
  listaProyector:string[],
  codDependencia?:string,
  listaAprobador?:string[],
  listaRevisor?:string[],
  usuarioRevisor?:string,
  usuarioAprobador?:string
}
