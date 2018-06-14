export interface DocumentoDTO {
  idePpdDocumento?: number;
  codTipoDoc?: string;
  descTipoDoc?: string;
  fecDocumento?: string;
  asunto: string;
  nroFolios: number;
  nroAnexos: number;
  codEstDoc?: string;
  ideEcm?: string;
}
