import {ActionTypes, Actions} from './login-actions';
import {tassign} from 'tassign';
import {UserProfile} from '../models/user-profile.model';

export interface State {
  token: string,
  profile: UserProfile;
  isAuthenticated: boolean,
  isLoading: boolean,
  error: string
}

const initialState: State = {
  token: null,
  profile: null,
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
        profile: action.payload.credentials,
        isLoading: false,
        error: null,
        isAuthenticated: true
      });

    case ActionTypes.LOGIN_FAIL:
      return tassign(state, {
        token: null,
        profile: null,
        error: action.payload.error,
        isLoading: false
      });

    case ActionTypes.LOGOUT:
      return tassign(state, {
        token: null,
        profile: null,
        error: null,
        isLoading: false,
        isAuthenticated: false
      });

    default:
      return state;
  }
}


