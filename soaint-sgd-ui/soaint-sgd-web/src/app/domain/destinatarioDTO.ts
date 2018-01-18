import {ConstanteDTO} from './constanteDTO';
import {DireccionDestinatarioDTO} from './direccionDestinatarioDTO';
import {FuncionarioDTO} from './funcionarioDTO';

export interface DestinatarioDTO {
    interno: boolean,
    tipoDestinatario: ConstanteDTO,
    tipoPersona?: ConstanteDTO,
    nombre?: string,
    tipoDocumento?: ConstanteDTO,
    nit?: string,
    actuaCalidad?: ConstanteDTO,
    actuaCalidadNombre?: string,
    sede?: ConstanteDTO,
    dependencia?: ConstanteDTO,
    funcionario?: FuncionarioDTO,
    direccion?: DireccionDestinatarioDTO
}
