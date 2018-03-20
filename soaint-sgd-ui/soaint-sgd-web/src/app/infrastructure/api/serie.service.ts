import { Injectable } from '@angular/core';
import {SerieSubserieApiService} from './serie-subserie.api';
import { Observable } from 'rxjs/Observable';
import { SerieDTO } from 'app/domain/serieDTO';
import { SubserieDTO } from 'app/domain/subserieDTO';

@Injectable()
export class SerieService {

  constructor(private serieSubserieService: SerieSubserieApiService) {  }

  getSeriePorDependencia(codDependencia): Observable<SerieDTO[]> {

    return this
      .serieSubserieService
      .ListarSerieSubserie({idOrgOfc: codDependencia})
      .map(response =>  response.listaSerie);
  }

  getSubseriePorDependenciaSerie(codDependencia, codSerie): Observable<SubserieDTO[]> {

    return this
      .serieSubserieService
      .ListarSerieSubserie({idOrgOfc: codDependencia, codSerie: codSerie})
      .map(response => response.listaSubSerie);
  }



}

