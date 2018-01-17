import {ConstanteDTO} from './constanteDTO';
import {DireccionDestinatarioDTO} from './direccionDestinatarioDTO';

export interface DestinatarioDTO {
    interno: boolean,
    tipoDestinatario: ConstanteDTO,
    tipoPersona?: ConstanteDTO,
    nombre?: string,
    usuario?: string,
    tipoDocumento?: ConstanteDTO,
    nit?: string,
    actuaCalidad?: ConstanteDTO,
    actuaCalidadNombre?: string,
    sede?: ConstanteDTO,
    dependencia?: ConstanteDTO,
    direccion?: DireccionDestinatarioDTO
}
