import { Component, OnInit } from '@angular/core';
import { LoginModel } from 'app/ui/page-components/login/login.model';
import { Router } from '@angular/router';
import { SessionService, WebModel } from 'app/infrastructure/web/session.service';
import { MessageBridgeService, MessageType } from 'app/infrastructure/web/message-bridge.service';
import { AuthenticationService } from 'app/infrastructure/security/authentication.service';
import { Message } from 'primeng/primeng';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

  model: LoginModel;
  msgs: Message[] = [];

  constructor(private _session: SessionService, private _router: Router, private _bridge: MessageBridgeService,
    private _authService: AuthenticationService) { }

  ngOnInit() {
    this.model = this._session.restoreStatus(WebModel.LOGIN, new LoginModel());
  }

  public login(): void {

    this._authService.login(this.model.user).subscribe(data => {
      if (data === true) {
        // login successful
        this.model.loggedin = true;
        this._session.save(WebModel.LOGIN, this.model);
        this._bridge.sendMessage({ type: MessageType.LOGIN_DONE, payload: this.model });
        this._router.navigate(['/home']);

      } else {
        // login failed
        this.showErrorOnLogin()
      }
    },
      (err) => this.showErrorOnLogin());
  }


  private showErrorOnLogin(): void {
    this.msgs = [];
    this.msgs.push({ severity: 'warn', summary: 'Warn Message', detail: 'Invalid credentials' });
  }

}
