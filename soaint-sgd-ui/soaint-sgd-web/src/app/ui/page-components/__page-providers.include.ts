import {LoginSandbox, AuthenticatedGuard} from './login/__login.include';


export const PAGE_COMPONENTS_PROVIDERS = [
  LoginSandbox,
  AuthenticatedGuard
];
