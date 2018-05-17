import {ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../../../../shared/validation-messages';
import {Observable} from "rxjs/Observable";
import {State as RootState} from "../../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {SerieService} from "../../../../../../infrastructure/api/serie.service";


import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {DependenciaDTO} from "../../../../../../domain/dependenciaDTO";
import {Subscription} from "rxjs/Subscription";
import {SolicitudCreacionUdService} from "../../../../../../infrastructure/api/solicitud-creacion-ud.service";
import {SerieDTO} from "../../../../../../domain/serieDTO";
import {UnidadDocumentalDTO} from "../../../../../../domain/unidadDocumentalDTO";
import {UnidadDocumentalApiService} from "../../../../../../infrastructure/api/unidad-documental.api";
import {ArchivarDocumentoModel} from "../../models/archivar-documento.model";
import {SolicitudCreacioUdModel} from "../../models/solicitud-creacio-ud.model";
import {isNullOrUndefined} from "util";
import {Guid} from "../../../../../../infrastructure/utils/guid-generator";
import {ConfirmationService} from "primeng/primeng";


@Component({
  selector: 'app-seleccionar-unidad-documental',
  templateUrl: './seleccionar-unidad-documental.component.html',
  styleUrls: ['./seleccionar-unidad-documental.component.scss'],
  providers:[ConfirmationService]
})
export class SeleccionarUnidadDocumentalComponent implements OnInit, OnDestroy {

  @Input() archivarDocumentoModel:ArchivarDocumentoModel;
  @Input() solicitudModel:SolicitudCreacioUdModel;

  @Output() onChangeSection:EventEmitter<string> = new EventEmitter;

  @Output() onSelectUD:EventEmitter<any> = new EventEmitter;

  @Output() onDeselectUD:EventEmitter<any> = new EventEmitter;

  form: FormGroup;

  operation:string = "bUnidadDocumental";

  seriesObservable$:Observable<SerieDTO[]>;

  unidadesDocumentales$:Observable<UnidadDocumentalDTO[]>;

  subscriptions:Subscription[] = [];

  globalDependencySubscriptor:Subscription;

   subseriesObservable$:Observable<any[]>;

  dependenciaSelected$ : Observable<any>;

  dependenciaSelected : DependenciaDTO;

  documentos: any[];

  validations: any = {};



   constructor(
     private formBuilder: FormBuilder
     , private serieSubSerieService:SerieService
     ,private _store:Store<RootState>
     ,private _solicitudUDService:SolicitudCreacionUdService
     ,private _udService:UnidadDocumentalApiService
     ,private _confirmationService:ConfirmationService
     ,private  changeDetector:ChangeDetectorRef
   ) {

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

     this.subscriptions.forEach( subscription => subscription.unsubscribe());

     }

  ngOnInit(): void {

   this.subscriptions.push(this.dependenciaSelected$.subscribe(result =>{
     this.seriesObservable$ = this
       .serieSubSerieService
       .getSeriePorDependencia(result.codigo)
       .map(list => {

         if(isNullOrUndefined(list))
           list = [];

         list.unshift({codigoSerie:null,nombreSerie:"Seleccione"});

         return list;
       });
     this.dependenciaSelected = result;
   }));

  }

  addSolicitud(){

     if(this.form.valid) {

       this.subscriptions.push(
         this._store.select(getAuthenticatedFuncionario).subscribe( funcionario => {

           this.solicitudModel.Solicitudes= [ ... this.solicitudModel.Solicitudes,{
             codigoSede:this.dependenciaSelected.codSede,
             codigoDependencia:this.dependenciaSelected.codigo,
             codigoSerie: this.getControlValue("serie"),
             codigoSubSerie: this.getControlValue("subserie"),
             descriptor1: this.getControlValue("descriptor1"),
             descriptor2: this.getControlValue("descriptor2"),
             id: this.getControlValue("identificador"),
             nombreUnidadDocumental: this.getControlValue("nombre"),
             observaciones: this.getControlValue("observaciones"),
             nro:  Guid.next(),
             estado: "",
             idSolicitante: funcionario.id.toString()
           }];


           this.unidadesDocumentales$ = Observable.of(this.solicitudModel.Solicitudes);

           this.changeDetector.detectChanges();

           this.form.reset();
         })
     );



     }


  }

  private getControlValue(identificador:string):string{

     const value = this.form.get(identificador).value;

    return  value ? value.toString() : "";
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

     buscarUnidadDocumental(){

    //  this.visiblePopup = true;

       const observable = this._store.select(getSelectedDependencyGroupFuncionario).switchMap( dependencia => this._udService.Listar({
         codigoDependencia : dependencia.codigo,
         codigoSerie:this.form.get('serie').value,
         codigoSubSerie: this.form.get('subserie').value,
         id: this.form.get('identificador').value,
         nombreUnidadDocumental:this.form.get('nombre').value
       })).share();

      this.unidadesDocumentales$ = observable;

      this.subscriptions.push( observable.subscribe( uds => {

        if(uds.length == 0){
          this._confirmationService.confirm({
            message: 'El sistema no encuentra la unidad documental que está buscando.\n Por favor, solicite su creación',
            header: 'Resultados no encontrados',
            icon: 'fa fa-question-circle',
            accept: () => {

              this.operation = 'solicitarUnidadDocumental';

             this.changeSection('solicitarUnidadDocumental');
            },
            reject: () => {}
          });
        }
      }))

    }

    changeSection( section:string){

      this.onChangeSection.emit(section);
    }

    selectUnidadDocumental(evt){

     this.archivarDocumentoModel.UnidadDocumental = evt.data;

     this.onSelectUD.emit(evt.data);
    }

  deselectUnidadDocumental(evt){

     this.onDeselectUD.emit(evt.data);
  }


}

