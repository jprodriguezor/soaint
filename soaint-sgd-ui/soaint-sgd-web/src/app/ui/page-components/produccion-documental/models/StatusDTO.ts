import {ConstanteDTO} from "../../../../domain/constanteDTO";
import {AnexoDTO, VersionDocumentoDTO} from "./DocumentoDTO";
import {ProyectorDTO} from "../../../../domain/ProyectorDTO";

export interface StatusDTO {
  datosGenerales: {
    tipoComunicacion: ConstanteDTO,
    tipoPlantilla: ConstanteDTO,
    listaVersionesDocumento: VersionDocumentoDTO[],
    listaAnexos: AnexoDTO[]
  },
  datosContacto: {
    responderRemitente: boolean,
  },
  gestionarProduccion: {
    listaProyectores: ProyectorDTO[]
  }
}
