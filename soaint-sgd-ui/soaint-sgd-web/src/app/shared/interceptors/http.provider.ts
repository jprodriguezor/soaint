import {Http, RequestOptions, XHRBackend} from '@angular/http';

import {PendingRequestInterceptor} from './pending-request.interceptor';
import {InterceptableHttp} from './interceptale-http';
import {Injector} from '@angular/core';
import {AuthExpiredInterceptor} from './auth-expired.interceptor';
import {ErrorHandlerInterceptor} from './errorhandler.interceptor';

export function interceptableFactory(backend: XHRBackend,
                                     defaultOptions: RequestOptions,
                                     injector: Injector) {
  return new InterceptableHttp(
    backend,
    defaultOptions,
    [
      new PendingRequestInterceptor(injector),
      new AuthExpiredInterceptor(injector),
      new ErrorHandlerInterceptor(injector)
    ]
  );
}

export function customHttpProvider() {
  return {
    provide: Http,
    useFactory: interceptableFactory,
    deps: [
      XHRBackend,
      RequestOptions,
      Injector
    ]
  };
}
