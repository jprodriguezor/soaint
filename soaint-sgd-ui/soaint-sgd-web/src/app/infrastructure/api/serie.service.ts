import { Injectable } from '@angular/core';
import {SerieSubserieApiService} from "./serie-subserie.api";

@Injectable()
export class SerieService {

  constructor(private serieSubserieService :SerieSubserieApiService) {  }

  getSeriePorDependencia(codDependencia){

    return this
      .serieSubserieService
      .ListarSerieSubserie({idOrgOfc:codDependencia})
      .map(response =>  response[0].listaSerie);
  }

  getSubseriePorDependenciaSerie(codDependencia,codSerie){

    return this
      .serieSubserieService
      .ListarSerieSubserie({idOrgOfc:codDependencia,codSerie:codSerie})
      .map(response => response[0].listaSubSerie);
  }



}
