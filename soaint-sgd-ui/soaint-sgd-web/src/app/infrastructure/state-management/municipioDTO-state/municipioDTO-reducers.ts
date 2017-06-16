import {ActionTypes as Autocomplete, Actions} from './municipioDTO-actions';
import {tassign} from 'tassign';
import {MunicipioDTO} from 'app/domain/municipioDTO';


export interface State {
  ids: number[];
  entities: { [codigoProceso: number]: MunicipioDTO };
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
      const values = action.payload.municipios;
      const newValues = values.filter(data => !state.entities[data.ideMunic]);

      const newValuesIds = newValues.map(data => data.ideMunic);
      const newValuesEntities = newValues.reduce((entities: { [ideMunic: number]: MunicipioDTO }, value: MunicipioDTO) => {
        return Object.assign(entities, {
          [value.ideMunic]: value
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


