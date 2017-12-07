import {Actions, ActionTypes as Autocomplete} from './cargarPlanillasDTO-actions';
import {tassign} from 'tassign';
import {PlanillaDTO} from '../../../domain/PlanillaDTO';
import {PlanAgenDTO} from '../../../domain/PlanAgenDTO';


export interface State {
  ids: number[];
  entities: { [ideAgente: number]: PlanAgenDTO };
  selectedId: number;
  data: any,
  filters: {
    nro_planilla: string;
  }
}

const initialState: State = {
  ids: [],
  entities: {},
  data: {},
  selectedId: null,
  filters: {
    nro_planilla: null
  }
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
      console.log(action);
      const values = action.payload.pagentes.pagente;
      if (!values) {
        return tassign(state, {
          ids: [],
          entities: {},
          data: action.payload,
          selectedId: null
        });
      }
      const ids = values.map(data => data.ideAgente);
      const entities = values.reduce((entities: { [ideAgente: number]: PlanAgenDTO }, value: PlanAgenDTO) => {
        return Object.assign(entities, {
          [value.ideAgente]: value
        });
      }, {});
      return tassign(state, {
        ids: [...ids],
        entities: entities,
        data: action.payload,
        selectedId: state.selectedId
      });
    }
    case Autocomplete.LOAD: {
      const filters = action.payload;
      return tassign(state, {
        filters: {
          nro_planilla: filters.nro_planilla || null
        }
      });
    }

    default:
      return state;
  }
}


