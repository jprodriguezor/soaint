import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';

import {AbstractControl, FormBuilder, FormControl, FormGroup, Validator, Validators} from '@angular/forms';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {getPrefijoCuadranteArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/prefijo-cuadrante-selectors';
import {getTipoViaArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/tipo-via-selectors';
import {getOrientacionArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/orientacion-selectors';
import {getBisArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/bis-selectors';
import {getTipoComplementoArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-complemento-selectors';


@Component({
  selector: 'app-datos-direccion',
  templateUrl: './datos-direccion.component.html',
})
export class DatosDireccionComponent implements OnInit {

  form: FormGroup;
  display = false;
  canAgregate = false;
  editable = false;

  prefijoCuadranteSuggestions$: Observable<ConstanteDTO[]>;
  tipoViaSuggestions$: Observable<ConstanteDTO[]>;
  orientacionSuggestions$: Observable<ConstanteDTO[]>;
  bisSuggestons$: Observable<ConstanteDTO[]>;
  tipoComplementoSuggestions$: Observable<ConstanteDTO[]>;

  direcciones: Array<any> = [];

  @Output()
  onClose: EventEmitter<any> = new EventEmitter<any>();

  @Output()
  onComplete: EventEmitter<any> = new EventEmitter();

  constructor(private _store: Store<State>,
              private _constanteSandbox: ConstanteSandbox,
              private formBuilder: FormBuilder) {
    this.initForm();
  }

  show() {
    this.display = true;
  }

  hide() {
    this.display = false;
    this.direcciones = [];
    this.form.reset();
  }

  completeRegistration() {
    this.onComplete.emit(this.direcciones);
    this.hide();
  }

  addDireccion() {
    let direccion = '';
    const tipoVia = this.form.get('tipoVia');
    const noViaPrincipal = this.form.get('noViaPrincipal');
    const prefijoCuadrante = this.form.get('prefijoCuadrante');
    const bis = this.form.get('bis');
    const orientacion = this.form.get('orientacion');
    const noVia = this.form.get('noVia');
    const prefijoCuadrante_se = this.form.get('prefijoCuadrante_se');
    const placa = this.form.get('placa');
    const orientacion_se = this.form.get('orientacion_se');
    const tipoComplemento = this.form.get('complementoTipo');
    const complementoAdicional = this.form.get('complementoAdicional');

    const value = {};

    if (tipoVia.value) {
      direccion += tipoVia.value.nombre;
      value['tipoVia'] = tipoVia.value;
      tipoVia.reset();
    }
    if (noViaPrincipal.value) {
      direccion += ' ' + noViaPrincipal.value;
      value['noViaPrincipal'] = noViaPrincipal.value;
      noViaPrincipal.reset();
    }
    if (prefijoCuadrante.value) {
      direccion += ' ' + prefijoCuadrante.value.nombre;
      value['prefijoCuadrante'] = prefijoCuadrante.value;
      prefijoCuadrante.reset();
    }
    if (bis.value) {
      direccion += ' ' + bis.value.nombre;
      value['bis'] = bis;
      bis.reset();
    }
    if (orientacion.value) {
      direccion += ' ' + orientacion.value.nombre;
      value['orientacion'] = orientacion.value;
      orientacion.reset();
    }
    if (noVia.value) {
      direccion += ' ' + noVia.value;
      value['noVia'] = noVia.value;
      noVia.reset();
    }
    if (prefijoCuadrante_se.value) {
      direccion += ' ' + prefijoCuadrante_se.value.nombre;
      prefijoCuadrante_se.reset();
    }
    if (placa.value) {
      direccion += ' ' + placa.value;
      value['placa'] = placa.value;
      placa.reset();
    }
    if (orientacion_se.value) {
      direccion += ' ' + orientacion_se.value.nombre;
      orientacion_se.reset();
    }
    if (tipoComplemento.value) {
      direccion += ' ' + tipoComplemento.value.nombre;
      tipoComplemento.reset();
    }
    if (complementoAdicional.value) {
      direccion += ' ' + complementoAdicional.value;
      complementoAdicional.reset();
    }
    value['direccion'] = direccion;
    const insert = [value];
    this.direcciones = [...insert, ...this.direcciones];
  }

  ngOnInit(): void {
    this.prefijoCuadranteSuggestions$ = this._store.select(getPrefijoCuadranteArrayData);
    this.tipoViaSuggestions$ = this._store.select(getTipoViaArrayData);
    this.orientacionSuggestions$ = this._store.select(getOrientacionArrayData);
    this.bisSuggestons$ = this._store.select(getBisArrayData);
    this.tipoComplementoSuggestions$ = this._store.select(getTipoComplementoArrayData);
  }

  initForm() {
    this.form = this.formBuilder.group({
      'tipoVia': [{value: null, editable: !this.editable}, Validators.required],
      'noViaPrincipal': [{value: null, editable: !this.editable}, Validators.required],
      'prefijoCuadrante': [null],
      'bis': [null],
      'orientacion': [null],
      'noVia': [{value: null, editable: !this.editable}, Validators.required],
      'prefijoCuadrante_se': [null],
      'placa': [{value: null, editable: !this.editable}, Validators.required],
      'orientacion_se': [null],
      'complementoTipo': [null],
      'complementoAdicional': [null],
    });
  }

}
