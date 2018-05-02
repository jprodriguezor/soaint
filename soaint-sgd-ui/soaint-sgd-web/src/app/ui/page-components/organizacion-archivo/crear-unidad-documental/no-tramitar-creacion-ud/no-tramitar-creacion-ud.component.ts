
import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {SolicitudCreacionUDDto} from "../../../../../domain/solicitudCreacionUDDto";
import {UnidadDocumentalApiService} from "../../../../../infrastructure/api/unidad-documental.api";
import {ConfirmationService} from "primeng/primeng";
import {isNullOrUndefined} from "util";
import {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {Subscription} from "rxjs/Subscription";
import {getSelectedDependencyGroupFuncionario} from "../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {DependenciaDTO} from "../../../../../domain/dependenciaDTO";

@Component({
  selector: 'app-no-tramitar-creacion-ud',
  templateUrl: './no-tramitar-creacion-ud.component.html',
  providers:[ConfirmationService]
})
export class NoTramitarCreacionUdComponent implements OnInit,OnChanges,OnDestroy {

  form:FormGroup;

  dependenciaSelected:DependenciaDTO;

  unsubscriber:Subscription;

  @Input() solicitud:SolicitudCreacionUDDto;

  @Output() onNoTramitarUnidadDocumental:EventEmitter<any> = new EventEmitter;


  constructor( private fb:FormBuilder
              ,private _udService:UnidadDocumentalApiService
              ,private _confirmationService:ConfirmationService
              ,private  _store:Store<RootState>
               ) {

    this.form = fb.group({
      "identificador":[{value:null,disabled:true}],
      "nombre":[{value:null,disabled:true}],
      "descriptor1":[{value:null,disabled:true}],
      "descriptor2":[{value:null,disabled:true}],
      "motivo":null,
      "observaciones":null
    });
  }


ngOnChanges(){

   this.form.setValue({
     'identificador':this.solicitud.id,
     'nombre':this.solicitud.nombreUnidadDocumental,
     'descriptor1':this.solicitud.descriptor1,
     'descriptor2':this.solicitud.descriptor2,
     'motivo' : null,
     'observaciones': null,
   });
}

 noTramitarCreacionUnidadesDocumentales(){

   this._confirmationService.confirm({
     message: '¿Está seguro que desea no tramitar esta unidad documental?',
     header: 'Confirmacion',
     icon: 'fa fa-question-circle',
     accept: () => {

       const  data = {
         //ubicacionTopografica:this.formAsignarUT.value,
         codigoSede:this.dependenciaSelected.codSede,
         codigoDependencia:this.dependenciaSelected.codigo,
         codigoSerie:this.solicitud.codigoSerie,
         codigoSubSerie:this.solicitud.codigoSubSerie,
         id:this.solicitud.id,
         nombreUnidadDocumental:this.solicitud.nombreUnidadDocumental,
         descriptor1:   this.solicitud.descriptor1,
         descriptor2:   this.solicitud.descriptor2,
         motivo: !isNullOrUndefined(this.form.get('motivo')) ? this.form.get('motivo').value: "",
         observaciones: !isNullOrUndefined(this.form.get('observaciones')) ? this.form.get('observaciones').value: "",
       };


       this._udService.noTramitarUnidadesDocumentales(data)
         .subscribe(() => {

           this.onNoTramitarUnidadDocumental.emit(data)

         }, error => {});

     },
     reject: () => {

     }
   });

 }

  ngOnInit(): void {

    this.unsubscriber = this._store.select(getSelectedDependencyGroupFuncionario).subscribe( dependencia => this.dependenciaSelected = dependencia )
  }

  ngOnDestroy(): void {

    this.unsubscriber.unsubscribe();
  }


}
