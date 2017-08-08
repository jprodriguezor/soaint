import {Observable} from 'rxjs/Observable';
import {TareaDTO} from '../../domain/tareaDTO';
import {TaskTypes} from '../type-cheking-clasess/class-types';


export interface TaskForm {
  type?: TaskTypes;
  task: TareaDTO;
  save(): Observable<any>;
}
