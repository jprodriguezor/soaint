export interface  UnidadDocumentalDTO {
    id?: string;
    descriptor2?: string;
    fechaCierre?: Date;
    fechaExtremaInicial?: Date;
    fechaExtremaFinal?: Date;
    soporte?: string;
    inactivo?: boolean;
    estado?: string; // depende de inactivo, solo para mostrar
    ubicacionTopografica?: string;
    faseArchivo?: string;
    descriptor1?: string;
    codigoSubSerie?: string;
    nombreSubSerie?: string;
    codigoSerie?: string;
    nombreSerie?: string;
    nombreUnidadDocumental?: string;
    codigoUnidadDocumental?: string;
    codigoDependencia?: string;
    nombreDependencia?: string;
    codigoSede?: string;
    nombreSede?: string;
    cerrada?: boolean;
    disposicion?: string;
    aprobado?: string;
    accion?: string;
    seleccionado?: boolean;
    observacion?: string;
    listaDocumentos?:any[];
}
