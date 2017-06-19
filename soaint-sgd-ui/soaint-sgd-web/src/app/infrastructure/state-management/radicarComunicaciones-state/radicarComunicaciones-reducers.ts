import {Actions, ActionTypes as Autocomplete} from './radicarComunicaciones-actions';
import {tassign} from 'tassign';
import {ComunicacionOficialDTO} from 'app/domain/ComunicacionOficialDTO';


export interface State {
  comunicacionOficial: ComunicacionOficialDTO
}

const initialState: State = {
  comunicacionOficial: null
};

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {
  switch (action.type) {

    case Autocomplete.RADICAR_SUCCESS: {
      const values = action.payload;


      return tassign(state, {
        comunicacionOficial: values
      });

    }

    default:
      return state;
  }
}


