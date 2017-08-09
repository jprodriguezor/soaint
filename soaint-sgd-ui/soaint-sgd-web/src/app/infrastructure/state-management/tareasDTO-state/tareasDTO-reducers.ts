import {ActionTypes, Actions} from './tareasDTO-actions';
import {tassign} from 'tassign';
import {TareaDTO} from 'app/domain/tareaDTO';
import {StartProcessPayload} from '../../../shared/interfaces/start-process-payload,interface';


export interface State {
  ids: number[];
  entities: { [idTarea: number]: TareaDTO };
  activeTask: TareaDTO;
  nextTask: StartProcessPayload
}

const initialState: State = {
  ids: [],
  entities: {},
  activeTask: null,
  nextTask: null
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

      const values = action.payload;
      const newValues = values.filter(data => !state.entities[data.idTarea]);

      const newValuesIds = newValues.map(data => data.idTarea);
      const newValuesEntities = newValues.reduce((entities: { [idTarea: number]: TareaDTO }, value: TareaDTO) => {
        return Object.assign(entities, {
          [value.idTarea]: value
        });
      }, {});

      return tassign(state, {
        ids: [...newValuesIds, ...state.ids],
        entities: tassign(newValuesEntities, state.entities)
      });

    }

    case ActionTypes.LOCK_ACTIVE_TASK: {
      return tassign(state, {
        activeTask: action.payload, // task
        nextTask: null
      });
    }

    case ActionTypes.UNLOCK_ACTIVE_TASK: {
      return tassign(state, {
        activeTask: null,
        nextTask: null
      });
    }

    case ActionTypes.SCHEDULE_NEXT_TASK: {
      const nextTask = action.payload;
      return tassign( state, {
        nextTask: nextTask
      });
    }

    case ActionTypes.START_TASK_SUCCESS: {

      const task = action.payload;
      const cloneEntities = tassign({}, state.entities);
      cloneEntities[task.idTarea] = task;

      return tassign(state, {
        ids: [task.idTarea, ...state.ids],
        entities: cloneEntities,
        activeTask: task
      });
    }

    case ActionTypes.COMPLETE_TASK_SUCCESS: {

      const task = action.payload;
      const cloneEntities = tassign({}, state.entities);
      delete cloneEntities[state.activeTask.idTarea];

      return tassign(state, {
        ids: state.ids.filter(value => value !== state.activeTask.idTarea),
        entities: cloneEntities,
        activeTask: null
      });
    }

    default:
      return state;
  }
}


