import { Sandbox as ConstanteDtoSandbox } from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import { Sandbox as ProcesoDtoSandbox } from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox';

export const STATE_MANAGEMENT_PROVIDERS = [
  ConstanteDtoSandbox,
  ProcesoDtoSandbox
];
