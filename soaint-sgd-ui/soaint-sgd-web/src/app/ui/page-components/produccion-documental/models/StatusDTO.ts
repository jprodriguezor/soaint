import {ConstanteDTO} from "../../../../domain/constanteDTO";
import {AnexoDTO, VersionDocumentoDTO} from "./DocumentoDTO";
import {ProyectorDTO} from "../../../../domain/ProyectorDTO";
import {DestinatarioDTO} from "../../../../domain/destinatarioDTO";

export interface StatusDTO {
  aprobado:number,
  listaProyector?:string[],
  listaAprobador?:string[],
  listaRevisor?:string[],
  usuarioProyector?:string,
  usuarioRevisor?:string,
  usuarioAprobador?:string,
  requiereAjustes?:boolean,
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
