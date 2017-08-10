import {Actions, ActionTypes} from './radicarComunicaciones-actions';
import {tassign} from 'tassign';
import {ComunicacionOficialDTO} from 'app/domain/comunicacionOficialDTO';


export interface State {
  entrada: {
    correspondencia: any,
    tipoComunicacion: string,
    sedeRemitente: any,
    destinatarioOriginal: any
  }
}

const initialState: State = {
  entrada: {
    correspondencia: null,
    tipoComunicacion: null,
    sedeRemitente: null,
    destinatarioOriginal: null
  }
};

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {
  switch (action.type) {

    case ActionTypes.TRIGGER_EXCLUDE_SEDE_REMITENTE_FROM_DESTINATARIO: {
      const sedeAdministrativaRemitente = action.payload;

      return tassign(state, {
        entrada: {
          correspondencia: state.entrada.correspondencia,
          tipoComunicacion: state.entrada.tipoComunicacion,
          destinatarioOriginal: state.entrada.destinatarioOriginal,
          sedeRemitente: sedeAdministrativaRemitente
        }
      });
    }

    case ActionTypes.TRIGGER_EXCLUDE_SEDE_REMITENTE_FROM_DESTINATARIO: {
      const destinatarioOriginal = action.payload;
      return tassign(state, {
        entrada: {
          correspondencia: state.entrada.correspondencia,
          tipoComunicacion: state.entrada.tipoComunicacion,
          destinatarioOriginal: destinatarioOriginal,
          sedeRemitente: state.entrada.sedeRemitente
        }
      });
    }

    case ActionTypes.RADICAR_SUCCESS: {
      const payload = action.payload;
      return tassign(state, {
        entrada: {
          correspondencia: payload
        }
      });
    }

    default:
      return state;
  }
}


