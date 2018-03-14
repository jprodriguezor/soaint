import {Observable} from 'rxjs/Observable';
import {RequestOptionsArgs, Response, Request} from '@angular/http';
import {Injectable} from '@angular/core';

/**
 * A HTTP interceptor responsibility chain member is a class, which may react on request and response of all requests
 * done by HTTP.
 */
export abstract class HttpInterceptor {
  private _successor: HttpInterceptor = null;

  set successor(successor: HttpInterceptor) {
    this._successor = successor;
  }

  processRequestInterception(url, options?: RequestOptionsArgs): RequestOptionsArgs {

    return (!this._successor) ? this.requestIntercept(url, options) :
      this._successor.processRequestInterception(this.requestIntercept(url,options));
  }

  processResponseInterception(url: string | Request, response: Observable<Response>): Observable<Response> {
    return (!this._successor) ? this.responseIntercept(url, response) :
      this._successor.processResponseInterception(url, this.responseIntercept(url, response));
  }

  abstract requestIntercept(url, options?: RequestOptionsArgs): RequestOptionsArgs;

  abstract responseIntercept(url, observable: Observable<Response>): Observable<Response>;

}
