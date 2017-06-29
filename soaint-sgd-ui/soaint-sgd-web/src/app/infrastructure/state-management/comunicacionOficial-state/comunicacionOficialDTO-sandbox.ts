import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './comunicacionOficialDTO-actions';
import {ComunicacionApiService} from '../../api/comunicacionOficial.api.service';
import {URLSearchParams} from '@angular/http';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _comunicacionApiService: ComunicacionApiService) {
  }

  loadData(payload: any) {
    let paramsAux: URLSearchParams = new URLSearchParams();
    paramsAux.append('fecha_ini', payload.fecha_ini);
    paramsAux.append('fecha_fin', payload.fecha_fin);
    paramsAux.append('cod_estado', payload.cod_estado);
    paramsAux.append('cod_dependencia', payload.codigoDependencia);
    return this._comunicacionApiService.listParams(environment.listarCorrespondencia_endpoint, paramsAux);
  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  loadDispatch(payload) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

}

