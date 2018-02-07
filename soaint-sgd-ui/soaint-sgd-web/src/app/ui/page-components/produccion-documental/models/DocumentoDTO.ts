import {ConstanteDTO} from '../../../../domain/constanteDTO';


export interface AnexoDTO {
    id: string,
    soporte: string,
    tipo: ConstanteDTO,
    descripcion: string,
    file?: any,
}

export interface VersionDocumentoDTO {
    id: string,
    tipo: string,
    nombre: string,
    size: number,
    contenido?: string,
    file?: File,
    calculateSize(): number
}

export class VersionDocumento implements VersionDocumentoDTO{
    id: string;
    tipo: string;
    nombre: string;
    size: number;
    contenido?: string;
    file?: File;

    constructor (id?: string, nombre?: string, contenido?: string, size?: number, type?: string, file?: File) {
       this.id = id || (new Date()).getTime().toString();
       this.tipo = type || (file && file.type) || 'HTML';
       this.size = size || (file && Math.ceil(file.size / 1024)) || 0;
       this.nombre = nombre || (file && file.name) || '';
       this.contenido = contenido || '';
       this.file = file || null;

       if (contenido && contenido.length > 0) {
          this.calculateSize();
       }
    }

    calculateSize(): number {
      this.size = Math.ceil(this.contenido.length / 1024);
      return this.size;
    }
}
