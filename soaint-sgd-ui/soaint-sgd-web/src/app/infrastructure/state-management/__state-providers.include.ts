import {Sandbox as ConstanteDtoSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {Sandbox as ProcesoDtoSandbox} from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox';
import {Sandbox as MunicipioDtoSandbox} from 'app/infrastructure/state-management/paisDTO-state/paisDTO-sandbox';
import {Sandbox as PaisDtoSandbox} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-sandbox';
import {Sandbox as DepartamentoDtoSandbox} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-sandbox';
import {Sandbox as DependenciaGrupoDtoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Sandbox as tareasDtoSandbox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {Sandbox as RadicarComunicacionesSandbox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {Sandbox as ComunicacionOficialSandbox} from 'app/infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';
import {Sandbox as FuncionarioDtoSandbox} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {Sandbox as SedeAdministrativaDtoSandbox} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-sandbox';
import {Sandbox as AsignacionDtoSandbox} from 'app/infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {Sandbox as NotificationsSandbox} from 'app/infrastructure/state-management/notifications-state/notifications-sandbox';
import {Sandbox as DistribucionFisicaSandbox} from 'app/infrastructure/state-management/distrubucionFisicaDTO-state/distrubucionFisicaDTO-sandbox';
import {Sandbox as CargarPlanillasSandbox} from 'app/infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-sandbox'


export const STATE_MANAGEMENT_PROVIDERS = [
  ConstanteDtoSandbox,
  ProcesoDtoSandbox,
  PaisDtoSandbox,
  DepartamentoDtoSandbox,
  MunicipioDtoSandbox,
  DependenciaGrupoDtoSandbox,
  tareasDtoSandbox,
  RadicarComunicacionesSandbox,
  ComunicacionOficialSandbox,
  RadicarComunicacionesSandbox,
  FuncionarioDtoSandbox,
  SedeAdministrativaDtoSandbox,
  AsignacionDtoSandbox,
  NotificationsSandbox,
  DistribucionFisicaSandbox,
  CargarPlanillasSandbox
];
