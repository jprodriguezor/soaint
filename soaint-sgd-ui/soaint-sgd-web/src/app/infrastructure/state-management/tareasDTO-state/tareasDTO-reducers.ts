import {ActionTypes, Actions} from './tareasDTO-actions';
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

    case ActionTypes.FILTER_COMPLETE:
    case ActionTypes.LOAD_SUCCESS: {
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
        ids: [...newValuesIds, ...state.ids ],
        entities: tassign(newValuesEntities, state.entities)
      });

    }

    case ActionTypes.START_TASK_SUCCESS: {

      const task = action.payload;

      if (state.ids.indexOf(task.idTarea) === -1) {
        return state;
      }

      let cloneEntities = tassign({}, state.entities);
      cloneEntities[task.idTarea] = task;

      return tassign(state, {
        entities: cloneEntities
      });
    }

    default:
      return state;
  }
}


