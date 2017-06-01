
import {LoginComponent} from './page-components/login/login.component';
import {HomeComponent} from './page-components/home/home.component';
import {ProductosComponent} from './page-components/productos/productos.component';
import {RadicarComunicacionesComponent} from './page-components/radicar-comunicaciones/radicar-comunicaciones.component';
import {DatosGeneralesComponent} from './bussiness-components/datos-generales/datos-generales.component';
import {DatosDestinatarioComponent} from './bussiness-components/datos-destinatario/datos-destinatario.component';
import {DatosRemitenteComponent} from './bussiness-components/datos-remitente/datos-remitente.component';
import {AppFooter} from './layout-components/app.footer.component';
import {AppMenuComponent, AppSubMenu} from './layout-components/app.menu.component';
import {InlineProfileComponent} from './layout-components/app.profile.component';
import {AppTopBar} from './layout-components/app.topbar.component';

const LAYOUT_COMPONENTS = [
  AppFooter,
  AppMenuComponent,
  AppSubMenu,
  InlineProfileComponent,
  AppTopBar,
  HomeComponent
];

const BUSSINESS_COMPONENTS = [
  DatosGeneralesComponent,
  DatosDestinatarioComponent,
  DatosRemitenteComponent
];

const PAGE_COMPONENTS = [
  LoginComponent,
  ProductosComponent,
  RadicarComunicacionesComponent,
];
export const UI_COMPONENTS = [
  ...LAYOUT_COMPONENTS,
  ...BUSSINESS_COMPONENTS,
  ...PAGE_COMPONENTS
];

