import {RolDTO} from '../../../../domain/rolesDTO';
import {FuncionarioDTO} from '../../../../domain/funcionarioDTO';

export interface ObservacionDTO {
    rol: RolDTO,
    funcionario: FuncionarioDTO,
    observaciones: string,
    fecha: Date
}