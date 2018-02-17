import {ApiBase} from './api/api-base';
import {DatosGeneralesApiService} from './api/datos-generales.api';
import {PlanillasApiService} from './api/planillas.api';
import {DroolsRedireccionarCorrespondenciaApi} from './api/drools-redireccionar-correspondencia.api';
import {ProduccionDocumentalApiService} from './api/produccion-documental.api';
import {PdMessageService} from '../ui/page-components/produccion-documental/providers/PdMessageService';
import {MessagingService} from '../shared/providers/MessagingService';

export const API_SERVICES = [
  ApiBase,
  DatosGeneralesApiService,
  PlanillasApiService,
  DroolsRedireccionarCorrespondenciaApi,
  ProduccionDocumentalApiService,
  MessagingService,
  PdMessageService
];

export {
  ApiBase,
  DatosGeneralesApiService,
  PlanillasApiService,
  DroolsRedireccionarCorrespondenciaApi,
  ProduccionDocumentalApiService,
  MessagingService,
  PdMessageService
};
