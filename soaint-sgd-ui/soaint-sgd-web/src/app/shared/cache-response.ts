import {ObjectHelper} from "./object-extends";
import {Observable} from "rxjs/Observable";

export  abstract class CacheResponse{

 protected   payloadsCached:{payload:any,response:any}[] = [];

  isCached(payload:any):any{

    return this.payloadsCached.find(p =>  ObjectHelper.similar(p.payload,payload));
  }

  protected getResponse(payload,defaultResponse = Observable.empty()):Observable<any>{

    const payloadCached = this.isCached(payload);

    return payloadCached === undefined? defaultResponse : Observable.of(payloadCached.response);
  }


}
