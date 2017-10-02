import {ApiBase} from './api/api-base';
import {DatosGeneralesApiService} from './api/datos-generales.api';
import {PlanillasApiService} from "./api/planillas.api";

export const API_SERVICES = [
  ApiBase,
  DatosGeneralesApiService,
  PlanillasApiService
];

export {
  ApiBase,
  DatosGeneralesApiService,
  PlanillasApiService
};
