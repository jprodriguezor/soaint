import {ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SupertypeSeries} from "../../shared/supertype-series";
import  {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {SerieService} from "../../../../../infrastructure/api/serie.service";
import {Observable} from "rxjs/Observable";
import {ConfirmationService} from "primeng/primeng";
import {UnidadDocumentalDTO} from "../../../../../domain/unidadDocumentalDTO";
import {UnidadDocumentalApiService} from "../../../../../infrastructure/api/unidad-documental.api";
import {isNullOrUndefined} from "util";
import {SolicitudCreacionUdService} from "../../../../../infrastructure/api/solicitud-creacion-ud.service";
import {SolicitudCreacioUdModel} from "../../archivar-documento/models/solicitud-creacio-ud.model";



@Component({
  selector: 'app-form-crear-unidad-documental',
  templateUrl: './form-crear-unidad-documental.component.html',
})
export class FormCrearUnidadDocumentalComponent extends SupertypeSeries implements  OnChanges{

  form:FormGroup;

  filterUDForm:FormGroup;

  formAsignarUT:FormGroup;

  subserieObservable2$:Observable<any[]>;


  @Input() solicitudModel:SolicitudCreacioUdModel;

  unidadesDocumentales$:Observable<UnidadDocumentalDTO[]>;

  @Output() onCreateUnidadDocumental:EventEmitter<any>  = new EventEmitter;


  constructor(private fb:FormBuilder,
              store:Store<RootState>,
              serieService:SerieService,
              private confirmationService:ConfirmationService,
              private udService:UnidadDocumentalApiService,
              private solicitudService:SolicitudCreacionUdService,
              private changeDetector:ChangeDetectorRef
              ) {

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

          data.codigoDependencia = this.dependenciaSelected.codigo;

          data.codigoSede = this.dependenciaSelected.codSede;

          this.solicitudService.actualizarSolicitudes(this.solicitudModel.SolicitudSelected).subscribe( () => this.onCreateUnidadDocumental.emit({action:"Creación UD"}) );

          this.solicitudModel.removeAtIndex();

          this.changeDetector.detectChanges();

        }, error => {});

      },
      reject: () => {

      }
    });
  }

  ngOnChanges(){

    if(this.solicitudModel.SelectedIndex > -1){

      const solicitud = this.solicitudModel.SolicitudSelected;

      this.subseriesObservable$ = this.dependenciaSelected$.switchMap( dependencia => this
        ._serieSubserieService
        .getSubseriePorDependenciaSerie(dependencia.codigo,solicitud.codigoSerie)
        .map(list => {
          list.unshift({codigoSubSerie:null,nombreSubSerie:"Seleccione"});
          return list;
        }),(out,inner) => inner );


      this.form.get("serie").setValue(solicitud.codigoSerie);
      this.form.get('subserie').setValue(solicitud.codigoSubSerie);
      this.form.get('identificador').setValue(solicitud.id);
      this.form.get("nombre").setValue(solicitud.nombreUnidadDocumental);
      this.form.get("descriptor1").setValue(solicitud.descriptor1);
      this.form.get("descriptor2").setValue(solicitud.descriptor2);
      this.form.get("observaciones").setValue(solicitud.observaciones);

    }
  }
}
