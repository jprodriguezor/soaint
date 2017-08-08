import {LoginSandbox, AuthenticatedGuard} from './login/__login.include';
import {TareaDtoGuard} from '../../infrastructure/state-management/tareasDTO-state/tareasDTO-guard';


export const PAGE_COMPONENTS_PROVIDERS = [
  LoginSandbox,
  AuthenticatedGuard,
  TareaDtoGuard
];
