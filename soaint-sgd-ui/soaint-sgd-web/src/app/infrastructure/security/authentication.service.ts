import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import 'rxjs/add/operator/map'
import { SessionService, WebModel } from 'app/infrastructure/utils/session.service';
import { Observable } from 'rxjs/Observable';
import { environment } from 'environments/environment';
import { Usuario } from 'app/domain/usuario';

@Injectable()
export class AuthenticationService {

  constructor(private _http: Http, private _session: SessionService) { }

  public login(user: Usuario): Observable<boolean> {

    let loggedin: boolean = false;

    return this._http.post(environment.security_endpoint + '/login', user).map(
      (response: Response) => {
        let token = response.json() && response.json().token;
        if (token) {
          // store jwt token in local storage to keep user logged in between page refreshes
          this._session.save(WebModel.SECURITY_TOKEN, token);
          // return true to indicate successful login
          return true;
        } else {
          // return false to indicate failed login
          return false;
        }
      });
  }

}
