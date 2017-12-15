import {HttpInterceptor} from './http.interceptor';
import {Injectable} from '@angular/core';
import {ConnectionBackend, Headers, Http, Request, RequestOptions, RequestOptionsArgs, Response} from '@angular/http';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class InterceptableHttp extends Http {
  private firstInterceptor: HttpInterceptor;

  private interceptors: HttpInterceptor[];

  constructor(backend: ConnectionBackend,
              defaultOptions: RequestOptions) {
    super(backend, defaultOptions);
  }

  setInterceptors(interceptors: HttpInterceptor[]) {
    this.interceptors = interceptors;
    /**
     * building a responsibility chain of http interceptors, so when processXXXInterception is called on first interceptor,
     * all http interceptors are called in a row
     * Note: the array of interceptors are wired in customHttpProvider of the generated Jhipster app in file `http.provider.ts`
     *
     */
    if (this.interceptors && this.interceptors.length > 0) {
      this.interceptors.reduce((chain, current) => {
        chain.successor = current;
        return current;
      });

      this.firstInterceptor = this.interceptors[0];
    }
  }

  request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {
    return this.intercept(url, super.request(url, this.getRequestOptionArgs(url, options)));
  }

  get(url: string, options?: RequestOptionsArgs): Observable<Response> {
    return super.get(url, this.getRequestOptionArgs(url, options));
  }

  post(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
    return super.post(url, body, this.getRequestOptionArgs(url, options));
  }

  put(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
    return super.put(url, body, this.getRequestOptionArgs(url, options));
  }

  delete(url: string, options?: RequestOptionsArgs): Observable<Response> {
    return super.delete(url, this.getRequestOptionArgs(url, options));
  }

  getRequestOptionArgs(url, options?: RequestOptionsArgs): RequestOptionsArgs {
    if (!options) {
      options = new RequestOptions();
    }
    if (!options.headers) {
      options.headers = new Headers();
    }

    return !this.firstInterceptor ? options : this.firstInterceptor.processRequestInterception(url, options);
  }

  intercept(url: string | Request, observable: Observable<Response>): Observable<Response> {
    return !this.firstInterceptor ? observable : this.firstInterceptor.processResponseInterception(url, observable);
  }
}
