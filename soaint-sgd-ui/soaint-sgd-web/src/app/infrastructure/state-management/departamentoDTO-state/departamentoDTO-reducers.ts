import {ActionTypes as Autocomplete, Actions} from './departamentoDTO-actions';
import {tassign} from 'tassign';
import {DepartamentoDTO} from 'app/domain/departamentoDTO';


export interface State {
  ids: number[];
  entities: { [id: number]: DepartamentoDTO };
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
      const values = action.payload.departamentos;
      const newValues = values.filter(data => !state.entities[data.id]);

      const newValuesIds = newValues.map(data => data.id);
      const newValuesEntities = newValues.reduce((entities: { [id: number]: DepartamentoDTO }, value: DepartamentoDTO) => {
        return Object.assign(entities, {
          [value.id]: value
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


