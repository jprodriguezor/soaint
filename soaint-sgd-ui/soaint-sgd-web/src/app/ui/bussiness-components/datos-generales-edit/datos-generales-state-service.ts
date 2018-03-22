import {Component, EventEmitter, Input, OnInit, Output, ViewChild, ViewEncapsulation, OnDestroy, Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {
  getMediosRecepcionArrayData,
  getTipoAnexosArrayData,
  getTipoComunicacionArrayData,
  getTipologiaDocumentalArrayData,
  getUnidadTiempoArrayData,
  getSoporteAnexoArrayData
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Dropdown } from 'primeng/components/dropdown/dropdown';
import 'rxjs/add/operator/single';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {DatosGeneralesApiService} from '../../../infrastructure/api/datos-generales.api';
import {createSelector} from 'reselect';
import {getUnidadTiempoEntities} from '../../../infrastructure/state-management/constanteDTO-state/selectors/unidad-tiempo-selectors';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {MEDIO_RECEPCION_EMPRESA_MENSAJERIA} from '../../../shared/bussiness-properties/radicacion-properties';

@Injectable()
export class DatosGeneralesStateService {

  form: FormGroup;
  dataform: any = null;
  visibility: any = {};

  radicadosReferidos: Array<{ nombre: string }> = [];
  descripcionAnexos: Array<{ tipoAnexo: ConstanteDTO, descripcion: string, soporteAnexo: ConstanteDTO }> = [];

  tipoComunicacionSuggestions$: Observable<any[]>;
  unidadTiempoSuggestions$: Observable<ConstanteDTO[]>;
  tipoAnexosSuggestions$: Observable<ConstanteDTO[]>;
  soporteAnexosSuggestions$: Observable<any[]>;
  medioRecepcionSuggestions$: Observable<ConstanteDTO[]>;
  tipologiaDocumentalSuggestions$: Observable<ConstanteDTO[]>;
  metricasTiempoTipologia$: Observable<any>;
  defaultSelectionMediosRecepcion$: Observable<any>;

  // default values Metricas por Tipologia
  medioRecepcionMetricaTipologia$: Observable<ConstanteDTO> = Observable.of(null);
  tiempoRespuestaMetricaTipologia$: Observable<number> = Observable.of(null);
  codUnidaTiempoMetricaTipologia$: Observable<ConstanteDTO> = Observable.of(null);

  disabled = true;

  @Input()
  mediosRecepcionInput: ConstanteDTO = null;

  @Output()
  onChangeTipoComunicacion: EventEmitter<any> = new EventEmitter();

  validations: any = {};

  constructor(
    private _store: Store<State>,
     private _apiDatosGenerales: DatosGeneralesApiService,
     private _constSandbox: ConstanteSandbox,
     private formBuilder: FormBuilder,
     ) {

  }

  Init() {
    this.initForm();
    this.LoadData();
  }

  initForm() {
    const reqDistFisica = (this.dataform.reqDistFisica === 1);
      this.form = this.formBuilder.group({
        'fechaRadicacion': [this.dataform.fechaRadicacion],
        'nroRadicado': [this.dataform.nroRadicado],
        'tipoComunicacion': [{value: this.dataform.tipoComunicacion, disabled: this.disabled}, Validators.required],
        'medioRecepcion': [{value: this.dataform.medioRecepcion, disabled: this.disabled}, Validators.required],
        'empresaMensajeria': [{value: this.dataform.empresaMensajeria, disabled: this.disabled}, Validators.required],
        'numeroGuia': [{value: this.dataform.numeroGuia, disabled: this.disabled}, Validators.compose([Validators.required, Validators.maxLength(8)])],
        'tipologiaDocumental': [{value: this.dataform.tipologiaDocumental, disabled: !this.disabled}, Validators.required],
        'unidadTiempo': [{value: this.dataform.unidadTiempo, disabled: this.disabled}],
        'numeroFolio': [{value: this.dataform.numeroFolio, disabled: this.disabled}, Validators.required],
        'inicioConteo': [this.dataform.inicioConteo],
        'reqDistFisica': [{value: reqDistFisica, disabled: this.disabled}],
        'reqDigit': [{value: this.dataform.reqDigit, disabled: this.disabled}],
        'tiempoRespuesta': [{value: this.dataform.tiempoRespuesta, disabled: this.disabled}],
        'asunto': [{value: this.dataform.asunto, disabled: !this.disabled}, Validators.compose([Validators.required, Validators.maxLength(500)])],
        'radicadoReferido': [{value: this.dataform.radicadoReferido, disabled: !this.disabled}],
        'tipoAnexos': [{value: null, disabled: this.disabled}],
        'soporteAnexos': [{value: null, disabled: this.disabled}],
        'tipoAnexosDescripcion': [{value: null, disabled: this.disabled}, Validators.maxLength(300)],
        'hasAnexos': [{value: this.dataform.hasAnexos, disabled: this.disabled}]
      });

  }

  listenForErrors() {
    this.bindToValidationErrorsOf('tipoComunicacion');
    this.bindToValidationErrorsOf('tipologiaDocumental');
    this.bindToValidationErrorsOf('numeroFolio');
    this.bindToValidationErrorsOf('asunto');
    this.bindToValidationErrorsOf('tipoAnexosDescripcion');
    this.bindToValidationErrorsOf('empresaMensajeria');
    this.bindToValidationErrorsOf('numeroGuia');
  }

  LoadData(): void {
    this.tipoComunicacionSuggestions$ = this._store.select(getTipoComunicacionArrayData);
    this.unidadTiempoSuggestions$ = this._store.select(getUnidadTiempoArrayData);
    this.tipoAnexosSuggestions$ = this._store.select(getTipoAnexosArrayData);
    this.medioRecepcionSuggestions$ = this._store.select(getMediosRecepcionArrayData);
    this.tipologiaDocumentalSuggestions$ = this._store.select(getTipologiaDocumentalArrayData);
    this.soporteAnexosSuggestions$ = this._store.select(getSoporteAnexoArrayData);

    this._constSandbox.loadDatosGeneralesDispatch();
    this._store.dispatch(new SedeAdministrativaLoadAction());

    this.form.get('tipologiaDocumental').valueChanges.subscribe((value) => {
      this.onSelectTipologiaDocumental(value);
    });

    this.form.get('medioRecepcion').valueChanges.subscribe((value) => {
      if (value && (value.codigo === MEDIO_RECEPCION_EMPRESA_MENSAJERIA)) {
        this.visibility.empresaMensajeria = true;
        this.visibility.numeroGuia = true;
      } else if (this.visibility.empresaMensajeria && this.visibility.numeroGuia) {
        delete this.visibility.empresaMensajeria;
        delete this.visibility.numeroGuia;
      }
    });
    this.listenForErrors();
  }

  addRadicadosReferidos() {
    const insertVal = [{nombre: this.form.get('radicadoReferido').value}];
    this.radicadosReferidos = [...insertVal, ...this.radicadosReferidos.filter(value => value.nombre !== this.form.get('radicadoReferido').value)];
    this.form.get('radicadoReferido').reset();
  }


  deleteRadicadoReferido(index) {
    const removeVal = [...this.radicadosReferidos];
    removeVal.splice(index, 1);
    this.radicadosReferidos = removeVal;
  }


  onSelectTipologiaDocumental(codigoTipologia) {
    this.metricasTiempoTipologia$ = this._apiDatosGenerales.loadMetricasTiempo(codigoTipologia);
    this.metricasTiempoTipologia$.subscribe(metricas => {
      console.log(metricas);
      this.tiempoRespuestaMetricaTipologia$ = Observable.of(metricas.tiempoRespuesta);
      this.codUnidaTiempoMetricaTipologia$ = this._store.select(createSelector(getUnidadTiempoEntities, (entities) => {
        return entities[metricas.codUnidaTiempo];
      }));
      this.form.get('inicioConteo').setValue(metricas.inicioConteo);
    });
  }

  onSelectTipoComunicacion(value) {
    this.onChangeTipoComunicacion.emit(value);
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
