import {RadicacionFormInterface} from "./radicacionForm.interface";
import {ConstanteDTO} from "../../../domain/constanteDTO";

export interface RadicacionEntradaFormInterface extends  RadicacionFormInterface{
  datosContactos?: Array<{
    tipoVia?: any,
    noViaPrincipal?: any,
    prefijoCuadrante?: any,
    bis?: any,
    orientacion?: any,
    noVia?: any,
    prefijoCuadrante_se?: any,
    placa?: any,
    orientacion_se?: any,
    complementoTipo?: any,
    complementoAdicional?: any,
    celular?: any,
    numeroTel?: any,
    correoEle?: any,
    pais?: any,
    departamento?: any,
    municipio?: any,
    principal?: any,
    direccion?: any,
    provinciaEstado?: any,
    ciudad?: any,
  }>,
  destinatario?: {
    tipoDestinatario: any,
    sedeAdministrativa: any,
    dependenciaGrupo: any,
    destinatarioPrincipal: any
  },
  agentesDestinatario: Array<{ tipoDestinatario: ConstanteDTO, sedeAdministrativa: ConstanteDTO, dependenciaGrupo: ConstanteDTO }>,
  remitente: {
    tipoPersona?: any,
    nit?: any,
    actuaCalidad?: any,
    tipoDocumento?: any,
    razonSocial?: any,
    nombreApellidos?: any,
    nroDocumentoIdentidad?: any,
    sedeAdministrativa?: any,
    dependenciaGrupo?: any
  },
}
