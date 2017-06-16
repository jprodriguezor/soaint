import { Sandbox as ConstanteDtoSandbox } from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import { Sandbox as ProcesoDtoSandbox } from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox';
import { Sandbox as MunicipioDtoSandbox } from 'app/infrastructure/state-management/paisDTO-state/paisDTO-sandbox';
import { Sandbox as PaisDtoSandbox } from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-sandbox';
import { Sandbox as DepartamentoDtoSandbox } from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-sandbox';
import { Sandbox as DependenciaGrupoDtoSandbox } from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';

export const STATE_MANAGEMENT_PROVIDERS = [
  ConstanteDtoSandbox,
  ProcesoDtoSandbox,
  PaisDtoSandbox,
  DepartamentoDtoSandbox,
  MunicipioDtoSandbox,
  DependenciaGrupoDtoSandbox
];
