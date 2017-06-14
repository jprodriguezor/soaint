import {ActionTypes as Autocomplete, Actions} from './procesoDTO-actions';
import {tassign} from 'tassign';
import {ProcesoDTO} from 'app/domain/procesoDTO';


export interface State {
  ids: string[];
  entities: { [codigoProceso: number]: ProcesoDTO };
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
      const values = action.payload.data;
      const newValues = values.filter(data => !state.entities[data.codigoProceso]);

      const newValuesIds = newValues.map(data => data.codigoProceso);
      const newValuesEntities = newValues.reduce((entities: { [codigoProceso: number]: ProcesoDTO }, value: ProcesoDTO) => {
        return Object.assign(entities, {
          [value.codigoProceso]: value
        });
      }, {});

      return tassign( state, {
        ids: [...state.ids, ...newValuesIds],
        entities: tassign(state.entities, newValuesEntities),
        selectedId: state.selectedId
      });

    }

    default:
      return state;
  }
}


