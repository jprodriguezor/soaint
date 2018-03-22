import {Component, EventEmitter, Input, OnInit, Output, ViewChild, ViewEncapsulation} from '@angular/core';
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
import {
  MEDIO_RECEPCION_EMPRESA_MENSAJERIA,
  RADICACION_ENTRADA, RADICACION_SALIDA
} from '../../../shared/bussiness-properties/radicacion-properties';
import {ViewFilterHook} from "../../../shared/ViewHooksHelper";
import {ExtendValidators} from "../../../shared/validators/custom-validators";

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

  @Input()
  editable = true;

  @Input() tipoRadicacion = RADICACION_ENTRADA;

  @Input()
  editmode = false;

  @Input()
  mediosRecepcionInput: ConstanteDTO = null;

  @Output()
  onChangeTipoComunicacion: EventEmitter<any> = new EventEmitter();

  @Output() onChangeTipoDistribucion: EventEmitter<boolean> = new EventEmitter;

  validations: any = {};
  //
  // @ViewChild('dropDownThing')
  // dropDownThing: Dropdown;


  constructor(private _store: Store<State>, private _apiDatosGenerales: DatosGeneralesApiService, private _constSandbox: ConstanteSandbox, private formBuilder: FormBuilder) {

  }

  initForm() {

      this.form = this.formBuilder.group({
        'fechaRadicacion': [null],
        'nroRadicado': [null],
        'tipoComunicacion': [{value: null, disabled: !this.editable}, Validators.required],
        'medioRecepcion': [{value: null, disabled: !this.editable}, Validators.required],
        'empresaMensajeria': [{value: null, disabled: true}, Validators.required],
        'numeroGuia': [{value: null, disabled: true}, Validators.compose([Validators.required, Validators.maxLength(8)])],
        'tipologiaDocumental': [{value: null, disabled: (this.editmode) ? this.editable : !this.editable }, Validators.required],
        'unidadTiempo': [{value: null, disabled: !this.editable}],
        'numeroFolio': [{value: null, disabled: !this.editable}, Validators.required],
        'inicioConteo': [null],
        'reqDistFisica': [{value: null, disabled: !this.editable}],
        'clase_envio'  : [null],
        'modalidad_correo'  : [null],
        'reqDigit': [{value: '1', disabled: !this.editable}],
        'tiempoRespuesta': [{value: null, disabled: !this.editable}],
        'asunto': [{value: null, disabled: (this.editmode) ? this.editable : !this.editable}, Validators.compose([Validators.required, Validators.maxLength(500)])],
        'radicadoReferido': [{value: null, disabled: (this.editmode) ? this.editable : !this.editable}],
        'tipoAnexos': [{value: null, disabled: !this.editable}],
        'soporteAnexos': [{value: null, disabled: !this.editable}],
        'tipoAnexosDescripcion': [{value: null, disabled: !this.editable}, Validators.maxLength(300)],
        'hasAnexos': [{value: null, disabled: !this.editable}]
      });

      if(this.tipoRadicacion == RADICACION_SALIDA){

        this.form.setValidators([
          ExtendValidators.requiredIf('reqDistFisica',true,'clase_envio'),
          ExtendValidators.requiredIf('reqDistFisica',true,'modalidad_correo'),
        ]);
      };

  }

  changeTipoDistribucion(evt:boolean){

    this.onChangeTipoDistribucion.emit(evt);
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

  ngOnInit(): void {
    this.tipoComunicacionSuggestions$ = this._store.select(getTipoComunicacionArrayData);
    this.unidadTiempoSuggestions$ = this._store.select(getUnidadTiempoArrayData);
    this.tipoAnexosSuggestions$ = this._store.select(getTipoAnexosArrayData);
    this.medioRecepcionSuggestions$ = this._store.select(getMediosRecepcionArrayData);
    this.tipologiaDocumentalSuggestions$ = this._store.select(getTipologiaDocumentalArrayData);

    this.soporteAnexosSuggestions$ = this._store.select(getSoporteAnexoArrayData);

    this._constSandbox.loadDatosGeneralesDispatch();
    this._store.dispatch(new SedeAdministrativaLoadAction());

    this.initForm();

    this.form.get('tipoComunicacion').valueChanges.subscribe((value) => {
      this.onSelectTipoComunicacion(value);
    });

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

  addTipoAnexosDescripcion() {
    const tipoAnexo = this.form.get('tipoAnexos').value;
    const soporteAnexo = this.form.get('soporteAnexos').value;
    const descripcion = this.form.get('tipoAnexosDescripcion').value;

    if (!tipoAnexo) {
      return;
    }
    const insertVal = [{tipoAnexo: tipoAnexo, descripcion: descripcion, soporteAnexo: soporteAnexo}];
    this.descripcionAnexos = [
      ...insertVal,
      ...this.descripcionAnexos.filter(
        value => value.tipoAnexo.nombre !== tipoAnexo.nombre ||
          value.descripcion !== descripcion
      )
    ];
    this.form.get('hasAnexos').setValue(this.descripcionAnexos.length);
    this.form.get('tipoAnexos').reset();
    this.form.get('soporteAnexos').reset();
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
    if (removeVal.length === 0) {
      this.form.get('hasAnexos').reset();
    }
    this.descripcionAnexos = removeVal;
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
  // resetCarFilter() {
  //  console.log(this.dropDownThing);
  //  this.dropDownThing.selectedOption = null;
  // }

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

  showDistributionFields():boolean{

    return this.tipoRadicacion == RADICACION_SALIDA && this.form.get('reqDistFisica').value;
  }


}
