import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import { ListadoUnidadDocumentalModel } from 'app/ui/page-components/unidades-documentales/models/listado.unidad.documental.model';
import { DetalleUnidadDocumentalDTO } from 'app/ui/page-components/unidades-documentales/models/DetalleUnidadDocumentalDTO';

@Injectable()
export class UnidadDocumentalApiService {

  constructor(private _api: ApiBase) {
  }

  Listar(payload: any) {
    // return this._api.post(environment.listar_unidad_documental, payload).map(response => response);
    return Observable.empty<ListadoUnidadDocumentalModel[]>();
  }

  GetDetalleUnidadDocumental(payload: any) {
      // return this._api.post(environment.listar_unidad_documental, payload).map(response => response);
    return null;
   }

}
