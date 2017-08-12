import {ActionTypes, Actions} from './notifications-actions';
import {tassign} from 'tassign';
import {ToastConfig} from 'ngx-toastr';

type messageType = 'info' | 'error' | 'success' | 'warn';

export interface CustomNotification {
  severity?: messageType;
  summary: string;
  detail?: string;
  action?: any,
  id?: number,
  options?: ToastConfig
}

export interface State {
  ids: number[];
  entities: { [id: string]: CustomNotification },
  filter: string
}

const initialState: State = {
  ids: [],
  entities: {},
  filter: null
}

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {
  switch (action.type) {

    case ActionTypes.PUSH_NOTIFICATION: {
      const newValue = action.payload;
      const newValueId = state.ids.length + 1;
      return tassign(state, {
        ids: [...state.ids, newValueId],
        entities: tassign(state.entities, {
          [newValueId]: newValue
        })
      });
    }

    default:
      return state;
  }
}


