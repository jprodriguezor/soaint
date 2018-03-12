import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SupertypeSeries} from "../../shared/supertype-series";
import  {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {SerieService} from "../../../../../infrastructure/api/serie.service";
import {Observable} from "rxjs/Observable";


@Component({
  selector: 'app-form-crear-unidad-documental',
  templateUrl: './form-crear-unidad-documental.component.html',
})
export class FormCrearUnidadDocumentalComponent extends SupertypeSeries{

  form:FormGroup;

  filterUDForm:FormGroup;

  formAsignarUT:FormGroup;

  subserieObservable2$:Observable<any[]>;


  constructor(private fb:FormBuilder,store:Store<RootState>,serieService:SerieService) {

    super(store,serieService);

    this.formCrearUDInit();

    this.formFiltrarUDInit();

    this.formAsignarUTInit();

  }

  private formCrearUDInit(){

    this.form = this.fb.group({
      serie:[null,Validators.required],
      subserie:[null,Validators.required],
      identificador:[null,Validators.required],
      nombre:[null,Validators.required],
      descriptor1:[null],
      descriptor2:[null],
      observaciones:[null],
      creada:[1],
    });
  }

  private formFiltrarUDInit(){

    this.filterUDForm = this.fb.group({
      serie:[null],
      subserie:[null],
      identificador:[null],
      nombre:[null],
      descriptor1:[null],
      descriptor2:[null],
    });
  }

  private formAsignarUTInit(){

    this.formAsignarUT = this.fb.group({
        bodega:[null,Validators.required],
        piso:[null,Validators.required],
        caja:[null,Validators.required],
        carpeta:[null,Validators.required],
      });
    }

  selectSerie2(evt){

      this.subserieObservable2$ = evt ?
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
