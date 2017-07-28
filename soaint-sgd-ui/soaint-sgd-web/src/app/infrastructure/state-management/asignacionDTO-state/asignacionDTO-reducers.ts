import {Actions, ActionTypes as Autocomplete} from './asignacionDTO-actions';
import {AsignacionDTO} from "../../../domain/AsignacionDTO";
import {tassign} from "tassign";


export interface State {
  ids: number[];
  entities: { [ideDocumento: number]: AsignacionDTO };
  justificationDialogVisible: boolean;
}

const initialState: State = {
  ids: [],
  entities: {},
  justificationDialogVisible: false
};

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {
  switch (action.type) {
    case Autocomplete.SET_JUSTIF_DIALOG_VISIBLE: {
      console.log(action.payload);
      return tassign(state, {
        justificationDialogVisible: action.payload
      })
    }

    default:
      return state;
  }
}


