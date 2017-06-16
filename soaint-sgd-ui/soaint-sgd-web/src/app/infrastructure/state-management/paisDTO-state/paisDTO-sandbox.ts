import {ConstanteDTO} from '../../../domain/constanteDTO';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {environment} from 'environments/environment';

import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {ListForSelectionApiService} from '../../api/list-for-selection.api.service';
import {createSelector} from 'reselect';
import * as selectors from './paisDTO-selectors';
import * as actions from './paisDTO-actions';
import {EffectsSubscription} from '@ngrx/effects';

@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _listSelectionService: ListForSelectionApiService) {
  }

  loadData(payload: any) {
    console.log(payload);
    return this._listSelectionService.list(environment.pais_endpoint, payload);
  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  loadDispatch() {
    this._store.dispatch(new actions.LoadAction());
  }

}

