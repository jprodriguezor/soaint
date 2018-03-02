import { EstadoUnidadDocumental } from 'app/ui/page-components/unidades-documentales/models/enums/estado.unidad.documental.enum';


 export interface ListadoUnidadConservacionModel {
    item: number;
    nombreSerieSubserie: string;
    identificadorUnidadDocumental: string;
    nombre: string;
    descriptor1: string;
    descriptor2: string;
    faseArchivo: string;
    estado: EstadoUnidadDocumental;
    fechaExtremaInicial: string;
    fechaExtremaFinal: string;
    fechaCierreTramite: string;
    soporte: string;
 }
