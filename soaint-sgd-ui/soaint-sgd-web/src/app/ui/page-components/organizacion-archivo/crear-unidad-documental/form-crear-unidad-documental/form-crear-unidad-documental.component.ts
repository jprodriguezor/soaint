import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SupertypeSeries} from "../../shared/supertype-series";
import  {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {SerieService} from "../../../../../infrastructure/api/serie.service";
import {Observable} from "rxjs/Observable";
import {ConfirmationService} from "primeng/primeng";
import {UnidadDocumentalDTO} from "../../../../../domain/unidadDocumentalDTO";
import {UnidadDocumentalApiService} from "../../../../../infrastructure/api/unidad-documental.api";
import {SolicitudCreacionUDDto} from "../../../../../domain/solicitudCreacionUDDto";
import {isNullOrUndefined} from "util";


@Component({
  selector: 'app-form-crear-unidad-documental',
  templateUrl: './form-crear-unidad-documental.component.html',
})
export class FormCrearUnidadDocumentalComponent extends SupertypeSeries implements  OnChanges{

  form:FormGroup;

  filterUDForm:FormGroup;

  formAsignarUT:FormGroup;

  subserieObservable2$:Observable<any[]>;

  UDSelected:UnidadDocumentalDTO;

  unidadesDocumentales$:Observable<UnidadDocumentalDTO[]>;

  @Input() solicitud?:SolicitudCreacionUDDto;

  @Output() onCreateUnidadDocumental:EventEmitter<any>  = new EventEmitter;


  constructor(private fb:FormBuilder,store:Store<RootState>,serieService:SerieService,private confirmationService:ConfirmationService,private udService:UnidadDocumentalApiService) {

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

  relacionarUD(){

    this.confirmationService.confirm({
      message: '¿Está seguro que desea relacionar esta unidad documental?',
      header: 'Confirmacion',
      icon: 'fa fa-question-circle',
      accept: () => {


      },
      reject: () => {

      }
    });

  }

  crearUD(){

    this.confirmationService.confirm({
      message: '¿Está seguro que desea crear esta unidad documental?',
      header: 'Confirmacion',
      icon: 'fa fa-question-circle',
      accept: () => {

      const  data = {
          //ubicacionTopografica:this.formAsignarUT.value,
          codigoSede:this.dependenciaSelected.codSede,
          codigoDependencia:this.dependenciaSelected.codigo,
          codigoSerie:this.form.get('serie').value,
          codigoSubSerie:this.form.get('subserie').value,
          id:this.form.get('identificador').value,
          nombreUnidadDocumental:this.form.get('nombre').value,
          descriptor1: !isNullOrUndefined(this.form.get('descriptor1')) ? this.form.get('descriptor1').value: "",
          descriptor2: !isNullOrUndefined(this.form.get('descriptor2')) ? this.form.get('descriptor2').value: "",
          observaciones:this.form.get('observaciones').value,
          accion:"Creación UD"
        };

        this.udService.crear(data)
        .subscribe(() => {

          this.onCreateUnidadDocumental.emit(data)

        }, error => {});

      },
      reject: () => {

      }
    });
  }

  ngOnChanges(){

    if(this.solicitud){

      this.subseriesObservable$ = this.dependenciaSelected$.switchMap( dependencia => this
        ._serieSubserieService
        .getSubseriePorDependenciaSerie(dependencia.codigo,this.solicitud.codigoSerie)
        .map(list => {
          list.unshift({codigoSubSerie:null,nombreSubSerie:"Seleccione"});
          return list;
        }),(out,inner) => inner );


      this.form.get("serie").setValue(this.solicitud.codigoSerie);
      this.form.get('subserie').setValue(this.solicitud.codigoSubSerie);
      this.form.get('identificador').setValue(this.solicitud.id);
      this.form.get("nombre").setValue(this.solicitud.nombreUnidadDocumental);
      this.form.get("descriptor1").setValue(this.solicitud.descriptor1);
      this.form.get("descriptor2").setValue(this.solicitud.descriptor2);
      this.form.get("observaciones").setValue(this.solicitud.observaciones);


    }
  }
}