import {Injectable} from '@angular/core';
import {Headers, Http, Request, Response, RequestOptionsArgs, RequestMethod, RequestOptions} from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/first';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {RequestArgs} from '@angular/http/src/interfaces';


@Injectable()
export class HttpHandler {

  private token$: Observable<string>;

  constructor(private _http: Http, private _store: Store<State>) {
    this.token$ = _store.select(s => s.auth.token);
  }

  requestHelper(url: string | RequestArgs, options?: RequestOptionsArgs): Observable<Response> {

    return this.token$.take(1).switchMap(token => {
      // console.log('Calling protected URL ...', token);

      options = options || new RequestOptions();
      options.headers = options.headers || new Headers();
      if (token !== null) {

        if (!options.headers.has('Content-Type')) {
          options.headers.append('Content-Type', 'application/json');
        }
        if (!options.headers.has('Authorization')) {
          options.headers.append('Authorization', 'Bearer ' + token);
        }
      } else {
        options.headers.append('Content-Type', 'application/json');
      }
      if (options.body && typeof options.body !== 'string') {
        options.body = JSON.stringify(options.body);
      }
      let request$ = null;
      if (typeof url === 'string') {
        const req: string = <string>url;
        request$ = this._http.request(req, options);
      } else {
        const req: Request = new Request(<RequestArgs>url);
        req.headers = options.headers;
        request$ = this._http.request(req, options);
      }
      return this.handleResponse(request$, token);
    });

  }

  uploadHelper(url: string | RequestArgs, options?: RequestOptionsArgs): Observable<Response> {
    return this.token$.take(1).switchMap(token => {
      options = options || new RequestOptions();
      options.headers = new Headers();
      if (token !== null) {
        // options.headers.append('Content-Type', 'multipart/form-data');
        options.headers.append('Accept', 'application/json');
        options.headers.append('Authorization', 'Bearer ' + token);
      } else {
        options.headers.append('Accept', 'application/json');
        // options.headers.append('Content-Type', 'multipart/form-data');
      }
      const req: Request = new Request(<RequestArgs>url);
      req.headers = options.headers;
      const request$ = this._http.request(req, options);
      return this.handleResponse(request$, token);
    });
  }

  handleResponse(request$: Observable<Response>, token): Observable<Response> {
    return request$.map((res: Response) => {
      if ('application/json' === res.headers.get('Content-Type')) {
        return res.json();
      }
      return res;
    });
  }

  public get(url: string, params: any, options?: RequestOptionsArgs): Observable<Response> {

    return this.requestHelper({url: url, params: params, method: RequestMethod.Get}, options);
  }

  public post(url: string, body: any, options?: RequestOptionsArgs): Observable<Response> {
    return this.requestHelper({url: url, body: body, method: RequestMethod.Post}, options);
  }

  public put(url: string, body: any, options ?: RequestOptionsArgs): Observable<Response> {
    return this.requestHelper({url: url, body: body, method: RequestMethod.Put}, options);
  }

  public delete(url: string, options ?: RequestOptionsArgs): Observable<Response> {
    return this.requestHelper({url: url, method: RequestMethod.Delete}, options);
  }

  public patch(url: string, body: any, options?: RequestOptionsArgs): Observable<Response> {
    return this.requestHelper({url: url, body: body, method: RequestMethod.Patch}, options);
  }

  public putFile(url: string, body: any, options ?: RequestOptionsArgs): Observable<Response> {
    return this.uploadHelper({url: url, body: body, method: RequestMethod.Post}, options);
  }


}
