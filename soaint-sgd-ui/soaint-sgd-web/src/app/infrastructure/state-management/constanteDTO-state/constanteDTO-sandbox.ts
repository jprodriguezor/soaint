import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';

import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './constanteDTO-actions';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/delay';
import {ApiBase} from '../../api/api-base';
import {CacheResponse} from "../../../shared/cache-response";

@Injectable()
export class Sandbox extends CacheResponse{

  constructor(private _store: Store<State>,
              private _api: ApiBase) {

    super();
  }

  loadData(payload: actions.GenericFilterAutocomplete) {
    let endpoint = null;
    switch (payload.key) {
      case 'tipoComunicacion':
        endpoint = environment.tipoComunicacion_endpoint;
        break;
      case 'tipoTelefono':
        endpoint = environment.tipoTelefono_endpoint;
        break;
      case 'tipoPersona':
        endpoint = environment.tipoPersona_endpoint;
        break;
      case 'tipoAnexos':
        endpoint = environment.tipoAnexos_endpoint;
        break;
      case 'tipoDocumento':
        endpoint = environment.tipoDocumento_endpoint;
        break;
      case 'tipoDestinatario':
        endpoint = environment.tipoDestinatario_endpoint;
        break;
      case 'unidadTiempo':
        endpoint = environment.unidadTiempo_endpoint;
        break;
      case 'mediosRecepcion':
        endpoint = environment.mediosRecepcion_endpoint;
        break;
      case 'tratamientoCortesia':
        endpoint = environment.tratamientoCortesia_endpoint;
        break;
      case 'actuaCalidad':
        endpoint = environment.actuaCalidad_endpoint;
        break;
      case 'tipologiaDocumental':
        endpoint = environment.tipologiaDocumental_endpoint;
        break;
      case 'tipoVia':
        endpoint = environment.tipoVia_endpoint;
        break;
      case 'orientacion':
        endpoint = environment.orientacion_endpoint;
        break;
      case 'bis':
        endpoint = environment.bis_endpoint;
        break;
      case 'tipoComplemento':
        endpoint = environment.tipoComplemento_endpoint;
        break;
      case 'prefijoCuadrante':
        endpoint = environment.prefijoCuadrante_endpoint;
        break;
      case 'soporteAnexo':
        endpoint = environment.soporteAnexo_endpoint;
        break;
    }

    if (endpoint !== null) {

      return this.getResponse(payload,this._api.list(endpoint, payload)
        .map(response => {
          this.cacheResponse(payload,response);
          return response;
        }));
    }
    return Observable.of([]).delay(400);
    // return Observable.of(this.getMock()).delay(400);
  }

  filterDispatch(target, query) {
    this._store.dispatch(new actions.FilterAction({key: target, data: query}));
  }

  loadDispatch(target) {
    this._store.dispatch(new actions.LoadAction({key: target}));
  }

  loadCausalDevolucionDispatch() {
    this._store.dispatch(new actions.LoadCausalDevolucionAction({key: 'causalDevolucion'}));
  }

  loadDatosGeneralesDispatch() {
    this._store.dispatch(new actions.LoadDatosGeneralesAction());
  }

  loadDatosRemitenteDispatch() {
    this._store.dispatch(new actions.LoadDatosRemitenteAction());
  }

}

