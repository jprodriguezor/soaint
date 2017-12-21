import {TareaDTO} from '../../../domain/tareaDTO';
import {ConstanteDTO} from '../../../domain/constanteDTO';

export interface RadicacionEntradaFormInterface {
  destinatario?:{
    tipoDestinatario:any,
    sedeAdministrativa:any,
    dependenciaGrupo:any,
    destinatarioPrincipal:any
  },
  generales:{
    fechaRadicacion:any,
    nroRadicado:any,
    tipoComunicacion:any,
    medioRecepcion:any
    empresaMensajeria:any,
    numeroGuia:any,
    tipologiaDocumental:any,
    unidadTiempo:any,
    numeroFolio:any,
    inicioConteo:any,
    reqDistFisica:any,
    reqDigit:any,
    reqDigitInmediata?:any,
    tiempoRespuesta:any,
    asunto:any,
    radicadoReferido?:any,
    tipoAnexos?:any,
    tipoAnexosDescripcion?:any,
    hasAnexos?:any
  },
  datosContactos?: Array<{
    tipoVia?:any,
    noViaPrincipal?:any,
    prefijoCuadrante?:any,
    bis?:any,
    orientacion?:any,
    noVia?:any,
    prefijoCuadrante_se?:any,
    placa?:any,
    orientacion_se?:any,
    complementoTipo?:any,
    complementoAdicional?:any,
    celular?:any,
    numeroTel?:any,
    correoEle?:any,
    pais?:any,
    departamento?:any,
    municipio?:any,
    principal?:any,
    direccion?: any,
    provinciaEstado?: any,
    ciudad?:any,
  }>,
  radicadosReferidos:Array<{ nombre:string }>,
  remitente:{
    tipoPersona?:any,
    nit?:any,
    actuaCalidad?:any,
    tipoDocumento?:any,
    razonSocial?:any,
    nombreApellidos?:any,
    nroDocumentoIdentidad?:any,
    sedeAdministrativa?:any,
    dependenciaGrupo?:any
  },
  descripcionAnexos:Array<{ tipoAnexo:ConstanteDTO, descripcion:string }>,
  agentesDestinatario:Array<{ tipoDestinatario:ConstanteDTO, sedeAdministrativa:ConstanteDTO, dependenciaGrupo:ConstanteDTO }>
  task?:TareaDTO
}
