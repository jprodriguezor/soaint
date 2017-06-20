import {ActionTypes as Autocomplete, Actions} from './tareasDTO-actions';
import {tassign} from 'tassign';
import {TareaDTO} from 'app/domain/tareaDTO';


export interface State {
  ids: number[];
  entities: { [idTarea: number]: TareaDTO };
}

const initialState: State = {
  ids: [],
  entities: {}
};

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
      const values = action.payload;
      const newValues = values.filter(data => !state.entities[data.idTarea]);

      const newValuesIds = newValues.map(data => data.idTarea);
      const newValuesEntities = newValues.reduce((entities: { [idTarea: number]: TareaDTO }, value: TareaDTO) => {
        return Object.assign(entities, {
          [value.idTarea]: value
        });
      }, {});

      return tassign(state, {
        ids: [...state.ids, ...newValuesIds],
        entities: tassign(state.entities, newValuesEntities)
      });

    }

    default:
      return state;
  }
}


