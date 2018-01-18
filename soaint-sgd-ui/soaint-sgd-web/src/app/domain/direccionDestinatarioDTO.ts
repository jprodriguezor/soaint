import {PaisDTO} from './paisDTO';
import {DepartamentoDTO} from './departamentoDTO';
import {MunicipioDTO} from './municipioDTO';

export interface DireccionDestinatarioDTO {
    email?: string,
    mobile?: string,
    phone?: string,
    pais?: PaisDTO,
    departamento?: DepartamentoDTO,
    municipio?: MunicipioDTO,
}