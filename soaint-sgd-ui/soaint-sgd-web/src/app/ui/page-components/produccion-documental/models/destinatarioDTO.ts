import {ConstanteDTO} from '../../../../domain/constanteDTO';
import {FuncionarioDTO} from '../../../../domain/funcionarioDTO';
import {PaisDTO} from '../../../../domain/paisDTO';
import {DepartamentoDTO} from '../../../../domain/departamentoDTO';
import {MunicipioDTO} from '../../../../domain/municipioDTO';

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
