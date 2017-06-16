import {LoginComponent} from './login/login.component';
import {HomeComponent} from './home/home.component';
import {WorkspaceComponent} from './workspace/workspace.component';
// import {ProductosComponent} from './productos/productos.component';
import {RadicarComunicacionesComponent} from './radicar-comunicaciones/radicar-comunicaciones.component';
import {ProcessComponent} from './process/process.component';

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
  WorkspaceComponent,
  ProcessComponent
];

export * from './__page-providers.include';
