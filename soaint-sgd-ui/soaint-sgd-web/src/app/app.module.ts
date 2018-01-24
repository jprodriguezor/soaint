import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';
import {AppRoutes} from './app.routes';
import 'rxjs/add/operator/toPromise';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import {NgxBarcodeModule} from 'ngx-barcode';
import {ToastrModule} from 'ngx-toastr';
// APP COMPONENTS
import {AppComponent} from './app.component';
import {
  BUSSINESS_COMPONENTS_PROVIDERS,
  LAYOUT_COMPONENTS_PROVIDERS,
  PAGE_COMPONENTS_PROVIDERS,
  UI_COMPONENTS
} from 'app/ui/__ui.include';
// APP MODULES
import {ConfirmationService, PIPES, PRIMENG_MODULES,} from './shared/__shared.include';
// import {PRIMENG_MODULES} from './shared/primeng/__primeng';
// import {PrintDirective} from './shared/directives/print.directive';
import {LocalStorageModule} from 'angular-2-local-storage';
// APP SERVICES
import {API_SERVICES, EFFECTS_MODULES, INFRASTRUCTURE_SERVICES} from './infrastructure/__infrastructure.include';
// Redux Store and Colaterals
import {StoreModule} from '@ngrx/store';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {ReduxStore} from './infrastructure/redux-store/__redux-config';
import {RouterStoreModule} from '@ngrx/router-store';
import {PdfViewerComponent} from 'ng2-pdf-viewer';
import {DistribucionFisicaComponent} from './ui/page-components/distribucion-fisica/distribucion-fisica.component';
import {CargarPlanillasComponent} from './ui/page-components/cargar-planillas/cargar-planillas.component';
import {EditarPlanillaComponent} from './ui/bussiness-components/editar-planilla/editar-planilla.component';
import {PlanillaGeneradaComponent} from './ui/bussiness-components/planilla-generada/planilla-generada.component';
import {DIRECTIVES} from './shared/directives/__directives.include';
import {PIPES_AS_PROVIDERS} from './shared/pipes/__pipes.include';
import {customHttpProvider} from './shared/interceptors/http.provider';
import { OrderModule } from 'ngx-order-pipe';
import { SingleUploadComponent } from './ui/layout-components/presentation/single-upload/single-upload.component';
import { SecurityRoleComponent } from './ui/page-components/security-role/security-role.component';
import { FuncionarioListComponent } from './ui/page-components/security-role/components/funcionario-list/funcionario-list.component';
import { ControlMessagesComponent } from './ui/page-components/security-role/components/control-messages/control-messages.component';
import { UnidadesDocumentalesComponent } from './ui/page-components/unidades-documentales/unidades-documentales.component';
import { GestionarDevolucionesComponent } from './ui/page-components/gestionar-devoluciones/gestionar-devoluciones.component';
import { CorregirRadicacionComponent } from './ui/page-components/corregir-radicacion/corregir-radicacion.component';
import { DocumentoEcmComponent } from './ui/bussiness-components/documento-ecm/documento-ecm.component';


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
    OrderModule,
    ToastrModule.forRoot({
      closeButton: true, // show close button
      timeOut: 3000, // time to live
      enableHtml: true, // allow html in message. (UNSAFE)
      extendedTimeOut: 1000, // time to close after a user hovers over toast
      progressBar: true, // show progress bar
      newestOnTop: true, // new toast placement
      // preventDuplicates: true,
      // toastClass: string = 'toast'; // class on toast
      // positionClass: string = 'toast-top-right'; // class on toast
      // titleClass: string = 'toast-title'; // class inside toast on title
      // messageClass: string = 'toast-message';
    }),
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
    PdfViewerComponent,
    ...DIRECTIVES,
    ...UI_COMPONENTS,
    ...PIPES,
    DistribucionFisicaComponent,
    CargarPlanillasComponent,
    EditarPlanillaComponent,
    PlanillaGeneradaComponent,
    SingleUploadComponent,
    SecurityRoleComponent,
    FuncionarioListComponent,
    ControlMessagesComponent,
    UnidadesDocumentalesComponent,
    GestionarDevolucionesComponent,
    CorregirRadicacionComponent,
    DocumentoEcmComponent
  ],
  providers: [
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    ...INFRASTRUCTURE_SERVICES,
    ...API_SERVICES,
    ...PAGE_COMPONENTS_PROVIDERS,
    ...LAYOUT_COMPONENTS_PROVIDERS,
    ...BUSSINESS_COMPONENTS_PROVIDERS,
    ...PIPES_AS_PROVIDERS,
    ConfirmationService,
    customHttpProvider()
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
