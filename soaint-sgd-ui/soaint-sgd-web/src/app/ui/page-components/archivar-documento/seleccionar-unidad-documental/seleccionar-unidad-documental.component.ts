import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../../shared/validation-messages';
import {ConfirmationService, Dropdown} from "primeng/primeng";
import {Observable} from "rxjs/Observable";
import {State as RootState} from "../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {SerieService} from "../../../../infrastructure/api/serie.service";

import {Sandbox as DependenciaSandbox} from "../../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox" ;

import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {DependenciaDTO} from "../../../../domain/dependenciaDTO";
import {SelectDependencyGroupAction}  from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-actions";
import {Subscription} from "rxjs/Subscription";
import {ROUTES_PATH} from "../../../../app.route-names";
import {go} from "@ngrx/router-store";
import {SolicitudCreacionUDModel} from "./SolicitudCreacionUD";


@Component({
  selector: 'app-seleccionar-unidad-documental',
  templateUrl: './seleccionar-unidad-documental.component.html',
  styleUrls: ['./seleccionar-unidad-documental.component.scss'],
  providers:[ConfirmationService]
})
export class SeleccionarUnidadDocumentalComponent implements OnInit, OnDestroy {

  form: FormGroup;

  solicitudes: SolicitudCreacionUDModel[] = [];

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
          .map(list => {list.unshift({
            codigoSerie:null,nombreSerie:"Seleccione"});
          return list});
        this.dependenciaSelected = result.codigo;
    });
  }

  addSolicitud(){

     if(this.form.valid){

        this.solicitudes.push({
          codSerie: this.getControlValue("serie"),
          codSubserie : this.getControlValue("subserie"),
          descriptor1 : this.getControlValue("descriptor1"),
          descriptor2 : this.getControlValue("descriptor2"),
          identificadorUD : this.getControlValue("identificador"),
          nombreUD : this.getControlValue("nombre"),
          observaciones : this.getControlValue("observaciones"),
          estado: ""
        });


     }
  }

  private getControlValue(identificador:string):string{

     return this.form.controls[identificador].value.toString();
  }

  crearSolicitudCuD(){

     if(this.form.valid){

       // this._taskSandBox.completeTaskDispatch({
       //   idProceso: this.task.idProceso,
       //   idDespliegue: this.task.idDespliegue,
       //   idTarea: this.task.idTarea,
       //   parametros: {
       //     devolucion: 2,
       //     requiereDigitalizacion: this.comunicacion.correspondencia.reqDigita,
       //   }
       // });
       // this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
     }

  };

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

     console.log(this.form.controls[control].value);
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

   selectSerie(evt){

     this.subseriesObservable$ = evt ?
       this
         .serieSubSerieService
         .getSubseriePorDependenciaSerie(this.dependenciaSelected,evt.value)
         .map(list => {
           list.unshift({codigoSubSerie:null,nombreSubSerie:"Seleccione"});
           return list;
         })
         : Observable.empty();


   }


}

0
