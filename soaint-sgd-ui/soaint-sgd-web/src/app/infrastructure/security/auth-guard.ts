import {Injectable} from '@angular/core';
import {Router, CanActivate} from '@angular/router';
// import {LoginModel} from 'app/ui/page-components/loginBackup/login.model';
import {SessionService, WebModel} from 'app/infrastructure/utils/session.service';

@Injectable()
export class AuthGuard implements CanActivate {
  //
  // loginModel: LoginModel;

  constructor(private _router: Router, private _session: SessionService) {
  }

  canActivate() {
    // this.loginModel = this._session.restoreStatus(WebModel.LOGIN, new LoginModel());

    // if (this.loginModel.loggedin) {
    //   console.log('user logged successfully');
    //   // logged in so return true
    //   return true;
    // }

    console.log('user not logged');
    // not logged in so redirect to login page
    this._router.navigate(['/login']);
    return false;
  }
}
