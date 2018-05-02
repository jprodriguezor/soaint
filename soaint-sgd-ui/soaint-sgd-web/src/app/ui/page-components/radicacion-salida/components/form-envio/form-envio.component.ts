import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Observable} from "rxjs/Observable";
import {ConstanteDTO} from "../../../../../domain/constanteDTO";
import  {Sandbox as ConstantesSandbox} from "../../../../../infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox";
import  {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {getModalidadCorreoArrayData} from "../../../../../infrastructure/state-management/constanteDTO-state/selectors/modalidad-correo-selectors";
import {getClaseEnvioArrayData} from "../../../../../infrastructure/state-management/constanteDTO-state/selectors/clase-envio-selectors";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-form-envio',
  templateUrl: './form-envio.component.html',
})
export class FormEnvioComponent implements OnInit{

  form:FormGroup;

  modalidadCorreo$:Observable<ConstanteDTO[]>;

  claseEnvio$:Observable<ConstanteDTO[]>;

 @Output()  onChangeFormStatus:EventEmitter<boolean> = new EventEmitter;

  constructor(private fb:FormBuilder,private _sandbox:ConstantesSandbox,private _store:Store<RootState>) {

    this.form = this.fb.group({
      clase_envio:[null,Validators.required],
      modalidad_correo:[null,Validators.required]
    });
  }

  ngOnInit() {

    this._sandbox.loaddatosEnvioDispatch();

    this.modalidadCorreo$ = this._store.select(getModalidadCorreoArrayData);
    this.claseEnvio$ = this._store.select(getClaseEnvioArrayData);
  }


}
