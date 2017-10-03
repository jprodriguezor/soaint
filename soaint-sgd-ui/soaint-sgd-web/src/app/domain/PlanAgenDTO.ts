import {ConstanteDTO} from "./constanteDTO";

export interface PlanAgenDTO {
  idePlanAgen: number;
  estado: string;
  varPeso: string;
  varValor: string;
  varNumeroGuia: string;
  fecObservacion: string;
  codNuevaSede: string;
  codNuevaDepen: string;
  observaciones: string;
  codCauDevo: string;
  fecCarguePla: string;
  usuario: string;
  ideAgente: number;
  ideDocumento: number;
  nroRadicado: number;
  tipoPersona: ConstanteDTO;
  tipologiaDocumental: ConstanteDTO;
  nit: string;
  nroDocuIdentidad: string;
  nombre: string;
  razonSocial: string;
  folios: number;
  anexos: number;
}
