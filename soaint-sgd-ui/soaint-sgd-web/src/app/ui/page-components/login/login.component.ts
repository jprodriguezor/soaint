import {Component, OnInit, ChangeDetectionStrategy} from '@angular/core';
import {SessionService, WebModel} from 'app/infrastructure/web/session.service';

import {LoginSandbox} from './redux-state/login-sandbox';
import {Observable} from 'rxjs/Observable';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import * as actions from './redux-state/login-actions';


@Component({
  selector: 'app-login',
  // providers: [LoginSandbox],
  templateUrl: './login.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginComponent implements OnInit {

  public loading$: Observable<boolean>;
  public error$: Observable<string>;
  public form: FormGroup;

  constructor(private _sandbox: LoginSandbox,
              private _formBuilder: FormBuilder
  ) {}

  ngOnInit() {

    this.form = this._formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

    this.loading$ = this._sandbox.selectorLoading();
    this.error$ = this._sandbox.selectorError();

  }

  /**
   * Submit the authentication form.
   * @method submit
   */
  public submit() {
    // get email and password values
    const username: string = this.form.get('username').value;
    const password: string = this.form.get('password').value;

    // trim values
    username.trim();
    password.trim();

    // set payload
    const payload = {
      username: username,
      password: password
    };

    this._sandbox.loginDispatch(payload);
  }


}
