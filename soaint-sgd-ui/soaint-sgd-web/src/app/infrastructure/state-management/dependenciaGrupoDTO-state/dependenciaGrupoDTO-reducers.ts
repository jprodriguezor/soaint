import {ActionTypes as Autocomplete, Actions} from './dependenciaGrupoDTO-actions';
import {tassign} from 'tassign';
import {ConstanteDTO} from 'app/domain/constanteDTO';


export interface State {
  ids: number[];
  entities: { [idConst: number]: ConstanteDTO };
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
      console.log(action.payload);
      const values = action.payload.constantes;
      const newValues = values.filter(data => !state.entities[data.ideConst]);

      const newValuesIds = newValues.map(data => data.ideConst);
      const newValuesEntities = newValues.reduce((entities: { [ideConst: number]: ConstanteDTO }, value: ConstanteDTO) => {
        return Object.assign(entities, {
          [value.ideConst]: value
        });
      }, {});

      return tassign(state, {
        ids: [...state.ids, ...newValuesIds],
        entities: tassign(state.entities, newValuesEntities),
        selectedId: state.selectedId
      });

    }

    default:
      return state;
  }
}


