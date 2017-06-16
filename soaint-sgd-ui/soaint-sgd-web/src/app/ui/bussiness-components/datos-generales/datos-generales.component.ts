import {Component, OnInit, ViewEncapsulation} from '@angular/core';
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
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';

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
  tipoComunicacionControl: AbstractControl;
  medioRecepcionControl: AbstractControl;
  tipologiaDocumentalControl: AbstractControl;
  unidadTiempoControl: AbstractControl;
  numeroFolioControl: AbstractControl;
  tiempoRespuestaControl: AbstractControl;
  cantidadAnexosControl: AbstractControl;
  asuntoControl: AbstractControl;
  tipoAnexosControl: AbstractControl;
  radicadoReferidoControl: AbstractControl;
  tipoAnexoDescripcionTipoAnexoControl: AbstractControl;
  tipoAnexoDescripcionDescripcionControl: AbstractControl;


  tipoComunicacionSuggestions$: Observable<ConstanteDTO[]>;
  unidadTiempoSuggestions$: Observable<ConstanteDTO[]>;
  tipoAnexosSuggestions$: Observable<ConstanteDTO[]>;
  medioRecepcionSuggestions$: Observable<ConstanteDTO[]>;
  tipologiaDocumentalSuggestions$: Observable<ConstanteDTO[]>;

  radicadosReferidos: Array<{ nombre: string }> = [];
  descripcionAnexos: Array<{ tipoAnexo: ConstanteDTO, descripcion: string }> = [];
  radicadoReferido: { nombre: string } = {nombre: ''};
  tipoAnexoDescripcion: { tipoAnexo: ConstanteDTO, descripcion: string } = {tipoAnexo: null, descripcion: ''};

  constructor(private _store: Store<State>, private _sandbox: Sandbox, private formBuilder: FormBuilder) {
    this.initForm();
  }

  initForm() {
    this.tipoComunicacionControl = new FormControl(null, Validators.required);
    this.medioRecepcionControl = new FormControl(null, Validators.required);
    this.tipologiaDocumentalControl = new FormControl(null, Validators.required);
    this.unidadTiempoControl = new FormControl(null);
    this.numeroFolioControl = new FormControl(null, Validators.required);
    this.tiempoRespuestaControl = new FormControl(null);
    this.cantidadAnexosControl = new FormControl(null);
    this.asuntoControl = new FormControl(null, Validators.required);
    this.tipoAnexosControl = new FormControl(null);
    this.radicadoReferidoControl = new FormControl(null);
    this.tipoAnexoDescripcionTipoAnexoControl = new FormControl(null);
    this.tipoAnexoDescripcionDescripcionControl = new FormControl(null);
    this.form = this.formBuilder.group({
      'tipoComunicacion': this.tipoComunicacionControl,
      'medioRecepcion': this.medioRecepcionControl,
      'tipologiaDocumental': this.tipologiaDocumentalControl,
      'unidadTiempo': this.unidadTiempoControl,
      'numeroFolio': this.numeroFolioControl,
      'tiempoRespuesta': this.tiempoRespuestaControl,
      'cantidadAnexos': this.cantidadAnexosControl,
      'asunto': this.asuntoControl,
      'tipoAnexos': this.tipoAnexosControl,
      'radicadoReferido': this.radicadoReferidoControl,
      'tipoAnexoDescripcionTipoAnexo': this.tipoAnexoDescripcionTipoAnexoControl,
      'tipoAnexoDescripcionDescripcion': this.tipoAnexoDescripcionDescripcionControl,
    });
  }

  ngOnInit(): void {
    this.tipoComunicacionSuggestions$ = this._store.select(getTipoComunicacionArrayData);
    this.unidadTiempoSuggestions$ = this._store.select(getUnidadTiempoArrayData);
    this.tipoAnexosSuggestions$ = this._store.select(getTipoAnexosArrayData);
    this.medioRecepcionSuggestions$ = this._store.select(getMediosRecepcionArrayData);
    this.tipologiaDocumentalSuggestions$ = this._store.select(getTipologiaDocumentalArrayData);
  }


  addRadicadosReferidos() {
    let radref = [...this.radicadosReferidos];
    radref.push(this.radicadoReferido);
    this.radicadosReferidos = radref;
    this.radicadoReferido = {
      nombre: ''
    };
  }

  deleteRadicadoReferido(index) {
    let radref = [...this.radicadosReferidos];
    radref.splice(index, 1);
    this.radicadosReferidos = radref;
  }


  addTipoAnexosDescripcion() {
    let radref = [...this.descripcionAnexos];
    console.log(this.tipoAnexoDescripcion);
    radref.push(this.tipoAnexoDescripcion);
    this.descripcionAnexos = radref;
    this.tipoAnexoDescripcion = {
      tipoAnexo: null, descripcion: ''
    };
  }

  deleteTipoAnexoDescripcion(index) {
    let radref = [...this.descripcionAnexos];
    radref.splice(index, 1);
    this.descripcionAnexos = radref;
  }

  onSelectTipoComunicacion(value) {
    console.info(value);
  }

  onFilterTipoComunicacion($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoComunicacion', query);
  }

  onDropdownClickTipoComunicacion($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoComunicacion');
  }

  onFilterTipoAnexos($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipoAnexos', query);
  }

  onDropdownClickTipoAnexos($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipoAnexos');
  }


  onFilterUnidadTiempo($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('unidadTiempo', query);
  }

  onDropdownClickUnidadTiempo($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('unidadTiempo');
  }

  onFilterMediosRecepcion($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('mediosRecepcion', query);
  }

  onDropdownClickMediosRecepcion($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('mediosRecepcion');
  }

  onFilterTipologiaDocumental($event) {
    const query = $event.query;
    this._sandbox.filterDispatch('tipologiaDocumental', query);
  }

  onDropdownClickTipologiaDocumental($event) {
    // this method triggers load of suggestions
    this._sandbox.loadDispatch('tipologiaDocumental');
  }


}
