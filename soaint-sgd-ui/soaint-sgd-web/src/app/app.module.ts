import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';
import {AppRoutes} from './app.routes';
import 'rxjs/add/operator/toPromise';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import { NgxBarcodeModule } from 'ngx-barcode';

// APP COMPONENTS
import {AppComponent} from './app.component';
import {UI_COMPONENTS, PAGE_COMPONENTS_PROVIDERS, LAYOUT_COMPONENTS_PROVIDERS, BUSSINESS_COMPONENTS_PROVIDERS} from 'app/ui/__ui.include';

// APP MODULES
// import { PAGE_MODULES } from './ui/page-components/__page-components.include';

// third party libs | components | modules
import {PRIMENG_MODULES, PrintDirective, PIPES} from './shared/__shared.include';
// import {PRIMENG_MODULES} from './shared/primeng/__primeng';
// import {PrintDirective} from './shared/directives/print.directive';
import {LocalStorageModule} from 'angular-2-local-storage';

// APP SERVICES
import {INFRASTRUCTURE_SERVICES, API_SERVICES, EFFECTS_MODULES} from './infrastructure/__infrastructure.include';

// Redux Store and Colaterals
import {StoreModule} from '@ngrx/store';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {ReduxStore} from './infrastructure/redux-store/__redux-states';
import { RouterStoreModule } from '@ngrx/router-store';


@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutes,
    HttpModule,
    BrowserAnimationsModule,
    NgxChartsModule,
    NgxBarcodeModule,
    // PrimeNG Modules => view components
    ...PRIMENG_MODULES,
    // third party libs
    LocalStorageModule.withConfig({
      prefix: 'my-app',
      //  storageType: 'localStorage'
      storageType: 'sessionStorage'
    }),
    /**
     * StoreModule.provideStore is imported once in the root module, accepting a reducer
     * function or object map of reducer functions. If passed an object of
     * reducers, combineReducers will be run creating your application
     * meta-reducer. This returns all providers for an @ngrx/store
     * based application.
     */
    StoreModule.provideStore(ReduxStore, {
      router: {
        path: window.location.pathname + window.location.search
      }
    }),
    /**
     * @ngrx/router-store keeps router state up-to-date in the store and uses
     * the store as the single source of truth for the router's state.
     */
    RouterStoreModule.connectRouter(),
    /**
     * Store devtools instrument the store retaining past versions of state
     * and recalculating new states. This enables powerful time-travel
     * debugging.
     *
     * To use the debugger, install the Redux Devtools extension for either
     * Chrome or Firefox
     *
     * See: https://github.com/zalmoxisus/redux-devtools-extension
     */
    StoreDevtoolsModule.instrumentOnlyWithExtension(),
    /**
     * EffectsModule.run() sets up the effects class to be initialized
     * immediately when the application starts.
     *
     * See: https://github.com/ngrx/effects/blob/master/docs/api.md#run
     */
    ...EFFECTS_MODULES


  ],
  declarations: [
    AppComponent,
    PrintDirective,
    ...UI_COMPONENTS,
    ...PIPES
  ],
  providers: [
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    ...INFRASTRUCTURE_SERVICES,
    ...API_SERVICES,
    ...PAGE_COMPONENTS_PROVIDERS,
    ...LAYOUT_COMPONENTS_PROVIDERS,
    ...BUSSINESS_COMPONENTS_PROVIDERS
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
