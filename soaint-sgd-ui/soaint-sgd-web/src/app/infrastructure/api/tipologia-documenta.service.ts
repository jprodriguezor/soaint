import { Injectable } from '@angular/core';
import {ApiBase} from "./api-base";
import {Observable} from "rxjs/Observable";
import {oa_dataSource} from "../../ui/page-components/organizacion-archivo/data-source";

@Injectable()
export class TipologiaDocumentaService {

  constructor(private _api:ApiBase) {}

  getTiplogiasDocumentales():Observable<any>{

     return Observable.of(oa_dataSource.tipologiasDocumentales);
  }

}
