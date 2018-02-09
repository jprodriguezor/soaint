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
    file?: Blob,
    idEcm?: string,
    calculateSize(): number
}

export class VersionDocumento implements VersionDocumentoDTO{
    id: string;
    tipo: string;
    nombre: string;
    size: number;
    contenido?: string;
    file?: Blob;
    idEcm?: string;

    constructor (nombre?: string, contenido?: string, size?: number, type?: string, file?: Blob) {


      this.id = 'none';
      this.tipo = type || (file && file.type) || 'html';
      this.size = size || (file && Math.ceil(file.size / 1024)) || 0;
      this.nombre = nombre || (file && (<File>file).name) || '';
      this.contenido = contenido || '';
      this.file = file || null;
      this.idEcm = null;

      if (contenido && contenido.length > 0) {this.calculateSize();}
    }

    calculateSize(): number {
      this.size = Math.ceil(this.contenido.length / 1024);
      return this.size;
    }
}
