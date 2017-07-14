import {Actions, ActionTypes as Autocomplete} from './asignacionDTO-actions';
import {CorrespondenciaDTO} from 'app/domain/correspondenciaDTO';


export interface State {
  ids: number[];
  entities: { [ideDocumento: number]: CorrespondenciaDTO };
  selectedId: number;
}

const initialState: State = {
  ids: [],
  entities: {},
  selectedId: null
};

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {
  switch (action.type) {

    case Autocomplete.ASSIGN_SUCCESS: {
      console.log(action.payload);
    }

    default:
      return state;
  }
}


