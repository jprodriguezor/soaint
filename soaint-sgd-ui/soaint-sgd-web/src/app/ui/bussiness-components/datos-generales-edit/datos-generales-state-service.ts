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
import { ConstanteApiService } from '../../../infrastructure/api/constantes.api';

@Injectable()
export class DatosGeneralesStateService {

  form: FormGroup;
  dataform: any = null;
  visibility: any = {};

  radicadosReferidos: Array<{ nombre: string }> = [];
  descripcionAnexos: Array<{ tipoAnexo: ConstanteDTO, descripcion: string, soporteAnexo: ConstanteDTO }> = [];
  constantesAnexos: ConstanteDTO[];

  tipoComunicacionSuggestions$: Observable<ConstanteDTO[]>;
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
  ppdDocumentoList: any;
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
     private _contantesService: ConstanteApiService
     ) {

  }

  Init() {
    this.initForm();
    this.LoadData();
    this.Subcripciones();
  }

  initForm() {
    const reqDistFisica = (this.dataform.reqDistFisica === 1);
    const reqDigit = (this.dataform.reqDigit === 1) ? 1 : 2;
      this.form = this.formBuilder.group({
        'fechaRadicacion': [this.dataform.fechaRadicacion],
        'nroRadicado': [this.dataform.nroRadicado],
        'tipoComunicacion': [{value: null, disabled: this.disabled}, Validators.required],
        'medioRecepcion': [{value: null, disabled: this.disabled}, Validators.required],
        'empresaMensajeria': [{value: this.dataform.empresaMensajeria, disabled: this.disabled}, Validators.required],
        'numeroGuia': [{value: this.dataform.numeroGuia, disabled: this.disabled}, Validators.compose([Validators.required, Validators.maxLength(8)])],
        'tipologiaDocumental': [{value: null, disabled: !this.disabled}, Validators.required],
        'unidadTiempo': [{value: null, disabled: this.disabled}],
        'numeroFolio': [{value: this.dataform.numeroFolio, disabled: this.disabled}, Validators.required],
        'inicioConteo': [this.dataform.inicioConteo],
        'reqDistFisica': [{value: reqDistFisica, disabled: this.disabled}],
        'reqDigit': [{value: reqDigit, disabled: this.disabled}],
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
    // tipo de comunicacion seleccionada
    this.tipoComunicacionSuggestions$ = this._store.select(getTipoComunicacionArrayData);
    this.tipoComunicacionSuggestions$
    .subscribe(result => {
        const selected = result.find(_item => _item.codigo === this.dataform.tipoComunicacion);
        this.form.controls['tipoComunicacion'].setValue(selected);
    });
    // unidad tiempo seleccionada
    this.unidadTiempoSuggestions$ = this._store.select(getUnidadTiempoArrayData);
    this.unidadTiempoSuggestions$
    .subscribe(result => {
      const selected = result.find(_item => _item.codigo === this.dataform.unidadTiempo.codigo);
      this.form.controls['unidadTiempo'].setValue(selected);
    });
    // medio recepción seleccionada
    this.medioRecepcionSuggestions$ = this._store.select(getMediosRecepcionArrayData);
    this.medioRecepcionSuggestions$
    .subscribe(result => {
      const selected = result.find(_item => _item.codigo === this.dataform.medioRecepcion.codigo);
        this.form.controls['medioRecepcion'].setValue(selected);
    });
    // tipología documental seleccionada
    this.tipologiaDocumentalSuggestions$ = this._store.select(getTipologiaDocumentalArrayData);
    this.tipologiaDocumentalSuggestions$
    .subscribe(result => {
      const tipologiaDoc = this.ppdDocumentoList[0].codTipoDoc;
      const selected = result.find(_item => _item.codigo === tipologiaDoc )
      this.form.controls['tipologiaDocumental'].setValue(selected);
    });
    // listado anexos
    // this.soporteAnexosSuggestions$ = this._contantesService.Listar({key: 'tipoAnexos' });
    // this.tipoAnexosSuggestions$ = this._contantesService.Listar({key: 'soporteAnexo' });
    // const anexosInfo$ = Observable.concat(this.soporteAnexosSuggestions$, this.tipoAnexosSuggestions$);
    // anexosInfo$.subscribe((result: ConstanteDTO[]) => {
    //     this.descripcionAnexos = this.descripcionAnexos
    //     .reduce((_listado, _anexo) => {
    //       const codAnexo = result.find(item => item.codigo === _anexo.tipoAnexo.codigo);
    //       const codTipoSoporte = result.find(item => item.codigo === _anexo.soporteAnexo.codigo);
    //       _anexo.tipoAnexo.nombre = (codAnexo) ? codAnexo.nombre : '';
    //       _anexo.soporteAnexo.nombre = (codTipoSoporte) ? codTipoSoporte.nombre : '';
    //       _listado.push(_anexo);
    //       return _listado;
    //     }, []);
    // });

    // // listado anexos
    this.soporteAnexosSuggestions$ = this._contantesService.Listar({key: 'soporteAnexo' })
    this.soporteAnexosSuggestions$
    .subscribe((result) => {
      if (result) {
        this.constantesAnexos = [...result];
        this.tipoAnexosSuggestions$ = this._contantesService.Listar({key: 'tipoAnexos' });
        this.tipoAnexosSuggestions$
        .subscribe(resultsoporte => {
          this.constantesAnexos = [...result].concat(resultsoporte);
          this.descripcionAnexos = this.descripcionAnexos
          .reduce((_listado, _anexo) => {
            const codAnexo = this.constantesAnexos.find(item => item.codigo === _anexo.tipoAnexo.codigo);
            const codTipoSoporte = this.constantesAnexos.find(item => item.codigo === _anexo.soporteAnexo.codigo);
            _anexo.tipoAnexo.nombre = codAnexo.nombre;
            _anexo.soporteAnexo.nombre = codTipoSoporte.nombre;
            _listado.push(_anexo);
            return _listado;
          }, []);
        });
      }
    });
    this._constSandbox.loadDatosGeneralesDispatch();
    this._store.dispatch(new SedeAdministrativaLoadAction());
    this.onSelectTipologiaDocumental(this.form.value.tipologiaDocumental);
  }

  Subcripciones() {
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
    if (codigoTipologia) {
      this.metricasTiempoTipologia$ = this._apiDatosGenerales.loadMetricasTiempo(codigoTipologia);
      this.metricasTiempoTipologia$.subscribe(metricas => {
        this.form.controls['tiempoRespuesta'].setValue(metricas.tiempoRespuesta);
        this.codUnidaTiempoMetricaTipologia$ = this._store.select(createSelector(getUnidadTiempoEntities, (entities) => {
          return entities[metricas.codUnidaTiempo];
        }));
        this.form.get('inicioConteo').setValue(metricas.inicioConteo);
      });
    }
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
