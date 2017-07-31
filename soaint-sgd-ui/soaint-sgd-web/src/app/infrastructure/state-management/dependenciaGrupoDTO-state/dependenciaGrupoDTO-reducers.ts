import {ActionTypes as Autocomplete, Actions} from './dependenciaGrupoDTO-actions';
import {tassign} from 'tassign';
import {OrganigramaDTO} from '../../../domain/organigramaDTO';


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
      const valuesIds = values.map(data => data.id);
      const valuesEntities = values.reduce((entities: { [id: number]: OrganigramaDTO }, value: OrganigramaDTO) => {
        return Object.assign(entities, {
          [value.id]: value
        });
      }, {});

      return tassign(state, {
        ids: [...valuesIds],
        entities: tassign({}, valuesEntities),
        selectedId: state.selectedId
      });

    }

    default:
      return state;
  }
}


