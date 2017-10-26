import {ActionTypes, Actions} from './tareasDTO-actions';
import {tassign} from 'tassign';
import {TareaDTO} from 'app/domain/tareaDTO';
import {LoadNextTaskPayload} from '../../../shared/interfaces/start-process-payload,interface';
import {loadDataReducer} from '../../redux-store/redux-util';


export interface State {
  ids: string[];
  entities: { [idTarea: string]: TareaDTO };
  stats: {name: string, value: number }[];
  activeTask: TareaDTO;
  nextTask: LoadNextTaskPayload,
}

const initialState: State = {
  ids: [],
  entities: {},
  stats: [],
  activeTask: null,
  nextTask: null,
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
      return loadDataReducer(action, state, action.payload, 'idTarea');
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
      return tassign(state, {
        nextTask: nextTask
      });
    }

    case ActionTypes.START_TASK_SUCCESS: {
      const task = action.payload;
      const cloneEntities = tassign({}, state.entities);
      cloneEntities[task.idTarea] = task;

      return tassign(state, {
        entities: cloneEntities,
        activeTask: task
      });

    }

    case ActionTypes.GET_TASK_STATS_SUCCESS: {
      const payload = action.payload;
      if(!Array.isArray(payload)) {
        return state;
      }
      let stats = [];
      payload.forEach(stat => {
        stats.push({ name: stat.status, value: stat.cantidad});
      });

      return tassign(state, {
        stats: stats
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


