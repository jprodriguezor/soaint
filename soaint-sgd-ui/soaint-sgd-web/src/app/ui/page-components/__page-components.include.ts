import {LoginComponent} from './login/login.component';
import {HomeComponent} from './home/home.component';
import {WorkspaceComponent} from './workspace/workspace.component';
import {DigitalizarDocumentoComponent} from './digitalizar-documento/digitalizar-documento.component';
import {RadicarComunicacionesComponent} from './radicar-comunicaciones/radicar-comunicaciones.component';
import {ProcessComponent} from './process/process.component';
import {DatosDireccionComponent} from '../bussiness-components/datos-direccion/datos-direccion.component';
import {AsignarComunicacionesComponent} from './asignacion-comunicaciones/asignacion-comunicaciones.component';
import {PopupJustificacionComponent} from '../bussiness-components/popup-justificacion/popup-justificacion.component';
import {PopupAgregarObservacionesComponent} from '../bussiness-components/popup-agregar-observaciones/popup-agregar-observaciones.component';
import {PopupRejectComponent} from '../bussiness-components/popup-reject/popup-reject.component';
import {GestionarComunicacionComponent} from '../bussiness-components/gestionar-comunicacion/gestionar-comunicacion.component';
import {RadicarSalidaComponent} from './radicacion-salida/radicar-salida.component';
import {CargaMasivaUploaderComponent} from './carga-masiva/uploader/carga-masiva-uploader.component';
import {CargaMasivaDetailsComponent} from "./carga-masiva/details/carga-masiva-details.component";

/**
 * All state updates are handled through dispatched actions in 'container'
 * components. This provides a clear, reproducible history of state
 * updates and user interaction through the life of our
 * application.
 *
 * Note: Container components are also reusable. Whether or not
 * a component is a presentation component or a container
 * component is an implementation detail.
 */
export const PAGE_COMPONENTS = [
  HomeComponent,
  LoginComponent,
  // ProductosComponent,
  RadicarComunicacionesComponent,
  DigitalizarDocumentoComponent,
  WorkspaceComponent,
  ProcessComponent,
  DatosDireccionComponent,
  AsignarComunicacionesComponent,
  PopupJustificacionComponent,
  PopupAgregarObservacionesComponent,
  PopupRejectComponent,
  GestionarComunicacionComponent,
  RadicarSalidaComponent,
  CargaMasivaUploaderComponent,
  CargaMasivaDetailsComponent
];

export * from './__page-providers.include';
