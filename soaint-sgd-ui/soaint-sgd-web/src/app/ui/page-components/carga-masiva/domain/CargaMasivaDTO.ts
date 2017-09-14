import {RegistroCargaMasivaDTO} from "./RegistroCargaMasivaDTO";

export class CargaMasivaDTO {
  idCargaMasiva: number;
  nombreCargaMasiva: string;
  fechaCreacionCargaMasiva: string;
  totalRegistrosCargaMasiva: number;
  estadoCargaMasiva: string;
  totalRegistrosExitososCargaMasiva: number;
  totalRegistrosErrorCargaMasiva: number;
  registrosCargaMasiva: RegistroCargaMasivaDTO[]
}
