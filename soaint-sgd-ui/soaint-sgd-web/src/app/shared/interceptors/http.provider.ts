import {Http, RequestOptions, XHRBackend} from '@angular/http';

import {PendingRequestInterceptor} from './pending-request.interceptor';
import {InterceptableHttp} from './interceptale-http';
import {Injector} from '@angular/core';

export function interceptableFactory(backend: XHRBackend,
                                     defaultOptions: RequestOptions,
                                     injector: Injector) {
  return new InterceptableHttp(
    backend,
    defaultOptions,
    [
      new PendingRequestInterceptor()
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
};
