import {ActionTypes as Autocomplete, Actions} from './municipioDTO-actions';
import {MunicipioDTO} from 'app/domain/municipioDTO';
import {loadDataReducer} from '../../redux-store/redux-util';


export interface State {
  ids: number[];
  entities: { [id: number]: MunicipioDTO };
  selectedId: number;
}

const initialState: State = {
  ids: [],
  entities: {},
  selectedId: null
}

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {
  switch (action.type) {

    case Autocomplete.FILTER_COMPLETE:
    case Autocomplete.LOAD_SUCCESS: {

      return loadDataReducer(action, state, action.payload.municipios, 'id');

    }

    default:
      return state;
  }
}


