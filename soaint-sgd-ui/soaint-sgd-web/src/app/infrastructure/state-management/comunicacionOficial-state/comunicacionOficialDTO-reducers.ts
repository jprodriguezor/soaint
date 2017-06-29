import {Actions, ActionTypes as Autocomplete} from './comunicacionOficialDTO-actions';
import {tassign} from 'tassign';
import {CorrespondenciaDTO} from 'app/domain/correspondenciaDTO';


export interface State {
  ids: number[];
  entities: { [ideDocumento: number]: CorrespondenciaDTO };
  selectedId: number;
}

const initialState: State = {
  ids: [],
  entities: {},
  selectedId: null
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
      const values = action.payload.comunicacionesOficiales;
      const newValues = values.filter(data => !state.entities[data.ideDocumento]);

      const newValuesIds = newValues.map(data => data.ideDocumento);
      const newValuesEntities = newValues.reduce((entities: { [ideDocumento: number]: CorrespondenciaDTO }, value: CorrespondenciaDTO) => {
        return Object.assign(entities, {
          [value.ideDocumento]: value
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


