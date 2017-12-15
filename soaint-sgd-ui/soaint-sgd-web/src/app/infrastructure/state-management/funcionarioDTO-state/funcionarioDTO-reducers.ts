import {Actions, ActionTypes} from './funcionarioDTO-actions';
import {tassign} from 'tassign';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';


export interface State {
  ids: number[];
  entities: { [id: number]: FuncionarioDTO };
  authenticatedFuncionario: FuncionarioDTO;
  selectedDependencyGroup: DependenciaDTO
}

const initialState: State = {
  ids: [],
  entities: {},
  authenticatedFuncionario: null,
  selectedDependencyGroup: null
};

/**
 * The reducer function.
 * @function reducer
 * @param {State} state Current state
 * @param {Actions} action Incoming action
 */
export function reducer(state = initialState, action: Actions) {
  switch (action.type) {

    case ActionTypes.LOAD_SUCCESS: {
      const funcionario = action.payload;
      return tassign(state, {
        authenticatedFuncionario: funcionario,
        selectedDependencyGroup: Array.isArray(funcionario.dependencias) ? funcionario.dependencias[0] : null
      });
    }

    case ActionTypes.LOAD_ALL_SUCCESS: {
      const values = action.payload.funcionarios;
      const newValues = values.filter(data => !state.entities[data.id]);

      const newValuesIds = newValues.map(data => data.id);
      const newValuesEntities = newValues.reduce((entities: { [id: number]: FuncionarioDTO }, value: FuncionarioDTO) => {
        return Object.assign(entities, {
          [value.id]: value
        });
      }, {});

      return tassign(state, {
        ids: [...state.ids, ...newValuesIds],
        entities: tassign(state.entities, newValuesEntities),
      });
    }

    case ActionTypes.SELECT_DEPENDENCY_GROUP: {
      return tassign(state, {
        selectedDependencyGroup: action.payload ? action.payload : (state.authenticatedFuncionario ? state.authenticatedFuncionario.dependencias[0] : null)
      });
    }

    default:
      return state;
  }
}


