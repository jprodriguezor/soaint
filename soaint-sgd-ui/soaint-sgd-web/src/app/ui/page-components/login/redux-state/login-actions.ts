import {Action} from '@ngrx/store';
import {type} from 'app/infrastructure/redux-store/redux-util';

export const ActionTypes = {
  LOGIN: type('[Login] Login Dispatch'),
  LOGIN_SUCCESS: type('[Login] Login SUCCESS'),
  LOGIN_FAIL: type('[Login] Login FAIL'),
  LOGOUT: type('[Logout] Logout Dispatch'),
  LOGOUT_SUCCESS: type('[Logout] Logout SUCCESS'),
  LOGOUT_FAIL: type('[Logout] Logout FAIL'),
};

export class LoginAction implements Action {
  type = ActionTypes.LOGIN;

  constructor(public payload?: any) { }
}

export class LoginSuccessAction implements Action {
  type = ActionTypes.LOGIN_SUCCESS;

  constructor(public payload?: any) { }
}

export class LoginFailAction implements Action {
  type = ActionTypes.LOGIN_FAIL;

  constructor(public payload?: any) { }
}

export class LogoutAction implements Action {
  type = ActionTypes.LOGOUT;

  constructor(public payload?: any) { }
}

export class LogoutSuccessAction implements Action {
  type = ActionTypes.LOGOUT_SUCCESS;

  constructor(public payload?: any) { }
}

export class LogoutFailAction implements Action {
  type = ActionTypes.LOGOUT_FAIL;

  constructor(public payload?: any) { }
}


export type Actions =
  LoginAction |
  LoginSuccessAction |
  LoginFailAction |
  LogoutAction |
  LogoutSuccessAction |
  LogoutFailAction;


