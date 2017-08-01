import {ActionTypes, Actions} from './actions';
import {tassign} from 'tassign';

type messageType = 'info' | 'error' | 'success' | 'warning';

interface Notification {
  severity: messageType;
  summary: string;
  detail: string;
  action?: any,
  id: number
}

export interface State {
  ids: number[];
  entities: { [id: string]: Notification },
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
      console.log(newValue);

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


