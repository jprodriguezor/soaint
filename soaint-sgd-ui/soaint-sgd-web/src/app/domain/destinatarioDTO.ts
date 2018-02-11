import {ConstanteDTO} from './constanteDTO';
import {FuncionarioDTO} from './funcionarioDTO';
import {PaisDTO} from './paisDTO';
import {DepartamentoDTO} from './departamentoDTO';
import {MunicipioDTO} from './municipioDTO';

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

    email?: string,
    mobile?: string,
    phone?: string,
    pais?: PaisDTO,
    departamento?: DepartamentoDTO,
    municipio?: MunicipioDTO,
}
