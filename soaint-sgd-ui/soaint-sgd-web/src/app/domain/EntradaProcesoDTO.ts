export interface EntradaProcesoDTO {
    idDespliegue: string;
    idProceso: string;
    instanciaProceso: number;
    idTarea: number;
    usuario: string;
    pass: string;
    estados: string[];
    parametros: {};
}
