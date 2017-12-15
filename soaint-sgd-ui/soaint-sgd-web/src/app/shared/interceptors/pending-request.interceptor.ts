import {Observable} from 'rxjs/Observable';
import {RequestOptionsArgs, Response} from '@angular/http';
import {HttpInterceptor} from './http.interceptor';
import {Injector} from '@angular/core';
import {LoadingService} from '../../infrastructure/utils/loading.service';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

export class PendingRequestInterceptor extends HttpInterceptor {

  pendingRequests = 0;
  filteredUrlPatterns: RegExp[] = [];
  loadingService: LoadingService;

  constructor(private injector: Injector) {
    super();
    this.loadingService = this.injector.get(LoadingService)
  }

  private shouldBypass(url: string): boolean {
    return this.filteredUrlPatterns.some(e => {
      return e.test(url);
    });
  }

  requestIntercept(url, options?: RequestOptionsArgs): RequestOptionsArgs {
    const shouldBypass = this.shouldBypass(url.url || url);
    if (!shouldBypass) {
      this.pendingRequests++;
      if (1 === this.pendingRequests) {
        this.loadingService.presentLoading();
      }
    }
    return options;
  }

  responseIntercept(url, observable: Observable<Response>): Observable<Response> {
    return observable.map((response: Response) => {
      const shouldBypass = this.shouldBypass(url.url || url);
      if (!shouldBypass) {
        this.pendingRequests--;
        if (0 === this.pendingRequests) {
          this.loadingService.dismissLoading();
        }
      }
      return response;
    }).catch(error => {
        const shouldBypass = this.shouldBypass(url.url || url);
        if (!shouldBypass) {
          this.pendingRequests = 0;
          this.loadingService.dismissLoading();
        }
        return Observable.throw(error);
      }
    )
  }
}
