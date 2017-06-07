import {Component, Inject, OnInit, Input, Output, EventEmitter, forwardRef} from '@angular/core';
import {AppComponent} from 'app/app.component';
import {MessageBridgeService, MessageType} from 'app/infrastructure/web/message-bridge.service';
import {Subscription} from 'rxjs/Subscription';
import {SessionService, WebModel} from 'app/infrastructure/web/session.service';
import {AdminLayoutComponent} from '../../container/admin-layout/admin-layout.component';

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html'
})
export class TopBarComponent implements OnInit {

  @Input() isAuthenticated: boolean;


  constructor(@Inject(forwardRef(() => AdminLayoutComponent)) public app: AdminLayoutComponent) {
  }

  ngOnInit(): void {

  }

}
