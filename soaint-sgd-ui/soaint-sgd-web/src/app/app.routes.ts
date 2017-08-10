import {RouterModule, Routes} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';
import {HomeComponent} from 'app/ui/page-components/home/home.component';
import {AuthenticatedGuard, LoginComponent} from 'app/ui/page-components/login/__login.include';
import {RadicarComunicacionesComponent} from './ui/page-components/radicar-comunicaciones/radicar-comunicaciones.component';
import {WorkspaceComponent} from './ui/page-components/workspace/workspace.component';
import {ProcessComponent} from './ui/page-components/process/process.component';
import {AsignarComunicacionesComponent} from './ui/page-components/asignacion-comunicaciones/asignacion-comunicaciones.component';
import {TareaDtoGuard} from './infrastructure/state-management/tareasDTO-state/tareasDTO-guard';
import {DigitalizarDocumentoComponent} from './ui/page-components/digitalizar-documento/digitalizar-documento.component';


export const ROUTES_PATH = {
  task: 'task',
  radicarCofEntrada: 'radicar-comunicaciones',
  digitalizarDocumento: 'digitalizar-documentos',
  dashboard: 'home',
  login: 'login',
  workspace: 'workspace',
  processList: 'process',
  asignacionComunicaciones: 'asignacion-comunicaciones'
};

export const routes: Routes = [
  {path: '', redirectTo: ROUTES_PATH.dashboard, pathMatch: 'full'},
  {path: ROUTES_PATH.login, component: LoginComponent},
  {path: ROUTES_PATH.dashboard, component: HomeComponent, canActivate: [AuthenticatedGuard]},
  {
    path: ROUTES_PATH.task,
    canActivate: [AuthenticatedGuard],
    children: [
      {path: ROUTES_PATH.radicarCofEntrada, component: RadicarComunicacionesComponent, canDeactivate: [TareaDtoGuard]},
      {
        path: ROUTES_PATH.digitalizarDocumento,
        component: DigitalizarDocumentoComponent,
        canActivate: [AuthenticatedGuard],
        canDeactivate: [TareaDtoGuard]
      }
    ]
  },
  {path: ROUTES_PATH.workspace, component: WorkspaceComponent, canActivate: [AuthenticatedGuard]},
  {path: ROUTES_PATH.processList, component: ProcessComponent, canActivate: [AuthenticatedGuard]},
  {path: ROUTES_PATH.asignacionComunicaciones, component: AsignarComunicacionesComponent, canActivate: [AuthenticatedGuard]},
];

export const AppRoutes: ModuleWithProviders = RouterModule.forRoot(routes);

// import { NgModule } from '@angular/core';
// import { RouterModule } from '@angular/router';
//
// @NgModule({
//   imports: [
//     RouterModule.forChild([
//       { path: '', redirectTo: '/home', pathMatch: 'full' },
//       { path: 'lazy', loadChildren: getLazyModule }
//     ])
//   ],
// })
// export class AppRoutingModule { }
