type tareaStatus = 'Reserved' | 'Completed';

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
