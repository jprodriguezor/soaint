import {Component, Inject, OnInit, Input, Output, EventEmitter, forwardRef} from '@angular/core';
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
