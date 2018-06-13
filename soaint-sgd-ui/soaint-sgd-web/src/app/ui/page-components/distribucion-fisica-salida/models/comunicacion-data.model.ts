import { ComunicacionOficialDTO } from "../../../../domain/comunicacionOficialDTO";
import { isNullOrUndefined } from "util";

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
    claseEnvio_modaliadCorreo: string;
    ideAgente: string;
    ideDocumento: string;
    envio_nroguia?: string;
    envio_peso?: string;
    envio_valor?: string;

    constructor(comunicacion) {
        this.TransformData(comunicacion);
    }

    TransformData(comunicacion) {
        const agente = this.getDatosDestinatario(comunicacion);
        //** fill data */
        this.nroRadicado = comunicacion.correspondencia.nroRadicado;
        this.fecRadicado = comunicacion.correspondencia.fecRadicado;
        this.tipoDoc = this.findTipoDoc(comunicacion);
        this.nombre_razonSocial = agente.razonSocial || agente.nombre;
        this.codSede = comunicacion.correspondencia.codSede;
        this.nombreSedeDestino = comunicacion.correspondencia.descSede;
        this.codDependencia = comunicacion.correspondencia.codDependencia;
        this.nombreDependenciaDestino = comunicacion.correspondencia.descDependencia;
        this.nombrePais = agente.datosContactoList[0].pais.nombre;
        this.nombreDepartamento = agente.datosContactoList.length && agente.datosContactoList[0].departamento ? agente.datosContactoList[0].departamento.nombre : '';
        this.nombreCiudadMunicipio = agente.datosContactoList.length && agente.datosContactoList[0].municipio ? agente.datosContactoList[0].municipio.nombre : '';
        this.direccion = agente.datosContactoList.length ? agente.datosContactoList[0].direccion : null;
        this.anexos = null;
        this.folio = null;
        this.claseEnvio_modaliadCorreo = (comunicacion.correspondencia.descClaseEnvio && comunicacion.correspondencia.codModalidadEnvio) ? comunicacion.correspondencia.descClaseEnvio  + ' / ' + comunicacion.correspondencia.codModalidadEnvio : '';
        this.ideAgente = agente.ideAgente;
        this.ideDocumento = comunicacion.correspondencia.ideDocumento;
        this.envio_nroguia = null;
        this.envio_peso = null;
        this.envio_valor = null

    }

    findTipoDoc(comunicacion: ComunicacionOficialDTO): string {
        if(!isNullOrUndefined(comunicacion.ppdDocumentoList)) {
          const doc = comunicacion.ppdDocumentoList[0];
          return '';
        } 
        return '';
    }

    getDatosDestinatario(comunicacion) {
        const destinatarioDTV = comunicacion.agentes.find(value => value.codTipAgent === 'TP-AGEI');
        return destinatarioDTV;
    }
}