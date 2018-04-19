
import {ConstanteDTO} from "../../../domain/constanteDTO";
import {TareaDTO} from "../../../domain/tareaDTO";

export  interface  RadicacionFormInterface{
  generales: {
    fechaRadicacion: any,
    nroRadicado: any,
    tipoComunicacion: any,
    medioRecepcion: any
    empresaMensajeria: any,
    numeroGuia: any,
    tipologiaDocumental: any,
    unidadTiempo: any,
    numeroFolio: any,
    inicioConteo: any,
    reqDistFisica: any,
    reqDigit: any,
    reqDigitInmediata?: any,
    tiempoRespuesta: any,
    asunto: any,
    radicadoReferido?: any,
    tipoAnexos?: any,
    tipoAnexosDescripcion?: any,
    hasAnexos?: any,
    ideDocumento: any,
    idePpdDocumento: any,
  },
  remitente: {
  ideAgente?: any,
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
  radicadosReferidos: Array<{ nombre: string, ideReferido: number, }>,
  descripcionAnexos: Array<{ ideAnexo: string, tipoAnexo: ConstanteDTO, descripcion: string, soporteAnexo: ConstanteDTO }>,
  task?: TareaDTO
}
