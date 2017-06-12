import {ActionTypes as Autocomplete, Actions} from './constanteDTO-actions';
import {tassign} from 'tassign';
import {ConstanteDTO} from 'app/domain/constanteDTO';

interface ConstanteDTOStateInterface {
  ids: number[];
  entities: { [id: number]: ConstanteDTO };
  selectedId?: number | null;
}

class ConstanteDTOStateInstance {
  ids = [];
  entities = {};
  selectedId = null
}

export interface State {
  tipoComunicacion: ConstanteDTOStateInterface;
  tipoPersona: ConstanteDTOStateInterface;
  tipoAnexos: ConstanteDTOStateInterface;
  tipoDocumento: ConstanteDTOStateInterface;
  tipoTelefono: ConstanteDTOStateInterface;
  tipoDestinatario: ConstanteDTOStateInterface;
  unidadTiempo: ConstanteDTOStateInterface;
  mediosRecepcion: ConstanteDTOStateInterface;
}

const initialState: State = {
  tipoComunicacion: new ConstanteDTOStateInstance(),
  tipoPersona: new ConstanteDTOStateInstance(),
  tipoAnexos: new ConstanteDTOStateInstance(),
  tipoDocumento: new ConstanteDTOStateInstance(),
  tipoTelefono: new ConstanteDTOStateInstance(),
  tipoDestinatario: new ConstanteDTOStateInstance(),
  unidadTiempo: new ConstanteDTOStateInstance(),
  mediosRecepcion: new ConstanteDTOStateInstance()
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
      const target = action.payload.key;
      const values = action.payload.data;
      const newValues = values.filter(data => !state[target].entities[data.id]);

      const newValuesIds = newValues.map(data => data.id);
      const newValuesEntities = newValues.reduce((entities: { [id: number]: ConstanteDTO }, value: ConstanteDTO) => {
        return Object.assign(entities, {
          [value.id]: value
        });
      }, {});
      const cloneState = Object.assign({}, state);
      cloneState[target] = {
        ids: [...state[target].ids, ...newValuesIds],
        entities: Object.assign({}, state[target].entities, newValuesEntities),
        selectedBookId: state[target].selectedBookId
      };
      return cloneState;
    }

    default:
      return state;
  }
}


