import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from "@angular/forms";
import {Observable} from "rxjs/Observable";
import {ConstanteDTO} from "app/domain/constanteDTO";
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {ProduccionDocumentalApiService} from "app/infrastructure/api/produccion-documental.api";
import {VALIDATION_MESSAGES} from "app/shared/validation-messages";
import {getAuthenticatedFuncionario} from "app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {FuncionarioDTO} from "app/domain/funcionarioDTO";
import {PdMessageService} from "../../providers/PdMessageService";

@Component({
  selector: 'pd-datos-generales',
  templateUrl: './datos-generales.component.html'
})

export class PDDatosGeneralesComponent implements OnInit{

  form: FormGroup;
  validations: any = {};

  funcionarioLog: FuncionarioDTO;

  tiposComunicacion$: Observable<ConstanteDTO[]>;
  tiposAnexo$: Observable<ConstanteDTO[]>;



  constructor(private _store: Store<State>,
              private _produccionDocumentalApi : ProduccionDocumentalApiService,
              private formBuilder: FormBuilder,
              private pdMessageService : PdMessageService){}


  initForm() {
    this.form = this.formBuilder.group({
      //Datos generales
      'usuarioResponsable': [this.usuarioResponsableFullname()],
      'fechaCreacion': [new Date()],
      'sedeAdministrativa': [null],
      'dependencia': [null],

      //Radicado asociado
      'fechaRadicacion': [new Date()],
      'noRadicado': [null],

      //Producir documento
      'tipoComunicacion': [{value: null}, Validators.required],
      'tipoPlantilla': [{value:null}],
      'elaborarDocumento': [null],

      //Anexos
      'soporte':"electronico",
      'tipoAnexo': [{value: null}],
      'descripcion': [null],
    });
  }


  tipoComunicacionChange(event) {
    this.pdMessageService.sendMessage(event.value);
  }




  ngOnInit(): void {
    this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });

    this.tiposComunicacion$ = this._produccionDocumentalApi.getTiposComunicacion({});
    this.tiposAnexo$ = this._produccionDocumentalApi.getTiposAnexo({});


    this.initForm();

    this.listenForErrors();
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

  usuarioResponsableFullname () {
    return (this.funcionarioLog.nombre + " " + this.funcionarioLog.valApellido1 + " " + this.funcionarioLog.valApellido2).trim();
  }
}

