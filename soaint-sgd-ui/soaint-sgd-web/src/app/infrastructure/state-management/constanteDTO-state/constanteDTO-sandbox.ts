import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {environment} from 'environments/environment';

import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {ListForSelectionApiService} from '../../api/list-for-selection.api.service';
import * as actions from './constanteDTO-actions';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _listSelectionService: ListForSelectionApiService,) {
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
      case 'sedeAdministrativa':
        endpoint = environment.sedeAdministrativa_endpoint;
        break;
      case 'dependenciaGrupo':
        endpoint = environment.dependenciaGrupo_endpoint;
        break;
      case 'tratamientoCortesia':
        endpoint = environment.tratamientoCortesia_endpoint;
        break;
      case 'tipologiaDocumental':
        endpoint = environment.tipologiaDocumental_endpoint;
        break;
    }

    if (endpoint !== null) {
      return this._listSelectionService.list(endpoint, payload);
    }
    return Observable.of(<any>[]);
  }

  filterDispatch(target, query) {
    this._store.dispatch(new actions.FilterAction({key: target, data: query}));
  }

  loadDispatch(target) {
    this._store.dispatch(new actions.LoadAction({key: target}));
  }


}

