import {RequestOptionsArgs, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import {HttpInterceptor} from './http.interceptor';
import {Injector} from '@angular/core';
import {ErrorHandlerService} from '../../infrastructure/utils/error-handler.service';

export class ErrorHandlerInterceptor extends HttpInterceptor {

  errorHandlerService: ErrorHandlerService;

  constructor(private injector: Injector) {
    super();
    this.errorHandlerService = this.injector.get(ErrorHandlerService)
  }

  requestIntercept(url, options?: RequestOptionsArgs): RequestOptionsArgs {
    return options;
  }

  responseIntercept(url, observable: Observable<Response>): Observable<Response> {
    return <Observable<Response>> observable.catch((error) => {
      if (!(error.status === 401 && (error.text() === '' ||
          (error.json().path && error.json().path.indexOf('/api/account') === 0 )))) {
        this.errorHandlerService.handleError(error);
      }
      return Observable.throw(error);
    });
  }
}
