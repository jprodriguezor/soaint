import {ConstanteDTO} from "./constanteDTO";
import { DireccionDTO } from "./DireccionDTO";
import { CorrespondenciaDTO } from "./correspondenciaDTO";
import { AgentDTO } from "./agentDTO";

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
  correspondencia?: CorrespondenciaDTO;
  agente?: AgentDTO;
}
