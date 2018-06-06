import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './asignacionDTO-actions';
import {ApiBase} from '../../api/api-base';
import {ObservacionDTO} from '../../../domain/observacionDTO';
import { isNullOrUndefined } from 'util';

@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
  }

  assignComunications(payload: any) {
    return this._api.post(environment.asignarComunicaciones_endpoint, payload);
  }

  reassignComunications(payload: any) {
    return this._api.post(environment.reasignarComunicaciones_endpoint, payload);
  }

  obtenerObservaciones(documentId: any) {
    return this._api.list(`${environment.obtenerObservaciones_endpoint}${documentId}`);
  }

  obtenerDocumento(documentId: any) {
    return this._api.list(`${environment.obtenerDocumento_endpoint}${documentId}`);
  }

  obtnerConstantesPorCodigos(codigos: string) {
    return this._api.list(`${environment.obtenerConstantesPorCodigo_endpoint}`, {
      codigos: codigos
    });
  }

  obtnerDependenciasPorCodigos(codigos: string) {
    return this._api.list(`${environment.obtenerDependenciasPorCodigo_endpoint}`, {
      codigos: codigos
    });
  }

  obtenerMunicipiosPorCodigos(codigos: string) {
    return this._api.list(`${environment.obtenerMunicipiosPorCodigo_endpoint}`, {
      codigos: codigos
    });
  }

  obtenerComunicacionPorNroRadicado(nroRadicado: string) {
      return this._api.list(`${environment.obtenerComunicacion_endpoint}${nroRadicado}`);
  }

  registrarObservacion(payload: ObservacionDTO) {
    return this._api.post(environment.registrarObservaciones_endpoint, payload);
  }

  redirectComunications(payload: any) {
    return this._api.post(environment.redireccionarComunicaciones_endpoint, payload);
  }

  rejectComunications(payload: any) {
    return this._api.post(environment.devolverComunicaciones_endpoint, payload);
  }

  rejectComunicationsAsignacion(payload: any) {
    return this._api.post(environment.devolverComunicacionesAsigancion_endpoint, payload);
  }
  assignDispatch(payload) {
    this._store.dispatch(new actions.AssignAction(payload));
  }

  reassignDispatch(payload) {
    this._store.dispatch(new actions.ReassignAction(payload));
  }

  redirectDispatch(payload) {
    this._store.dispatch(new actions.RedirectAction(payload));
  }

  setVisibleJustificationDialogDispatch(payload: boolean) {
    this._store.dispatch(new actions.SetJustificationDialogVisibleAction(payload));
  }

  setVisibleDetailsDialogDispatch(payload: boolean) {
    this._store.dispatch(new actions.SetDetailsDialogVisibleAction(payload));
  }

  setVisibleAddObservationsDialogDispatch(payload: boolean) {
    this._store.dispatch(new actions.SetAddObservationsDialogVisibleAction(payload));
  }

  setVisibleRejectDialogDispatch(payload: boolean) {
    this._store.dispatch(new actions.SetRejectDialogVisibleAction(payload));
  }

}

