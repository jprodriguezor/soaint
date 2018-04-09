import {ObjectHelper} from './object-extends';
import {Observable} from 'rxjs/Observable';

export  abstract class CacheResponse {

 protected   payloadsCached: {payload: any, response: any, endpoint?: string}[] = [];


  isCached(payload: any, endpoint?: string): any {

    return this.payloadsCached.find(p =>  ObjectHelper.similar(p.payload, payload) && ( p.endpoint === null || p.endpoint === endpoint));
  }

  protected getResponse(payload, defaultResponse = Observable.empty(), endpoint?: string): Observable<any> {

    const payloadCached = this.isCached(payload, endpoint);

    return payloadCached === undefined ? defaultResponse : Observable.of(payloadCached.response);
  }

  protected  cacheResponse(payload, response, endpoint?) {

      this.payloadsCached.push({payload: payload, response: response, endpoint: endpoint});
  }


}
