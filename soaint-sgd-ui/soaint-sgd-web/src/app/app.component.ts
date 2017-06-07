import {Component, AfterViewInit, ElementRef, Renderer, ViewChild, OnInit, OnDestroy} from '@angular/core';
import {MessageBridgeService, MessageType} from 'app/infrastructure/web/message-bridge.service';
import {Subscription} from 'rxjs/Subscription';
import {SessionService, WebModel} from 'app/infrastructure/web/session.service';
// import { LoginModel } from 'app/ui/page-components/login/login.model';
import {LoginSandbox} from 'app/ui/page-components/login/__login.include';
import {Observable} from 'rxjs/Observable';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {

  constructor() {
  }

}
