import { ComunicacionOficialDTO } from "../../../../domain/comunicacionOficialDTO";
import { isNullOrUndefined } from "util";
import { DocumentoDTO } from "../../../../domain/documentoDTO";

export class ComunicacionDataModel {
    nroRadicado: string;
    fecRadicado: Date;
    tipoDoc: string;
    nombre_razonSocial: string;
    codSede: string;
    nombreSedeDestino: string;
    codDependencia: string;
    nombreDependenciaDestino: string;
    nombrePais: string;
    nombreDepartamento: string;
    nombreCiudadMunicipio: string;
    direccion: string;
    folio: string;
    anexos: string;
    claseEnvio_modalidadCorreo: string;
    ideAgente: string;
    ideDocumento: string;
    envio_nroguia?: string;
    envio_peso?: string;
    envio_valor?: string;
    descClaseEnvio: string;
    descModalidadCorreo: string;

    constructor(comunicacion) {
        this.TransformData(comunicacion);
    }

    TransformData(comunicacion) {
        const agente = this.getDatosDestinatario(comunicacion);
        const documento =this.getDocumento(comunicacion);
        //** fill data */
        this.nroRadicado = comunicacion.correspondencia.nroRadicado;
        this.fecRadicado = comunicacion.correspondencia.fecRadicado;
        this.tipoDoc = documento ? documento.descTipoDoc: '';
        this.nombre_razonSocial = agente.razonSocial || agente.nombre;
        this.codSede = comunicacion.correspondencia.codSede;
        this.nombreSedeDestino = comunicacion.correspondencia.descSede;
        this.codDependencia = comunicacion.correspondencia.codDependencia;
        this.nombreDependenciaDestino = comunicacion.correspondencia.descDependencia;
        this.nombrePais = agente.datosContactoList[0].pais.nombre;
        this.nombreDepartamento = agente.datosContactoList.length && agente.datosContactoList[0].departamento ? agente.datosContactoList[0].departamento.nombre : '';
        this.nombreCiudadMunicipio = agente.datosContactoList.length && agente.datosContactoList[0].municipio ? agente.datosContactoList[0].municipio.nombre : '';
        this.direccion = agente.datosContactoList.length ? agente.datosContactoList[0].direccion : null;
        this.anexos = documento ? documento.nroAnexos.toString(): '';
        this.folio = documento ? documento.nroFolios.toString(): '';
        this.claseEnvio_modalidadCorreo = (comunicacion.correspondencia.codModalidadEnvio && comunicacion.correspondencia.codModalidadEnvio) ? comunicacion.correspondencia.descClaseEnvio  + ' / ' + comunicacion.correspondencia.codModalidadEnvio : '';
        this.descClaseEnvio = comunicacion.correspondencia.descClaseEnvio || '';
        this.descModalidadCorreo = comunicacion.correspondencia.descModalidadEnvio || '';
        this.ideAgente = agente.ideAgente;
        this.ideDocumento = comunicacion.correspondencia.ideDocumento;
        this.envio_nroguia = null;
        this.envio_peso = null;
        this.envio_valor = null

    }

    getDocumento(comunicacion): DocumentoDTO {
        if(!isNullOrUndefined(comunicacion.ppdDocumentos)) {
          return comunicacion.ppdDocumentos.length ? comunicacion.ppdDocumentos[0]: null ;
        } 
        return null;
    }

    getDatosDestinatario(comunicacion) {
        const destinatarioDTV = comunicacion.agentes.find(value => value.codTipAgent === 'TP-AGEI');
        return destinatarioDTV;
    }
}