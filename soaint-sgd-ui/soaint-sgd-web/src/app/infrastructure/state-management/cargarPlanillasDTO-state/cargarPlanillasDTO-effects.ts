import {Injectable} from '@angular/core';
import {Actions, Effect, toPayload} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/pairwise';
import 'rxjs/add/observable/combineLatest';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/let';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/withLatestFrom';
import 'rxjs/add/operator/distinctUntilChanged';
import * as actions from './cargarPlanillasDTO-actions';
import {Sandbox} from './cargarPlanillasDTO-sandbox';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {getSelectedDependencyGroupFuncionario} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';

@Injectable()
export class Effects {

  constructor(private actions$: Actions,
              private _store$: Store<RootState>,
              private _sandbox: Sandbox) {
  }

  @Effect()
  load: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.LOAD)
    .map(toPayload)
    .distinctUntilChanged()
    .withLatestFrom(this._store$.select(getSelectedDependencyGroupFuncionario))
    .distinctUntilChanged()
    .switchMap(
      ([payload, state]) => {
        // const new_payload = tassign(payload, {
        //   cod_dependencia: state.codigo
        // });
        return this._sandbox.loadData(payload)
          .map((response) => new actions.LoadSuccessAction(response))
          .catch((error) => Observable.of(new actions.LoadFailAction({error}))
          )
      }
    );

  @Effect()
  reload: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.RELOAD)
    .distinctUntilChanged()
    .withLatestFrom(this._store$.select((state) => state.distribucionFisica.filters), this._store$.select(getSelectedDependencyGroupFuncionario))
    .switchMap(
      ([payload, filters, authFuncionarioDependencySelected]) => {
        const request = Object.assign({}, filters, {
          cod_dependencia: authFuncionarioDependencySelected.codigo
        });
        return this._sandbox.loadData(request)
          .map((response) => new actions.LoadSuccessAction(response))
          .catch((error) => Observable.of(new actions.LoadFailAction({error}))
          )
      }
    );
}
