import { Injectable } from '@angular/core';
import {ApiBase} from './api-base';
import {ComunicacionOficialDTO} from '../../domain/comunicacionOficialDTO';
import {environment} from '../../../environments/environment';
import { CacheResponse } from '../../shared/cache-response';
import {Observable} from 'rxjs/Observable';
import { DependenciaDTO } from '../../domain/dependenciaDTO';
import { ConstanteDTO } from '../../domain/constanteDTO';

@Injectable()
export class SedeApiService extends CacheResponse {

  constructor(private _api: ApiBase) {
    super();
   }


}
