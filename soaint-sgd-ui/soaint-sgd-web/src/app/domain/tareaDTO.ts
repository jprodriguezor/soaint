type tareaStatus = 'Reserved' | 'Completed' | 'InProgress' | 'Canceled';

export interface TareaDTO {
  idTarea: number;
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
}
