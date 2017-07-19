import {Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {
  getMediosRecepcionArrayData,
  getTipoAnexosArrayData,
  getTipoComunicacionArrayData,
  getTipologiaDocumentalArrayData,
  getUnidadTiempoArrayData
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import 'rxjs/add/operator/single';
import {getVentanillaData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/medios-recepcion-selectors';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';

@Component({
  selector: 'app-datos-generales',
  templateUrl: './datos-generales.component.html',
  styles: [`
    .ui-datalist-header, .ui-datatable-header {
      text-align: left !important;
    }
  `],
  encapsulation: ViewEncapsulation.None
})
export class DatosGeneralesComponent implements OnInit {

  form: FormGroup;

  radicadosReferidos: Array<{ nombre: string }> = [];
  descripcionAnexos: Array<{ tipoAnexo: ConstanteDTO, descripcion: string }> = [];

  tipoComunicacionSuggestions$: Observable<any[]>;
  unidadTiempoSuggestions$: Observable<ConstanteDTO[]>;
  tipoAnexosSuggestions$: Observable<ConstanteDTO[]>;
  medioRecepcionSuggestions$: Observable<ConstanteDTO[]>;
  tipologiaDocumentalSuggestions$: Observable<ConstanteDTO[]>;

  defaultSelectionMediosRecepcion$: Observable<any>;

  @Input()
  editable = true;

  @Input()
  datosRemitente: any;

  @Input()
  datosDestinatario: any;

  selectionMediosRecepcion: any;

  @Output()
  onChangeTipoComunicacion: EventEmitter<any> = new EventEmitter();

  validations: any = {};


  constructor(private _store: Store<State>, private _sandbox: Sandbox, private formBuilder: FormBuilder) {
  }

  initForm() {

    this.form = this.formBuilder.group({
      'fechaRadicacion': [null],
      'nroRadicado': [null],
      'tipoComunicacion': [{value: null, disabled: !this.editable}, Validators.required],
      'medioRecepcion': [{value: null, disabled: !this.editable}, Validators.required],
      'tipologiaDocumental': [{value: null, disabled: !this.editable}, Validators.required],
      'unidadTiempo': [{value: null, disabled: !this.editable}],
      'numeroFolio': [{value: null, disabled: !this.editable}, Validators.required],
      'reqDistFisica': [{value: null, disabled: !this.editable}],
      'reqDigit': [{value: null, disabled: !this.editable}],
      'tiempoRespuesta': [{value: null, disabled: !this.editable}],
      'asunto': [{value: null, disabled: !this.editable}, Validators.required],
      'radicadoReferido': [{value: null, disabled: !this.editable}],
      'tipoAnexos': [{value: null, disabled: !this.editable}],
      'tipoAnexosDescripcion': [{value: null, disabled: !this.editable}],
    });
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('tipoComunicacion');
    this.bindToValidationErrorsOf('tipologiaDocumental');
    this.bindToValidationErrorsOf('numeroFolio');
    this.bindToValidationErrorsOf('asunto');
  }

  ngOnInit(): void {
    this.tipoComunicacionSuggestions$ = this._store.select(getTipoComunicacionArrayData);
    this.unidadTiempoSuggestions$ = this._store.select(getUnidadTiempoArrayData);
    this.tipoAnexosSuggestions$ = this._store.select(getTipoAnexosArrayData);
    this.medioRecepcionSuggestions$ = this._store.select(getMediosRecepcionArrayData);
    this.tipologiaDocumentalSuggestions$ = this._store.select(getTipologiaDocumentalArrayData);

    this._sandbox.loadCommonConstantsDispatch();

    this.initForm();

    this.form.get('tipoComunicacion').valueChanges.subscribe((value) => {
      this.onSelectTipoComunicacion(value);
    });

    this.defaultSelectionMediosRecepcion$ = this._store.select(getVentanillaData);
    this.listenForErrors();
  }

  addRadicadosReferidos() {
    const insertVal = [{nombre: this.form.get('radicadoReferido').value}];
    this.radicadosReferidos = [...insertVal, ...this.radicadosReferidos.filter(value => value.nombre !== this.form.get('radicadoReferido').value)];
    this.form.get('radicadoReferido').reset();
  }

  addTipoAnexosDescripcion() {
    const tipoAnexo = this.form.get('tipoAnexos').value;
    const descripcion = this.form.get('tipoAnexosDescripcion').value;
    console.log(tipoAnexo, descripcion);
    if (!tipoAnexo || !descripcion) {
      return;
    }
    const insertVal = [{tipoAnexo: tipoAnexo, descripcion: descripcion}];
    this.descripcionAnexos = [
      ...insertVal,
      ...this.descripcionAnexos.filter(
        value => value.tipoAnexo.nombre !== tipoAnexo.nombre ||
        value.descripcion !== descripcion
      )
    ];
    this.form.get('tipoAnexos').reset();
    this.form.get('tipoAnexosDescripcion').reset();
  }

  deleteRadicadoReferido(index) {
    const removeVal = [...this.radicadosReferidos];
    removeVal.splice(index, 1);
    this.radicadosReferidos = removeVal;
  }

  deleteTipoAnexoDescripcion(index) {
    const removeVal = [...this.descripcionAnexos];
    removeVal.splice(index, 1);
    this.descripcionAnexos = removeVal;
  }

  onSelectTipoComunicacion(value) {
    this.onChangeTipoComunicacion.emit(value);
    // this.datosRemitente.onSelectTipoPersona();
    // this.datosDestinatario.onSelectTipoComunicacion();
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


}
