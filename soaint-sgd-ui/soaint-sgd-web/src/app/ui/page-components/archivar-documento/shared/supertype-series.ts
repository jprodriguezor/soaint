import {OnInit} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {DependenciaDTO} from "../../../../domain/dependenciaDTO";
import {SerieDTO} from "../../../../domain/serieDTO";
import {State as RootState} from "../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {SerieService} from "../../../../infrastructure/api/serie.service";
import {getSelectedDependencyGroupFuncionario} from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {Subscription} from "rxjs/Subscription";

export abstract class SupertypeSeries implements OnInit{

  dependenciaSelected$ : Observable<any>;

  dependenciaSelected : DependenciaDTO;

  subseriesObservable$:Observable<any[]>;

  seriesObservable$:Observable<SerieDTO[]>;

  globalDependencySubscriptor:Subscription;




  constructor(protected _store:Store<RootState>, protected _serieSubserieService:SerieService){

        this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
  }

  ngOnInit(){

    this.globalDependencySubscriptor =  this.dependenciaSelected$.subscribe(result =>{
      this.seriesObservable$ = this
        ._serieSubserieService
        .getSeriePorDependencia(result.codigo)
        .map(list => {list.unshift({
          codigoSerie:null,nombreSerie:"Seleccione"});
          return list});
      this.dependenciaSelected = result;
    });
  }

  selectSerie(evt)
  {

    this.subseriesObservable$ = evt ?
      this
        ._serieSubserieService
        .getSubseriePorDependenciaSerie(this.dependenciaSelected.codigo,evt.value)
        .map(list => {
          list.unshift({codigoSubSerie:null,nombreSubSerie:"Seleccione"});
          return list;
        })
      : Observable.empty();
  }
}
