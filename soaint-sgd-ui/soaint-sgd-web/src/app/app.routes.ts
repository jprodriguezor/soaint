import {RouterModule, Routes} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';
import {HomeComponent} from 'app/ui/page-components/home/home.component';
import {LoginComponent, AuthenticatedGuard} from 'app/ui/page-components/login/__login.include';
import {RadicarComunicacionesComponent} from './ui/page-components/radicar-comunicaciones/radicar-comunicaciones.component';
import {WorkspaceComponent} from './ui/page-components/workspace/workspace.component';

export const routes: Routes = [
    {path: '', redirectTo: 'home', pathMatch: 'full'},
    {path: 'login', component: LoginComponent},
    {path: 'home', component: HomeComponent, canActivate: [AuthenticatedGuard]},
    {path: 'radicar-comunicaciones', component: RadicarComunicacionesComponent, canActivate: [AuthenticatedGuard]},
    {path: 'workspace', component: WorkspaceComponent, canActivate: [AuthenticatedGuard]},
  ]
;

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
