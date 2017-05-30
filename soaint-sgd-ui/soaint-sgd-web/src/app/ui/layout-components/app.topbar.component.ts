import { Component, Inject, forwardRef, OnInit, OnDestroy } from '@angular/core';
import { AppComponent } from "app/app.component";
import { LoginModel } from "app/ui/page-components/login/login.model";
import { MessageBridgeService, MessageType } from "app/infrastructure/web/message-bridge.service";
import { Subscription } from "rxjs/Subscription";
import { SessionService, WebModel } from "app/infrastructure/web/session.service";

@Component({
    selector: 'app-topbar',
    templateUrl: './app.topbar.component.html'
})
export class AppTopBar implements OnInit, OnDestroy  {
    
    loginModel: LoginModel;
    subscription: Subscription;

    constructor(private _session: SessionService, private _bridge: MessageBridgeService, @Inject(forwardRef(() => AppComponent)) public app: AppComponent) { }

    ngOnInit(): void {

        // restore model just if it already exists
        this.loginModel = this._session.restoreStatus(WebModel.LOGIN,new LoginModel());
        this.subscription = this._bridge.getMessage().subscribe(message => { 
            
            if( message.type === MessageType.LOGIN_DONE ){
                this.loginModel = message.payload;
            }

            if( message.type === MessageType.LOGOUT_DONE ){
                this.loginModel = new LoginModel();
            }
        });
    }

    ngOnDestroy(): void {
        this.subscription.unsubscribe();
    }
}