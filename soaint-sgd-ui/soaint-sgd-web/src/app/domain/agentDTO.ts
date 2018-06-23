import {ContactoDTO} from './contactoDTO';
export interface AgentDTO {
  ideAgente?: number;
  codTipoRemite?: string;
  descTipoRemite?: string;
  codTipoPers?: string;
  descTipoPers?: string;
  nombre?: string;
  razonSocial?: string;
  nit?: string;
  codCortesia?: string;
  descCortesia?: string;
  codEnCalidad?: string;
  codTipDocIdent?: string;
  descTipDocIdent?: string;
  nroDocuIdentidad?: string;
  codSede?: string;
  descSede?: string;  
  codDependencia?: string;
  descDependencia?: string;
  codEstado?: string;
  descEstado?: string;
  fecAsignacion?: string;
  codTipAgent: string;
  descTipAgent?: string;
  indOriginal?: string;
  numRedirecciones?: number;
  numDevoluciones?: number;
  datosContactoList?: ContactoDTO[],
  ideFunci?:number
}
