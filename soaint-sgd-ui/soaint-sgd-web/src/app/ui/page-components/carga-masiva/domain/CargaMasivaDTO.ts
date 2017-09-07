import {RegistroCargaMasivaDTO} from "./RegistroCargaMasivaDTO";

export class CargaMasivaDTO {
  id: number;
  nombre: string;
  fecha_creacion: string;
  total_registros: number;
  estado: string;
  total_registros_exitosos: number;
  total_registros_error: number;
  registros_carga_masiva: RegistroCargaMasivaDTO[]
}
