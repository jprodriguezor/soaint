import { EffectsModule } from '@ngrx/effects';
import { LoginEffects } from 'app/ui/page-components/login/redux-state/login-effects';
import { Effects as ConstanteDtoEffects } from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-effects';
import { Effects as ProcesoDtoEffects } from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-effects';
import { Effects as PaisDtoEffects } from 'app/infrastructure/state-management/paisDTO-state/paisDTO-effects';
import { Effects as DepartamentoDtoEffects } from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-effects';
import { Effects as MunicipioDtoEffects } from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-effects';
import { Effects as DependenciaGrupoDtoEffects } from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-effects';


/**
 * Effects offer a way to isolate and easily test side-effects within your
 * application.
 * The `toPayload` helper function returns just
 * the payload of the currently dispatched action, useful in
 * instances where the current state is not necessary.
 *
 * Documentation on `toPayload` can be found here:
 * https://github.com/ngrx/effects/blob/master/docs/api.md#topayload
 *
 * If you are unfamiliar with the operators being used in these examples, please
 * check out the sources below:
 *
 * Official Docs: http://reactivex.io/rxjs/manual/overview.html#categories-of-operators
 * RxJS 5 Operators By Example: https://gist.github.com/btroncone/d6cf141d6f2c00dc6b35
 */


export const EFFECTS_MODULES = [
  EffectsModule.run(LoginEffects),
  EffectsModule.run(ConstanteDtoEffects),
  EffectsModule.run(ProcesoDtoEffects),
  EffectsModule.run(PaisDtoEffects),
  EffectsModule.run(DepartamentoDtoEffects),
  EffectsModule.run(MunicipioDtoEffects),
  EffectsModule.run(DependenciaGrupoDtoEffects)
];