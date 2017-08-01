import {ActionTypes, Actions} from './actions';
import {tassign} from 'tassign';

type messageType = 'info' | 'error' | 'success' | 'warning';

interface Notification {
  message_summary: string;
  message_description: any;
  message_type: messageType,
  action?: any
}

export interface State {
  notifications: { [id: string]: Notification }
}

const initialState: State = {
  notifications: {}
}

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {
  switch (action.type) {

    default:
      return state;
  }
}


