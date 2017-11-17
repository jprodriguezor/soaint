import {Injector} from '@angular/core';
import {RequestOptionsArgs, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import {HttpInterceptor} from './http.interceptor';
import {ErrorHandlerService} from '../../infrastructure/utils/error-handler.service';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

export class AuthExpiredInterceptor extends HttpInterceptor {

  errorHandlerService: ErrorHandlerService;

  constructor(private injector: Injector) {
    super();
    this.errorHandlerService = this.injector.get(ErrorHandlerService)
  }

  requestIntercept(url, options?: RequestOptionsArgs): RequestOptionsArgs {
    return options;
  }

  responseIntercept(url, observable: Observable<Response>): Observable<Response> {
    return <Observable<Response>> observable.catch((error, source) => {
      if (error.status === 401) {
        return this.errorHandlerService.handleAuthExpiredError(error);
      }
      return Observable.throw(error);
    });
  }
}
