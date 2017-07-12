import {Actions, ActionTypes as Autocomplete} from './radicarComunicaciones-actions';
import {tassign} from 'tassign';
import {ComunicacionOficialDTO} from 'app/domain/comunicacionOficialDTO';


export interface State extends ComunicacionOficialDTO {

}

const initialState: State = {
  correspondencia: null,
  agenteList: null,
  ppdDocumentoList: null,
  anexoList: null,
  referidoList: null,
  datosContactoList: null
};

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


