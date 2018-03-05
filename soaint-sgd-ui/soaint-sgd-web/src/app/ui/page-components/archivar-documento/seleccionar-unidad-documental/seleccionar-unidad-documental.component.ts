import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../../shared/validation-messages';
import {ConfirmationService} from "primeng/primeng";
import {Observable} from "rxjs/Observable";
import {State as RootState} from "../../../../infrastructure/redux-store/redux-reducers";
import {ApiBase} from "../../../../infrastructure/api/api-base";
import {Store} from "@ngrx/store";
import {environment} from "../../../../../environments/environment";
import {SerieSubserieApiService} from "../../../../infrastructure/api/serie-subserie.api";
import {SerieService} from "../../../../infrastructure/api/serie.service";

import {Sandbox as DependenciaSandbox} from "../../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox" ;

import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {DependenciaDTO} from "../../../../domain/dependenciaDTO";
import {SelectDependencyGroupAction}  from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-actions";
import {Subscription} from "rxjs/Subscription";


@Component({
  selector: 'app-seleccionar-unidad-documental',
  templateUrl: './seleccionar-unidad-documental.component.html',
  styleUrls: ['./seleccionar-unidad-documental.component.scss'],
  providers:[ConfirmationService]
})
export class SeleccionarUnidadDocumentalComponent implements OnInit, OnDestroy {

  form: FormGroup;

  series: Array<any> = [];

  operation:string = "bUnidadDocumental";

  seriesObservable$:Observable<any[]>;

  globalDependencySubscriptor:Subscription;

  subseries: Array<any> = [];

  subseriesObservable$:Observable<any[]>;

  dependenciaSelected$ : Observable<any>;

  dependenciaSelected : DependenciaDTO;

  documentos: any[];

  validations: any = {};

  visiblePopup:boolean = false;

  currentPage:number = 1;

   constructor(private formBuilder: FormBuilder,private confirmationService:ConfirmationService, private serieSubSerieService:SerieService,private _dependenciaSanbox:DependenciaSandbox,private _store:Store<RootState>) {

    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);

     this.initForm();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'serie': [null, Validators.required],
      'subserie': [null, Validators.required],
      'identificador': [null, Validators.required],
      'nombre': [null, Validators.required],
      'descriptor1': [null],
      'descriptor2': [null],
      'observaciones': [null,Validators.required],
     // 'operation'  :["bUnidadDocumental"],
    });
  }

  ngOnDestroy(): void {

     this.globalDependencySubscriptor.unsubscribe();
  }

  ngOnInit(): void {

   this.globalDependencySubscriptor =  this.dependenciaSelected$.subscribe(result =>{
        this.seriesObservable$ = this
          .serieSubSerieService
          .getSeriePorDependencia(result.codigo)
         // .map(series =>  series.map(serie => { return {label:serie.nombreSerie,value:serie.codigoSerie}})) ;
    });

  }

  listenForErrors() {
    this.bindToValidationErrorsOf('tipoComunicacion');
    this.bindToValidationErrorsOf('tipologiaDocumental');
    this.bindToValidationErrorsOf('numeroFolio');
    this.bindToValidationErrorsOf('asunto');
    this.bindToValidationErrorsOf('tipoAnexosDescripcion');
    this.bindToValidationErrorsOf('empresaMensajeria');
    this.bindToValidationErrorsOf('numeroGuia');
  }

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);
    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    }
  }

  bindToValidationErrorsOf(control: string) {
    const ac = this.form.get(control);
    ac.valueChanges.subscribe(() => {
      if ((ac.touched || ac.dirty) && ac.errors) {
        const error_keys = Object.keys(ac.errors);
        const last_error_key = error_keys[error_keys.length - 1];
        this.validations[control] = VALIDATION_MESSAGES[last_error_key];
      } else {
        delete this.validations[control];
      }
    });
  }

  currentSection():string{
   return this.operation;
   }

   buscarUnidadDocumental(){

     this.visiblePopup = true;
   }

   next(){

     this.currentPage++;
   }

   prev(){

     this.currentPage --;
   }

   closePopup(){
     this.visiblePopup = false;
     this.form.controls['operation'].setValue("solicitarUnidadDocumental");
   }
   confirmArchivarDocumentos(){

     this.confirmationService.confirm({
       message: '¿Está seguro de archivar el documento en la carpeta XXXXX?',
       header: 'Confirmacion',
       icon: 'fa fa-question-circle',
       accept: () => {


       },
       reject: () => {

       }
     });
   }

   private clearValue(control:string){

     const ac = this.form.get(control);

     ac.setValue(null);
   }

   clearFilters(){

     this.clearValue("serie");
     this.clearValue("subserie");
     this.clearValue("identificador");
     this.clearValue("nombre");
     this.clearValue("descriptor1");
     this.clearValue("descriptor2");
     this.clearValue("observaciones");
   }


}

