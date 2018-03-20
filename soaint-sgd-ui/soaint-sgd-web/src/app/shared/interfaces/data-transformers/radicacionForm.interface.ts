
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
    hasAnexos?: any
  },
  radicadosReferidos: Array<{ nombre: string }>,
  descripcionAnexos: Array<{ tipoAnexo: ConstanteDTO, descripcion: string, soporteAnexo: ConstanteDTO }>,
  task?: TareaDTO
}
