import { UnidadDocumentalDTO } from '../../../../domain/unidadDocumentalDTO';

export interface DisposicionFormBuscarModel {
    unidadDocumental: UnidadDocumentalDTO;
    disposiciones: string[];
}