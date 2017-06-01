import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';
import {AppRoutes} from './app.routes';
import 'rxjs/add/operator/toPromise';

// APP COMPONENTS
import {AppComponent} from './app.component';
import {UI_COMPONENTS} from 'app/ui/__ui.include';

// third party libs | components | modules
import { PRIMENG_MODULES } from './shared/primeng/__primeng';
import {LocalStorageModule} from 'angular-2-local-storage';

// APP SERVICES
import {INFRASTRUCTURE_SERVICES, API_SERVICES} from './infrastructure/__infrastructure.include';


@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutes,
    HttpModule,
    BrowserAnimationsModule,
    // PrimeNG Modules => view components
    ...PRIMENG_MODULES,
    // third party libs
    LocalStorageModule.withConfig({
      prefix: 'my-app',
      //  storageType: 'localStorage'
      storageType: 'sessionStorage'
    })
  ],
  declarations: [
    AppComponent,
    ...UI_COMPONENTS
  ],
  providers: [
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    ...INFRASTRUCTURE_SERVICES,
    ...API_SERVICES
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
