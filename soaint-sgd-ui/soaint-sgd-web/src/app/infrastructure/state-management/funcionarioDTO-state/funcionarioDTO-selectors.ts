import {State} from './funcionarioDTO-reducers';
import 'rxjs/add/operator/reduce';
import {createSelector} from 'reselect';
import * as rootStore from 'app/infrastructure/redux-store/redux-reducers';

export const getFuncionario = (state: rootStore.State) => state.tareas;


