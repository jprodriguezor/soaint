import {ContactoDTO} from './contactoDTO';
export interface AgentDTO {
  ideAgente?: number;
  codTipoRemite?: string;
  codTipoPers?: string;
  nombre?: string;
  razonSocial?: string;
  nit?: string;
  codCortesia?: string;
  codEnCalidad?: string;
  codTipDocIdent?: string;
  nroDocuIdentidad?: string;
  codSede?: string;
  codDependencia?: string;
  codEstado?: string;
  fecAsignacion?: string;
  codTipAgent: string;
  indOriginal?: string;
  numRedirecciones?: number;
  numDevoluciones?: number;
  datosContactoList?: ContactoDTO[],
  ideFunci?:number
}
