import {Actions, ActionTypes as Autocomplete} from './asignacionDTO-actions';
import {AsignacionDTO} from "../../../domain/AsignacionDTO";
import {tassign} from "tassign";


export interface State {
  ids: number[];
  entities: { [ideDocumento: number]: AsignacionDTO };
  justificationDialogVisible: boolean;
  agregarObservacionesDialogVisible: boolean;
  rejectDialogVisible: boolean;
  detailsDialogVisible: boolean;
}

const initialState: State = {
  ids: [],
  entities: {},
  justificationDialogVisible: false,
  agregarObservacionesDialogVisible: false,
  rejectDialogVisible: false,
  detailsDialogVisible: false
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
      });
    }
    case Autocomplete.SET_ADD_OBSERV_DIALOG_VISIBLE: {
      console.log(action.payload);
      return tassign(state, {
        agregarObservacionesDialogVisible: action.payload
      });
    }
    case Autocomplete.SET_REJECT_DIALOG_VISIBLE: {
      console.log(action.payload);
      return tassign(state, {
        rejectDialogVisible: action.payload
      });
    }
    case Autocomplete.SET_DETAILS_DIALOG_VISIBLE: {
      console.log(action.payload);
      return tassign(state, {
        detailsDialogVisible: action.payload
      });
    }

    default:
      return state;
  }
}


