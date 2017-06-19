import {Actions, ActionTypes as Autocomplete} from './radicarComunicaciones-actions';
import {tassign} from 'tassign';
import {ComunicacionOficialDTO} from 'app/domain/ComunicacionOficialDTO';


export interface State {
  ids: number[];
  entities: { [codigoProceso: number]: ComunicacionOficialDTO };
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

    case Autocomplete.RADICAR_SUCCESS: {
      const values = action.payload.paises;
      const newValues = values.filter(data => !state.entities[data.id]);

      const newValuesIds = newValues.map(data => data.id);
      const newValuesEntities = newValues.reduce((entities: { [id: number]: ComunicacionOficialDTO }, value: ComunicacionOficialDTO) => {
        return Object.assign(entities, {
          [value.correspondencia.nroRadicado]: value
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


