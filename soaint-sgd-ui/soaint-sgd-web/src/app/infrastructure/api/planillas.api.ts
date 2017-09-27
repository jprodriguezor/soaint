import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {PlanillaDTO} from "../../domain/PlanillaDTO";

@Injectable()
export class PlanillasApiService {

  constructor(private _api: ApiBase) {
  }

  exportarPlanillas(payload: PlanillaDTO) {
    return this._api.post(environment.exportarPlanilla_endpoint);
  }
}
