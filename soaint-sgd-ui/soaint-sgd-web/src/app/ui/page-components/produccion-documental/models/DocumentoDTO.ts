import {ConstanteDTO} from '../../../../domain/constanteDTO';

export type TipoDocumento = 'PDF' | 'HTML';

export interface AnexoDTO {
    soporte: string,
    tipo: ConstanteDTO,
    descripcion: string,
    file?: any,
}

export interface VersionDocumentoDTO {
    tipo: TipoDocumento;
    nombre: string;
    size: number;
    contenido?: string;

}
