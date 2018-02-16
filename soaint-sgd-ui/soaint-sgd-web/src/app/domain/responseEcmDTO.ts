import {DocumentoEcmDTO} from './documentoEcmDTO';

export interface ResponseEcmDTO {
    codMensaje: string,
    mensaje: string,
    metadatosDocumentosDTOList?: DocumentoEcmDTO[]
}