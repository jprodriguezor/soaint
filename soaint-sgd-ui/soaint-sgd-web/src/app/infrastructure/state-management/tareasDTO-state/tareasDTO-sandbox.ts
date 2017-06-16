import {ConstanteDTO} from '../../../domain/constanteDTO';
import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {ListForSelectionApiService} from '../../api/list-for-selection.api.service';
import * as actions from './tareasDTO-actions';
import {tassign} from 'tassign';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _listSelectionService: ListForSelectionApiService) {
  }

  loadData(payload: any) {
    const clonePayload = tassign(payload, {
      estados: [
        'RESERVADO',
        'COMPLETADO',
        'ENPROGRESO',
        'LISTO'
      ]
    });
    return this._listSelectionService.post(environment.tasksForStatus_endpoint, clonePayload);
  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  initTaskDispatch(payload?) {
    this._store.dispatch(new actions.StartTaskAction(payload));
  }

  loadDispatch(payload?) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

}

