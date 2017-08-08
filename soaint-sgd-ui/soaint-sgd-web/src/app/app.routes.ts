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

export const routes: Routes = [
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'home', component: HomeComponent, canActivate: [AuthenticatedGuard]},
  {
    path: 'task',
    canActivate: [AuthenticatedGuard],
    children: [
      {path: 'radicar-comunicaciones', component: RadicarComunicacionesComponent, canDeactivate: [TareaDtoGuard]},
      {
        path: 'digitalizar-documentos',
        component: DigitalizarDocumentoComponent,
        canActivate: [AuthenticatedGuard],
        canDeactivate: [TareaDtoGuard]
      }
    ]
  },
  {path: 'workspace', component: WorkspaceComponent, canActivate: [AuthenticatedGuard]},
  {path: 'process', component: ProcessComponent, canActivate: [AuthenticatedGuard]},
  {path: 'asignacion-comunicaciones', component: AsignarComunicacionesComponent, canActivate: [AuthenticatedGuard]},
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
