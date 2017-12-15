import {LoadingService} from './utils/loading.service';

export * from './__api.include';

import {STATE_MANAGEMENT_PROVIDERS} from './state-management/__state-providers.include';
import {EventsService} from './utils/events.service';
import {SessionService} from './utils/session.service';
import {HttpHandler} from './utils/http-handler';
import {ErrorHandlerService} from './utils/error-handler.service';

export * from './security/auth-guard';
export * from './security/authentication.service';
export * from './utils/events.service';
export * from './utils/session.service';
export * from './state-management/__state-effects.include';

export const INFRASTRUCTURE_SERVICES = [
  ...STATE_MANAGEMENT_PROVIDERS,
  EventsService,
  SessionService,
  HttpHandler,
  LoadingService,
  ErrorHandlerService
];
