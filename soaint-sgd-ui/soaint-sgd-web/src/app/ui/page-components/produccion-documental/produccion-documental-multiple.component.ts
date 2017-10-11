import {ChangeDetectorRef, Component, Input, OnInit, ViewChild} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";
import {Store} from "@ngrx/store";
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {ProduccionDocumentalApiService} from "app/infrastructure/api/produccion-documental.api";
import {Observable} from "rxjs/Observable";
import {ConstanteDTO} from "app/domain/constanteDTO";
import {
  getArrayData as getFuncionarioArrayData
} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {VALIDATION_MESSAGES} from "app/shared/validation-messages";
import {FuncionarioDTO} from "app/domain/funcionarioDTO";
import {Sandbox} from "app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox";
import {ProyeccionDocumentoDTO} from "../../../domain/ProyeccionDocumentoDTO";

@Component({
  selector: 'produccion-documental-multiple',
  templateUrl: './produccion-documental-multiple.component.html',
  styleUrls: ['produccion-documental.component.css'],
})

export class ProduccionDocumentalMultipleComponent implements OnInit{


  constructor(private _store: Store<RootState>,
              private _produccionDocumentalApi : ProduccionDocumentalApiService,
              private _funcionarioSandbox: Sandbox,
              private _changeDetectorRef: ChangeDetectorRef,
              private formBuilder: FormBuilder) {  }

  form: FormGroup;
  validations: any = {};
  listaProyectores : ProyeccionDocumentoDTO[] = [];

  sedesAdministrativas$ : Observable<ConstanteDTO[]>;
  dependencias$ : Observable<ConstanteDTO[]>;
  funcionarios$ : Observable<FuncionarioDTO[]>;

  tiposPlantilla : ConstanteDTO[];



  agregarProyector() {
    let proyectores = this.listaProyectores;
    let proyector : ProyeccionDocumentoDTO = {
      sede: this.form.get('sede').value,
      dependencia: this.form.get('dependencia').value,
      funcionario: this.form.get('funcionario').value,
      tipoPlantilla: this.form.get('tipoPlantilla').value
    };
    proyectores.push(proyector);
    this.listaProyectores = [...proyectores];
    console.log(this.listaProyectores);
    this.refreshView();
  }


  initForm() {
    this.form = this.formBuilder.group({
      //Datos generales
      'sede': [{value: null}],
      'dependencia': [{value: null}],
      'funcionario': [{value: null}],
      'tipoPlantilla': [{value:null}],
    });
  }

  ngOnInit(): void {
    this.sedesAdministrativas$ = this._produccionDocumentalApi.getSedes({});
    this.dependencias$ = this._produccionDocumentalApi.getDependencias({});
    this.tiposPlantilla = this._produccionDocumentalApi.getTiposPlantilla({});
    this.funcionarios$ = this._store.select(getFuncionarioArrayData);
    this._funcionarioSandbox.loadAllFuncionariosDispatch();

    this.initForm();
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('tipoComunicacion');
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
    ac.valueChanges.subscribe(value => {
      if ((ac.touched || ac.dirty) && ac.errors) {
        const error_keys = Object.keys(ac.errors);
        const last_error_key = error_keys[error_keys.length - 1];
        this.validations[control] = VALIDATION_MESSAGES[last_error_key];
      } else {
        delete this.validations[control];
      }
    });
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }
}
