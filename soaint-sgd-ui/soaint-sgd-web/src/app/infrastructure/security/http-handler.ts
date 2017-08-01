import {Injectable} from '@angular/core';
import {Headers, Http, Request, Response, RequestOptionsArgs, RequestMethod, RequestOptions} from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/first';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {RequestArgs} from '@angular/http/src/interfaces';
import {LogoutAction} from 'app/ui/page-components/login/redux-state/login-actions';
import {PushNotificationAction} from '../../ui/layout-components/container/growl-message/redux-state/actions';

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
      let request$ = null;
      if (typeof url === 'string') {
        const req: string = <string>url;
        request$ = this._http.request(req, options);
      } else {
        const req: Request = new Request(<RequestArgs>url);
        req.headers = options.headers;
        request$ = this._http.request(req, options);
      }

      return request$.map((res: Response) => res.json()).catch(res => {

        this._store.dispatch(new PushNotificationAction({
          severity: 'error',
          summary: 'Error Inesperado del sistema',
          detail: 'Ha ocurrido un error al intentar conectarse',
          action: ''
        }));

        console.log('sdfgsdfg');

        if (res.status === 401 && token !== null) {
          this._store.dispatch(new LogoutAction());
        } else if (res.status !== 500) {
          return Observable.create(observer => observer.error(res));
        } else {
          return Observable.create(observer => observer.error(res.statusText));
        }
      });
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

}
