import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import { DetalleUnidadDocumentalDTO } from 'app/ui/page-components/unidades-documentales/models/DetalleUnidadDocumentalDTO';
import {UnidadDocumentalDTO} from '../../domain/unidadDocumentalDTO';
import { MensajeRespuestaDTO } from '../../domain/MensajeRespuestaDTO';
import { AnexoFullDTO } from '../../domain/anexoFullDTO';

@Injectable()
export class CorrespondenciaApiService {

  constructor(private _api: ApiBase) {
  }

  // ListarAnexos(nroRadicado: string): Observable<AnexoFullDTO[]> {
  //   return this._api.list(environment.listarAnexos_endpoint + nroRadicado)
  //   .map((resp) => resp);
  // }
}