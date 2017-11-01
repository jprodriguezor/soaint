export type TipoDocumento = 'PDF' | 'WORD' | 'EXCEL';

export interface VersionDocumentoDTO {
    tipo: TipoDocumento;
    nombre: string;
    size: number;
    descripcion?: string;
}
