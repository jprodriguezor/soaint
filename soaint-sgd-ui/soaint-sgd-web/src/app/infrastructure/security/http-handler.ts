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

    return this.token$.first().switchMap(token => {
      console.log('Calling protected URL ...', token);

      options = options || new RequestOptions();
      options.headers = new Headers();
      if (token !== null) {
        options.headers.append('Content-Type', 'application/json');
        options.headers.append('Authorization', 'Bearer ' + token);
      } else {
        options.headers.append('Content-Type', 'application/json');
      }
      if (options.body && typeof options.body !== 'string') {
        options.body = JSON.stringify(options.body);
      }

      if (typeof url === 'string') {
        const req: string = <string>url;
        return this._http.request(req, options).map((res: Response) => res.json());
      } else {
        const req: Request = new Request(<RequestArgs>url);
        req.headers = options.headers;
        return this._http.request(req, options).map((res: Response) => res.json());
      }
    });

  }

  public get(url: string, options?: RequestOptionsArgs): Observable<Response> {
    return this.requestHelper({url: url, method: RequestMethod.Get}, options);
  }

  public post(url: string, body: any, options?: RequestOptionsArgs): Observable<Response> {
    return this.requestHelper({url: url, body: body, method: RequestMethod.Post}, options);
    // console.log('dsgsg');
    // return this._http.post(url, body);
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

}
