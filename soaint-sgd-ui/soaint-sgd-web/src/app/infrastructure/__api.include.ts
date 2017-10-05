import {ApiBase} from './api/api-base';
import {DatosGeneralesApiService} from './api/datos-generales.api';
import {PlanillasApiService} from './api/planillas.api';
import {DroolsRedireccionarCorrespondenciaApi} from './api/drools-redireccionar-correspondencia.api';
import {ProduccionDocumentalApiService} from "./api/produccion-documental.api";

export const API_SERVICES = [
  ApiBase,
  DatosGeneralesApiService,
  PlanillasApiService,
  DroolsRedireccionarCorrespondenciaApi,
  ProduccionDocumentalApiService
];

export {
  ApiBase,
  DatosGeneralesApiService,
  PlanillasApiService,
  DroolsRedireccionarCorrespondenciaApi,
  ProduccionDocumentalApiService
};
