import {Actions, ActionTypes as Autocomplete} from './sedeAdministrativaDTO-actions';
import {tassign} from 'tassign';
import {OrganigramaDTO} from 'app/domain/organigramaDTO';


export interface State {
  ids: number[];
  entities: { [id: number]: OrganigramaDTO };
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

    case Autocomplete.LOAD_SUCCESS: {
      const values = action.payload.organigrama;
      const newValues = values.filter(data => !state.entities[data.id]);

      const newValuesIds = newValues.map(data => data.id);
      const newValuesEntities = newValues.reduce((entities: { [id: number]: OrganigramaDTO }, value: OrganigramaDTO) => {
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


