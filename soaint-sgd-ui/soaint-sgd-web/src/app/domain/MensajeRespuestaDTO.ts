import { ContenidoDependenciaTrdDTO } from 'app/domain/ContenidoDependenciaTrdDTO';
import { DocumentoEcmDTO } from 'app/domain/documentoEcmDTO';

export interface MensajeRespuestaDTO {
    codMensaje: string;
    contenidoDependenciaTrdDTOS?: ContenidoDependenciaTrdDTO[];
    documentoDTOList?: DocumentoEcmDTO[];
    mensaje: string;
    response: any;
}
