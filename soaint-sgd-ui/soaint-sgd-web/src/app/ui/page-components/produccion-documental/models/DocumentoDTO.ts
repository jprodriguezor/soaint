import {ConstanteDTO} from '../../../../domain/constanteDTO';


export interface AnexoDTO {
    id: string,
    soporte: string,
    tipo: ConstanteDTO,
    descripcion?: string,
    file?: File,
}

export interface VersionDocumentoDTO {
    id: string,
    tipo: string,
    nombre: string,
    size: number,
    version?: string,
    contenido?: string,
    file?: Blob
}
