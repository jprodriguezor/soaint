import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';

@Component({
  selector: 'app-gestionar-devoluciones',
  templateUrl: './gestionar-devoluciones.component.html',
  styleUrls: ['./gestionar-devoluciones.component.scss'],
})
export class GestionarDevolucionesComponent implements OnInit {

  constructor(private _store: Store<State>, private _constSandbox: ConstanteSandbox, private formBuilder: FormBuilder) {
  }

  form: FormGroup;

  ngOnInit() {
    this.initForm();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'nroRadicado': [null],
    });
  }

}
