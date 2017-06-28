import {Actions, ActionTypes as Autocomplete} from './constanteDTO-actions';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {tassign} from 'tassign';

interface ConstanteDTOStateInterface {
  ids: number[];
  entities: { [ideConst: number]: ConstanteDTO };
  selectedId?: number | null;
  filter?: string;
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
  sedeAdministrativa: ConstanteDTOStateInterface;
  dependenciaGrupo: ConstanteDTOStateInterface;
  tipologiaDocumental: ConstanteDTOStateInterface;
  tratamientoCortesia: ConstanteDTOStateInterface;
  tipoVia: ConstanteDTOStateInterface;
  prefijoCuadrante: ConstanteDTOStateInterface;
  bis: ConstanteDTOStateInterface;
  orientacion: ConstanteDTOStateInterface;
}

const initialState: State = {
  tipoComunicacion: new ConstanteDTOStateInstance(),
  tipoPersona: new ConstanteDTOStateInstance(),
  tipoAnexos: new ConstanteDTOStateInstance(),
  tipoDocumento: new ConstanteDTOStateInstance(),
  tipoTelefono: new ConstanteDTOStateInstance(),
  tipoDestinatario: new ConstanteDTOStateInstance(),
  unidadTiempo: new ConstanteDTOStateInstance(),
  mediosRecepcion: new ConstanteDTOStateInstance(),
  sedeAdministrativa: new ConstanteDTOStateInstance(),
  dependenciaGrupo: new ConstanteDTOStateInstance(),
  tipologiaDocumental: new ConstanteDTOStateInstance(),
  tratamientoCortesia: new ConstanteDTOStateInstance(),
  tipoVia: new ConstanteDTOStateInstance(),
  prefijoCuadrante: new ConstanteDTOStateInstance(),
  bis: new ConstanteDTOStateInstance(),
  orientacion: new ConstanteDTOStateInstance()
}

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {
  switch (action.type) {

    case Autocomplete.LOAD_SUCCESS: {
      console.log(action.payload);
      const target = action.payload.key;
      const values = action.payload.data.constantes;
      const newValues = values.filter(data => !state[target].entities[data.ideConst]);

      const newValuesIds = newValues.map(data => data.ideConst);
      const newValuesEntities = newValues.reduce((entities: { [ideConst: number]: ConstanteDTO }, value: ConstanteDTO) => {
        return Object.assign(entities, {
          [value.ideConst]: value
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

    case Autocomplete.FILTER: {
      console.log(action.payload);0
      const target = action.payload.key;
      const query = action.payload.data;
      const cloneState = Object.assign({}, state);
      cloneState[target] = tassign(cloneState[target], {
        filter: query
      });
      return cloneState;
    }

    default:
      return state;
  }
}

