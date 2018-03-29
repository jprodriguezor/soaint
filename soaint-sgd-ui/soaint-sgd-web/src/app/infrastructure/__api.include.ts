import {ApiBase} from './api/api-base';
import {DatosGeneralesApiService} from './api/datos-generales.api';
import {PlanillasApiService} from './api/planillas.api';
import {DroolsRedireccionarCorrespondenciaApi} from './api/drools-redireccionar-correspondencia.api';
import {ProduccionDocumentalApiService} from './api/produccion-documental.api';
import {PdMessageService} from '../ui/page-components/produccion-documental/providers/PdMessageService';
import {MessagingService} from '../shared/providers/MessagingService';
import { UnidadDocumentalApiService } from 'app/infrastructure/api/unidad-documental.api';
import { SerieSubserieApiService } from 'app/infrastructure/api/serie-subserie.api';
import {SerieService} from './api/serie.service';
import { ConstanteApiService } from './api/constantes.api';
import { CorrespondenciaApiService } from './api/correspondencia.api';
import { DependenciaApiService } from './api/dependencia.api';

export const API_SERVICES = [
  ApiBase,
  DatosGeneralesApiService,
  PlanillasApiService,
  DroolsRedireccionarCorrespondenciaApi,
  ProduccionDocumentalApiService,
  MessagingService,
  PdMessageService,
  UnidadDocumentalApiService,
  SerieSubserieApiService,
  SerieService,
  ConstanteApiService,
  CorrespondenciaApiService,
  DependenciaApiService,
];

export {
  ApiBase,
  DatosGeneralesApiService,
  PlanillasApiService,
  DroolsRedireccionarCorrespondenciaApi,
  ProduccionDocumentalApiService,
  MessagingService,
  PdMessageService,

};
