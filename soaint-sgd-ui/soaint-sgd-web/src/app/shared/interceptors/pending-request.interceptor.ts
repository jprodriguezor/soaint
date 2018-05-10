import {Observable} from 'rxjs/Observable';
import {RequestOptionsArgs, Response} from '@angular/http';
import {HttpInterceptor} from './http.interceptor';
import {Injector} from '@angular/core';
import {LoadingService} from '../../infrastructure/utils/loading.service';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';
import {ObjectHelper} from "../object-extends";
import * as lodash from 'lodash';

export class
PendingRequestInterceptor extends HttpInterceptor {

  pendingRequests = 0;
  filteredUrlPatterns: RegExp[] = [];
  loadingService: LoadingService;
  requestQueuee : any[] = [];



  constructor(private injector: Injector) {
    super();
    this.loadingService = this.injector.get(LoadingService)
  }

  private extractFromRequest(request):any{

    return {
      url:request.url,
      method:request.method,
      _body:request._body
    }
  }

  private shouldBypass(request: any): boolean {

    const rq = this.extractFromRequest(request);

     return this.requestQueuee.some(e => {
        return ObjectHelper.similar(rq,e)

    });
  }

  requestIntercept(url, options?: RequestOptionsArgs): RequestOptionsArgs {

      let r = (typeof url === 'object') ? url : Object.assign({url:url},options);

      const shouldBypass = this.shouldBypass(r);

    if (!shouldBypass) {

      this.requestQueuee.push(this.extractFromRequest(r));

     if(this.requestQueuee.length == 1){
       this.loadingService.presentLoading();
     }
    }
    return options;
  }

  responseIntercept(url, observable: Observable<Response>): Observable<Response> {

    let r = typeof  url == 'object' ? this.extractFromRequest(url) :{url:url};

    return observable.map((response: Response) => {

      const index = this.requestQueuee.findIndex( request => ObjectHelper.similar(r,request));

      this.requestQueuee.splice(index,1);

      console.log("peticiones count",this.requestQueuee.length)

       if(this.requestQueuee.length == 0){
        this.loadingService.dismissLoading();
      }
      return response;
    }).catch(error => {

        this.requestQueuee = [];

        this.loadingService.dismissLoading();

        return Observable.throw(error);
      }
    )
  }
}
