
import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {SolicitudCreacionUDDto} from "../../../../../domain/solicitudCreacionUDDto";
import {ConfirmationService} from "primeng/primeng";
import {isNullOrUndefined} from "util";
import {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {Subscription} from "rxjs/Subscription";
import {getSelectedDependencyGroupFuncionario} from "../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {DependenciaDTO} from "../../../../../domain/dependenciaDTO";
import {SolicitudCreacionUdService} from "../../../../../infrastructure/api/solicitud-creacion-ud.service";
import {ConstanteDTO} from "../../../../../domain/constanteDTO";
import {Observable} from "rxjs/Observable";
import  {Sandbox as ConstanteSandbox} from "../../../../../infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox";
import {SolicitudCreacioUdModel} from "../../archivar-documento/models/solicitud-creacio-ud.model";

@Component({
  selector: 'app-no-tramitar-creacion-ud',
  templateUrl: './no-tramitar-creacion-ud.component.html',
  providers:[ConfirmationService]
})
export class NoTramitarCreacionUdComponent implements OnInit,OnChanges,OnDestroy {

  form:FormGroup;

  dependenciaSelected:DependenciaDTO;

  motivo$:Observable<ConstanteDTO[]>;

  unsubscriber:Subscription;

  @Input() solicitudModel:SolicitudCreacioUdModel;

  @Output() onNoTramitarUnidadDocumental:EventEmitter<any> = new EventEmitter;


  constructor( private fb:FormBuilder
              ,private _solicitudService:SolicitudCreacionUdService
              ,private _confirmationService:ConfirmationService
              ,private  _store:Store<RootState>
              ,private  _sandbox: ConstanteSandbox
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

    if(this.solicitudModel.SelectedIndex == -1)
      return;

    const solicitud = this.solicitudModel.SolicitudSelected;

   this.form.setValue({
     'identificador':solicitud.id,
     'nombre':solicitud.nombreUnidadDocumental,
     'descriptor1':solicitud.descriptor1,
     'descriptor2':solicitud.descriptor2,
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

       const solicitud = this.solicitudModel.SolicitudSelected;

       if(!isNullOrUndefined(this.form.get('motivo')))
         solicitud.motivo =  this.form.get('motivo').value;

       solicitud.accion = "No Tramitar UD";

        this._solicitudService.actualizarSolicitudes(solicitud)
         .subscribe(() => {

           this.onNoTramitarUnidadDocumental.emit();

            this.solicitudModel.removeAtIndex();
         }, error => {});
     },
     reject: () => {}
   });
 }
  ngOnInit(): void {

    this._sandbox.loadMotivoNoCreacionUdDispatch();

    this.unsubscriber = this._store.select(getSelectedDependencyGroupFuncionario).subscribe( dependencia => this.dependenciaSelected = dependencia )
  }

  ngOnDestroy(): void {

    this.unsubscriber.unsubscribe();
  }


}
