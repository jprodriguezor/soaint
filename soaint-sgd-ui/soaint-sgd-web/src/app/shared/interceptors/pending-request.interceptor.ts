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

  private shouldBypass(request: any): boolean {

    //console.log(this.requestQueuee.);

    // return this.filteredUrlPatterns.some(e => {
    //   return e.test(url);
    // });

    return this.requestQueuee.some(e => {

      return ObjectHelper.similar(request,e);
    });
  }

  requestIntercept(url, options?: RequestOptionsArgs): RequestOptionsArgs {

      let r = (typeof url === 'object') ? url : Object.assign({url:url},options);

    // const shouldBypass = this.shouldBypass(url.url || url);

     const shouldBypass = this.shouldBypass(r);

    if (!shouldBypass) {

     this.requestQueuee.push(r);

     if(this.requestQueuee.length == 1){
       this.loadingService.presentLoading();
     }

      /*this.pendingRequests++;
      if (1 === this.pendingRequests) {
        this.loadingService.presentLoading();
      }*/
    }
    return options;
  }

  responseIntercept(url, observable: Observable<Response>): Observable<Response> {

    let r = typeof  url == 'object' ? url :{url:url};

    return observable.map((response: Response) => {


      const index = this.requestQueuee.indexOf(r);

     this.requestQueuee.splice(index,1);

         if(this.requestQueuee.length == 0){
        this.loadingService.dismissLoading();
      }

      /*if (!shouldBypass) {
        this.pendingRequests--;
        if (0 === this.pendingRequests) {
          this.loadingService.dismissLoading();
        }
      }*/
      return response;
    }).catch(error => {  console.log(error);

        this.requestQueuee = [];

      this.loadingService.dismissLoading();

        // const shouldBypass = this.shouldBypass(url.url || url);
        // if (!shouldBypass) {
        //   this.pendingRequests = 0;
        //   this.loadingService.dismissLoading();
        // }
        return Observable.throw(error);
      }
    )
  }
}
