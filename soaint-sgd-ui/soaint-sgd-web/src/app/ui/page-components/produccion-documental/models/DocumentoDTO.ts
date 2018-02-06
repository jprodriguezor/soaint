import {ConstanteDTO} from '../../../../domain/constanteDTO';

export type TipoDocumento = 'PDF' | 'HTML';

export interface AnexoDTO {
    id: string,
    soporte: string,
    tipo: ConstanteDTO,
    descripcion: string,
    file?: any,
}

export interface VersionDocumentoDTO {
    id: string,
    tipo: TipoDocumento;
    nombre: string;
    size: number;
    contenido?: string;

    calculateSize(): number;
}

export class VersionDocumento implements VersionDocumentoDTO{
    id: string;
    tipo: TipoDocumento;
    nombre: string;
    size: number;
    contenido?: string;

    constructor (id?: string, nombre?: string, contenido?: string, size?: number, type?: TipoDocumento) {
       this.id = id || (new Date()).getTime().toString();
       this.tipo = type || 'HTML';
       this.size = size || 0;
       this.nombre = nombre || '';
       this.contenido = contenido || '';

       if (contenido && contenido.length > 0) {
          this.calculateSize();
       }
    }

    calculateSize(): number {
      this.size = Math.ceil(this.contenido.length / 1024);
      return this.size;
    }
}
