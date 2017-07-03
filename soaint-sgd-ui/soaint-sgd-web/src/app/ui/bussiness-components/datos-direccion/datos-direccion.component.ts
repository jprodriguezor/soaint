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
  tipoViaControl: AbstractControl;
  noViaPrincipalControl: AbstractControl;
  prefijoCuadranteControl: AbstractControl;
  orientacionControl: AbstractControl;
  bisControl: AbstractControl;
  noViaControl: AbstractControl;
  placaControl: AbstractControl;

  prefijoCuadranteSuggestions$: Observable<ConstanteDTO[]>;
  tipoViaSuggestions$: Observable<ConstanteDTO[]>;
  orientacionSuggestions$: Observable<ConstanteDTO[]>;
  bisSuggestons$: Observable<ConstanteDTO[]>;

  @Input()
  display = false;

  @Output()
  dialogHide: EventEmitter<any> = new EventEmitter<any>();

  constructor(private _store: Store<State>,
              private _constanteSandbox: ConstanteSandbox,
              private formBuilder: FormBuilder) {
    this.initForm();
  }

  hideDialog() {
    this.display = false;
  }

  addAddress() {
    this.hideDialog();
    this.dialogHide.emit(this.form.value);
  }

  ngOnInit(): void {
    this.prefijoCuadranteSuggestions$ = this._store.select(getPrefijoCuadranteArrayData);
    this.tipoViaSuggestions$ = this._store.select(getTipoViaArrayData);
    this.orientacionSuggestions$ = this._store.select(getOrientacionArrayData);
    this.bisSuggestons$ = this._store.select(getBisArrayData);
  }

  initForm() {
    this.tipoViaControl = new FormControl(null);
    this.noViaPrincipalControl = new FormControl(null);
    this.prefijoCuadranteControl = new FormControl(null);
    this.orientacionControl = new FormControl(null);
    this.bisControl = new FormControl(null);
    this.noViaControl = new FormControl(null);
    this.placaControl = new FormControl(null);

    this.form = this.formBuilder.group({
      'tipoVia': this.tipoViaControl,
      'noViaPrincipal': this.noViaPrincipalControl,
      'prefijoCuadrante': this.prefijoCuadranteControl,
      'orientacion': this.orientacionControl,
      'bis': this.bisControl,
      'noVia': this.noViaControl,
      'placa': this.placaControl
    });
  }


  onFilterTipoVia($event) {
    this._constanteSandbox.filterDispatch('tipoVia', $event.query);
  }

  onDropdownClickTipoVia($event) {
    // this method triggers load of suggestions
    this._constanteSandbox.loadDispatch('tipoVia');
  }

  onFilterPrefijoCuadrante($event) {
    const query = $event.query;
    this._constanteSandbox.filterDispatch('prefijoCuadrante', query);
  }

  onDropdownClickPrefijoCuadrante($event) {
    // this method triggers load of suggestions
    this._constanteSandbox.loadDispatch('prefijoCuadrante');
  }

  onFilterOrientacion($event) {
    const query = $event.query;
    this._constanteSandbox.filterDispatch('orientacion', query);
  }

  onDropdownClickOrientacion($event) {
    // this method triggers load of suggestions
    this._constanteSandbox.loadDispatch('orientacion');
  }

  onFilterBis($event) {
    const query = $event.query;
    this._constanteSandbox.filterDispatch('bis', query);
  }

  onDropdownClickBis($event) {
    // this method triggers load of suggestions
    this._constanteSandbox.loadDispatch('bis');
  }

}
