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
import {SolicitudCreacionUDDto} from "../../../../domain/solicitudCreacionUDDto";
import {SolicitudCreacionUdService} from "../../../../infrastructure/api/solicitud-creacion-ud.service";
import {SerieDTO} from "../../../../domain/serieDTO";
import {UnidadDocumentalDTO} from "../../../../domain/unidadDocumentalDTO";
import {afterTaskComplete} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers";
import {go} from "@ngrx/router-store";
import {ROUTES_PATH} from "../../../../app.route-names";
import {Guid} from "../../../../infrastructure/utils/guid-generator";
import {UnidadDocumentalApiService} from "../../../../infrastructure/api/unidad-documental.api";


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

  seriesObservable$:Observable<SerieDTO[]>;

  unidadesDocumentales$:Observable<UnidadDocumentalDTO[]>;

  unidadesDocumentales:UnidadDocumentalDTO[];

  unidadDocumentalSelected:UnidadDocumentalDTO;

  documentos$:Observable<any[]>;

  globalDependencySubscriptor:Subscription;

  afterTaskCompleteSubscriptor:Subscription;

  subseries: Array<any> = [];

  solicitudes:SolicitudCreacionUDDto[] = [];

  subseriesObservable$:Observable<any[]>;

  dependenciaSelected$ : Observable<any>;

  dependenciaSelected : DependenciaDTO;

  documentos: any[];

  validations: any = {};

  visiblePopup:boolean = false;

  currentPage:number = 1;

   constructor(
     private formBuilder: FormBuilder
     ,private confirmationService:ConfirmationService
     , private serieSubSerieService:SerieService
     ,private _store:Store<RootState>
     ,private _solicitudUDService:SolicitudCreacionUdService
     ,private _udService:UnidadDocumentalApiService) {

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

     this.afterTaskCompleteSubscriptor.unsubscribe();
  }

  ngOnInit(): void {

   this.globalDependencySubscriptor =  this.dependenciaSelected$.subscribe(result =>{
        this.seriesObservable$ = this
          .serieSubSerieService
          .getSeriePorDependencia(result.codigo)
          .map(list => {list.unshift({
            codigoSerie:null,nombreSerie:"Seleccione"});
          return list});
        this.dependenciaSelected = result;
    });

   this.afterTaskCompleteSubscriptor =  afterTaskComplete.subscribe( t => this._store.dispatch(go(['/' + ROUTES_PATH.workspace])));
  }

  addSolicitud(){

     if(this.form.valid) {

       let nro = this.solicitudes.length;

       this.solicitudes.push({
         codSerie: this.getControlValue("serie"),
         codSubserie: this.getControlValue("subserie"),
         descriptor1: this.getControlValue("descriptor1"),
         descriptor2: this.getControlValue("descriptor2"),
         identificadorUD: this.getControlValue("identificador"),
         nombreUD: this.getControlValue("nombre"),
         observaciones: this.getControlValue("observaciones"),
         fechaHora: new Date().getTime(),
         nro: nro,
         estado: "",
       });
     }


  }

  private getControlValue(identificador:string):string{

     return this.form.controls[identificador].value.toString();
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

         //this._udService.archivarDocumento({});

       },
       reject: () => {

       }
     });
   }


   finalizarTarea(){

     switch (this.operation){
       case 'bUnidadDocumental' :

          this.archivarDocumento();
         break;

       case 'solicitarUnidadDocumental' :

         this.crearSolicitudCreacionUD();
         break;
     }
   }

  selectSerie(evt)
  {

    this.subseriesObservable$ = evt ?
      this
        .serieSubSerieService
        .getSubseriePorDependenciaSerie(this.dependenciaSelected.codigo,evt.value)
        .map(list => {
          list.unshift({codigoSubSerie:null,nombreSubSerie:"Seleccione"});
          return list;
        })
      : Observable.empty();
  }

   private crearSolicitudCreacionUD(){

     if(this.solicitudes.length > 0){

         this._solicitudUDService.solicitarUnidadDocumental(this.solicitudes);
     }
   }

    private archivarDocumento(){

      this.visiblePopup = true;

    }

    buscarUnidadDocumental(){

    //  this.visiblePopup = true;

      this.unidadesDocumentales$ = this._udService.Listar({
        codSerie:this.form.get('serie').value,
        codSubserie: this.form.get('subserie').value,
        identificador: this.form.get('identificador').value,
        nombre:this.form.get('nombre').value
      });
    }


}

