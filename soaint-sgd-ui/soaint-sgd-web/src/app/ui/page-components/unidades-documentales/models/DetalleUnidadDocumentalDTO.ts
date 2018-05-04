import { ListadoUnidadConservacionModel } from 'app/ui/page-components/unidades-documentales/models/listado.unidad.conservacion.model';


export interface DetalleUnidadDocumentalDTO {
    identificadorUnidadDocumental: string;
    nombre: string;
    descriptor1: string;
    descriptor2: string;
    unidadesConservacion: ListadoUnidadConservacionModel[];
}
