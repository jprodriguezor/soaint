import {ActionTypes, Actions} from './login-actions';
import {tassign} from 'tassign';

export interface State {
  token: string,
  isAuthenticated: boolean,
  isLoading: boolean,
  error: string
}

const initialState: State = {
  token: null,
  isLoading: false,
  error: null,
  isAuthenticated: false
}

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {
  switch (action.type) {

    case ActionTypes.LOGIN_SUCCESS:
      return tassign(state, {
        token: action.payload.token,
        isLoading: false,
        error: null,
        isAuthenticated: true
      });

    case ActionTypes.LOGIN_FAIL:
      return tassign(state, {
        token: null,
        error: action.payload.error,
        isLoading: false
      });

    case ActionTypes.LOGOUT:
      return tassign(state, {
        token: null,
        error: null,
        isLoading: false,
        isAuthenticated: false
      });

    default:
      return state;
  }
}


