import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {PlanillaDTO} from '../../domain/PlanillaDTO';

@Injectable()
export class PlanillasApiService {

  constructor(private _api: ApiBase) {
  }

  generarPlanillas(payload: PlanillaDTO) {
    return this._api.post(environment.generarPlanilla_endpoint, payload);
  }

  exportarPlanilla(payload: { nroPlanilla: string, formato: string }) {
    return this._api.list(environment.exportarPlanilla_endpoint, payload);
  }

  cargarPlanillas(payload: PlanillaDTO) {
    return this._api.post(environment.cargarPlanilla_endpoint, payload);
  }
}
