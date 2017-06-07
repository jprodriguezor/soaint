import {RouterModule, Routes} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';
import {HomeComponent} from 'app/ui/page-components/home/home.component';
import {LoginComponent, AuthenticatedGuard} from 'app/ui/page-components/login/__login.include';
// import {AuthGuard} from 'app/infrastructure/security/auth-guard';
// import {RadicarComunicacionesComponent} from './ui/page-components/radicar-comunicaciones/radicar-comunicaciones.component';

export const routes: Routes = [
    {path: '', redirectTo: 'home', pathMatch: 'full'},
    {path: 'login', component: LoginComponent},
    {path: 'home', component: HomeComponent, canActivate: [AuthenticatedGuard]},
    // {path: 'radicar-comunicaciones', component: RadicarComunicacionesComponent, canActivate: [AuthenticatedGuard]}
  ]
;

export const AppRoutes: ModuleWithProviders = RouterModule.forRoot(routes);
