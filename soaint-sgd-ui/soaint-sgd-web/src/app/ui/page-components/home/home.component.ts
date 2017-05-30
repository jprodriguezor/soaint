import { Component, OnInit } from '@angular/core';
import { LoginModel } from "app/ui/page-components/login/login.model";
import { SessionService, WebModel } from "app/infrastructure/web/session.service";
import { ProductsService } from "app/infrastructure/api/products.service";
import { HomeModel } from "app/ui/page-components/home/home.model";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {

  loginModel: LoginModel;
  homeModel: HomeModel;

  constructor(private _session: SessionService) { }

  ngOnInit() {
    this.loginModel = this._session.restoreStatus(WebModel.LOGIN,new LoginModel());
    this.homeModel = new HomeModel();
  }

}
