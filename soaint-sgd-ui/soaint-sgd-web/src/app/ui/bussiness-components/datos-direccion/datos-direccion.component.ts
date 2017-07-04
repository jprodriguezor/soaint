import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';

import {AbstractControl, FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {getPrefijoCuadranteArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/prefijo-cuadrante-selectors';
import {getTipoViaArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/tipo-via-selectors';
import {getOrientacionArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/orientacion-selectors';
import {getBisArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/bis-selectors';


@Component({
  selector: 'app-datos-direccion',
  templateUrl: './datos-direccion.component.html',
})
export class DatosDireccionComponent implements OnInit {

  form: FormGroup;
  display = false;
  canAgregate = false;

  prefijoCuadranteSuggestions$: Observable<ConstanteDTO[]>;
  tipoViaSuggestions$: Observable<ConstanteDTO[]>;
  orientacionSuggestions$: Observable<ConstanteDTO[]>;
  bisSuggestons$: Observable<ConstanteDTO[]>;

  direcciones: Array<{direccion: string}> = [];

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

    if (tipoVia.value) {
      direccion += tipoVia.value.nombre;
      tipoVia.reset();
    }
    if (noViaPrincipal.value) {
      direccion += ' ' + noViaPrincipal.value;
      noViaPrincipal.reset();
    }
    if (prefijoCuadrante.value) {
      direccion += ' ' + prefijoCuadrante.value.nombre;
      prefijoCuadrante.reset();
    }
    if (bis.value) {
      direccion += ' ' + bis.value.nombre;
      bis.reset();
    }
    if (orientacion.value) {
      direccion += ' ' + orientacion.value.nombre;
      orientacion.reset();
    }
    if (noVia.value) {
      direccion += ' ' + noVia.value;
      noVia.reset();
    }
    if (prefijoCuadrante_se.value) {
      direccion += ' ' + prefijoCuadrante_se.value.nombre;
      prefijoCuadrante_se.reset();
    }
    if (placa.value) {
      direccion += ' ' + placa.value;
      placa.reset();
    }
    if (orientacion_se.value) {
      direccion += ' ' + orientacion_se.value.nombre;
      orientacion_se.reset();
    }

    const insert = [{direccion: direccion}];
    this.direcciones = [...insert, ...this.direcciones];
  }

  ngOnInit(): void {
    this.prefijoCuadranteSuggestions$ = this._store.select(getPrefijoCuadranteArrayData);
    this.tipoViaSuggestions$ = this._store.select(getTipoViaArrayData);
    this.orientacionSuggestions$ = this._store.select(getOrientacionArrayData);
    this.bisSuggestons$ = this._store.select(getBisArrayData);
  }

  initForm() {
    this.form = this.formBuilder.group({
      'tipoVia': [null],
      'noViaPrincipal': [null],
      'prefijoCuadrante': [null],
      'bis': [null],
      'orientacion': [null],
      'noVia': [null],
      'prefijoCuadrante_se': [null],
      'placa': [null],
      'orientacion_se': [null],
    });
  }

}
