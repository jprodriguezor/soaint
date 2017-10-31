type tareaStatus = 'RESERVADO' | 'ENPROGRESO' | 'LISTO' | 'CANCELADO' | 'COMPLETADO';

export interface TareaDTO {
  idTarea: string;
  nombre: string;
  estado: tareaStatus;
  prioridad: number;
  idResponsable: string;
  idCreador: string;
  fechaCreada: Date;
  tiempoActivacion: Date;
  tiempoExpiracion: Date;
  idProceso: string;
  idInstanciaProceso: string;
  idDespliegue: string;
  idParent: number;
  variables: any
}
