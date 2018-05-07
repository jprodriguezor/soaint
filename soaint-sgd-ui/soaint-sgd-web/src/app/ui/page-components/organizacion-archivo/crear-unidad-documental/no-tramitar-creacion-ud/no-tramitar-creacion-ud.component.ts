
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

  @Input() solicitud:SolicitudCreacionUDDto;

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

       if(!isNullOrUndefined(this.form.get('motivo')))
         this.solicitud.motivo =  this.form.get('motivo').value;

       this.solicitud.accion = "No Tramitar UD";

        this._solicitudService.actualizarSolicitudes(this.solicitud)
         .subscribe(() => {
           this.onNoTramitarUnidadDocumental.emit(this.solicitud);
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
