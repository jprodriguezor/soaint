import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';
import {AppRoutes} from './app.routes';
import 'rxjs/add/operator/toPromise';

import {
  AccordionModule,
  AutoCompleteModule,
  BreadcrumbModule,
  ButtonModule,
  CalendarModule,
  CarouselModule,
  ChartModule,
  CheckboxModule,
  ChipsModule,
  CodeHighlighterModule,
  ConfirmDialogModule,
  ContextMenuModule,
  DataGridModule,
  DataListModule,
  DataScrollerModule,
  DataTableModule,
  DialogModule,
  DragDropModule,
  DropdownModule,
  EditorModule,
  FieldsetModule,
  FileUploadModule,
  GalleriaModule,
  GMapModule,
  GrowlModule,
  InputMaskModule,
  InputSwitchModule,
  InputTextareaModule,
  InputTextModule,
  LightboxModule,
  ListboxModule,
  MegaMenuModule,
  MenubarModule,
  MenuModule,
  MessagesModule,
  MultiSelectModule,
  OrderListModule,
  OverlayPanelModule,
  PaginatorModule,
  PanelMenuModule,
  PanelModule,
  PasswordModule,
  PickListModule,
  ProgressBarModule,
  RadioButtonModule,
  RatingModule,
  ScheduleModule,
  SelectButtonModule,
  SharedModule,
  SlideMenuModule,
  SliderModule,
  SpinnerModule,
  SplitButtonModule,
  StepsModule,
  TabMenuModule,
  TabViewModule,
  TerminalModule,
  TieredMenuModule,
  ToggleButtonModule,
  ToolbarModule,
  TooltipModule,
  TreeModule,
  TreeTableModule
} from 'primeng/primeng';

import {AppComponent} from './app.component';
import {InlineProfileComponent} from 'app/ui/layout-components/app.profile.component';
import {AppFooter} from 'app/ui/layout-components/app.footer.component';
import {AppTopBar} from 'app/ui/layout-components/app.topbar.component';
import {AppMenuComponent, AppSubMenu} from 'app/ui/layout-components/app.menu.component';
import {HomeComponent} from './ui/page-components/home/home.component';
import {LocalStorageModule} from 'angular-2-local-storage';
import {LoginComponent} from './ui/page-components/login/login.component';
import {SessionService} from 'app/infrastructure/web/session.service';
import {MessageBridgeService} from 'app/infrastructure/web/message-bridge.service';
import {AuthenticationService} from 'app/infrastructure/security/authentication.service';
import {AuthGuard} from 'app/infrastructure/security/auth-guard';
import {ProductsService} from 'app/infrastructure/api/products.service';
import {ProductosComponent} from './ui/page-components/productos/productos.component';
import {RadicarComunicacionesComponent} from './ui/page-components/radicar-comunicaciones/radicar-comunicaciones.component';
import {DatosGeneralesComponent} from './ui/bussiness-components/datos-generales/datos-generales.component';
import {TipoComunicacionService} from './infrastructure/api/tipo.comunicacion.service';
import {MedioRecepcionService} from './infrastructure/api/medio.recepcion.service';
import {TipologiaDocumentalService} from './infrastructure/api/tipologia.documental.service';
import {UnidadTiempoService} from './infrastructure/api/unidad.tiempo.service';
import {DatosDestinatarioComponent} from './ui/bussiness-components/datos-destinatario/datos-destinatario.component';
import {DatosRemitenteComponent} from './ui/bussiness-components/datos-remitente/datos-remitente.component';
import {TipoDestinatarioService} from './infrastructure/api/tipo.destinatario.service';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutes,
    HttpModule,
    BrowserAnimationsModule,
    AccordionModule,
    AutoCompleteModule,
    BreadcrumbModule,
    ButtonModule,
    CalendarModule,
    CarouselModule,
    ChartModule,
    CheckboxModule,
    ChipsModule,
    CodeHighlighterModule,
    ConfirmDialogModule,
    SharedModule,
    ContextMenuModule,
    DataGridModule,
    DataListModule,
    DataScrollerModule,
    DataTableModule,
    DialogModule,
    DragDropModule,
    DropdownModule,
    EditorModule,
    FieldsetModule,
    FileUploadModule,
    GalleriaModule,
    GMapModule,
    GrowlModule,
    InputMaskModule,
    InputSwitchModule,
    InputTextModule,
    InputTextareaModule,
    LightboxModule,
    ListboxModule,
    MegaMenuModule,
    MenuModule,
    MenubarModule,
    MessagesModule,
    MultiSelectModule,
    OrderListModule,
    OverlayPanelModule,
    PaginatorModule,
    PanelModule,
    PanelMenuModule,
    PasswordModule,
    PickListModule,
    ProgressBarModule,
    RadioButtonModule,
    RatingModule,
    ScheduleModule,
    SelectButtonModule,
    SlideMenuModule,
    SliderModule,
    SpinnerModule,
    SplitButtonModule,
    StepsModule,
    TabMenuModule,
    TabViewModule,
    TerminalModule,
    TieredMenuModule,
    ToggleButtonModule,
    ToolbarModule,
    TooltipModule,
    TreeModule,
    TreeTableModule,
    LocalStorageModule.withConfig({
      prefix: 'my-app',
      //  storageType: 'localStorage'
      storageType: 'sessionStorage'
    })
  ],
  declarations: [
    AppComponent,
    AppMenuComponent,
    AppSubMenu,
    AppTopBar,
    AppFooter,
    InlineProfileComponent,
    HomeComponent,
    LoginComponent,
    ProductosComponent,
    DatosGeneralesComponent,
    DatosDestinatarioComponent,
    DatosRemitenteComponent,
    RadicarComunicacionesComponent
  ],
  providers: [
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    SessionService, MessageBridgeService, AuthenticationService,
    AuthGuard, ProductsService, TipoComunicacionService, MedioRecepcionService,
    TipologiaDocumentalService, UnidadTiempoService, TipoDestinatarioService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
