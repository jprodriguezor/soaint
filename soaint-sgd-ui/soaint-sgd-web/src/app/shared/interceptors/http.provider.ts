import {Http, RequestOptions, XHRBackend} from '@angular/http';

import {PendingRequestInterceptor} from './pending-request.interceptor';
import {InterceptableHttp} from './interceptale-http';
import {Injector} from '@angular/core';
import {AuthExpiredInterceptor} from './auth-expired.interceptor';
import {ErrorHandlerInterceptor} from './errorhandler.interceptor';

export function interceptableFactory(backend: XHRBackend,
                                     defaultOptions: RequestOptions,
                                     injector: Injector) {
  const interceptable = new InterceptableHttp(
    backend,
    defaultOptions
  );
  const interceptors = [
    new PendingRequestInterceptor(injector),
    new AuthExpiredInterceptor(injector),
    new ErrorHandlerInterceptor(injector)
  ];

  interceptable.setInterceptors(interceptors);

  return interceptable;
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
