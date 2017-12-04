import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {ProduccionDocumentalApiService} from 'app/infrastructure/api/produccion-documental.api';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {getAuthenticatedFuncionario} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {FuncionarioDTO} from 'app/domain/funcionarioDTO';
import {PdMessageService} from '../../providers/PdMessageService';
import {TareaDTO} from 'app/domain/tareaDTO';
import {VersionDocumentoDTO} from '../../models/VersionDocumentoDTO';
import {AnexoDTO} from '../../models/AnexoDTO';

@Component({
  selector: 'pd-datos-generales',
  templateUrl: './datos-generales.component.html'
})

export class PDDatosGeneralesComponent implements OnInit {

  form: FormGroup;

  validations: any = {};

  @Input()
  taskData: TareaDTO;

  funcionarioLog: FuncionarioDTO;

  tiposComunicacion$: Observable<ConstanteDTO[]>;
  tiposAnexo$: Observable<ConstanteDTO[]>;
  tiposPlantilla$: Observable<ConstanteDTO[]>;

  confirmadaGenerarVersion = false;
  versionesDocumentoVisible = false;
  nombreVersionDocumento: string;

  fechaCreacion = new Date();

  tipoPlantillaSelected: ConstanteDTO[];

  listaVersionesDocumento: VersionDocumentoDTO[] = [];
  listaAnexos: AnexoDTO[] = [];

  constructor(private _store: Store<State>,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private pdMessageService: PdMessageService) {
    console.log(this.form);
    this.initForm();
  }


  initForm() {
    console.log(this.form);
    console.log(this.formBuilder);
    this.form = this.formBuilder.group({
      'tipoComunicacion': [null, Validators.required],
      'tipoPlantilla': [null, Validators.required],
      'elaborarDocumento': [null],
      'soporte': 'electronico',
      'tipoAnexo': [null],
      'descripcion': [null],
    });
  }


  tipoComunicacionChange(event) {
    this.pdMessageService.sendMessage(event.value);
  }

  tipoPlanillaChange(event) {
    this.tipoPlantillaSelected = event.value;
    this.nombreVersionDocumento = event.value.nombre + '_';
  }

  hideVersionesDocumentoDialog() {
    this.versionesDocumentoVisible = false;
    this.confirmadaGenerarVersion = false;
  }

  showVersionesDocumentoDialog() {
    this.versionesDocumentoVisible = true;
  }

  confirmarGenerarVersion() {
    this.confirmadaGenerarVersion = true;
  }

  agregarVersion() {
    const versiones = this.listaVersionesDocumento;
    versiones.push({
      tipo: 'PDF',
      nombre: this.nombreVersionDocumento,
      size: Math.floor(Math.random() * (100000 - 1) + 1)
    });
    this.listaVersionesDocumento = [...versiones];
    this.hideVersionesDocumentoDialog();
    this.refreshView();
  }

  agregarAnexo() {
    const anexos = this.listaAnexos;
    const anexo: AnexoDTO = {
      soporte: this.form.get('soporte').value,
      tipo: this.form.get('tipoAnexo').value,
      descripcion: this.form.get('descripcion').value
    };
    anexos.push(anexo);
    this.listaAnexos = [...anexos];
    this.refreshView();
  }


  ngOnInit(): void {
    this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });

    this.tiposComunicacion$ = this._produccionDocumentalApi.getTiposComunicacion({});
    this.tiposAnexo$ = this._produccionDocumentalApi.getTiposAnexo({});
    this.tiposPlantilla$ = this._produccionDocumentalApi.getTiposPlantilla({});

    this.listenForErrors();
    console.log(this.taskData);
  }


  listenForErrors() {
    this.bindToValidationErrorsOf('tipoPlantilla');
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

