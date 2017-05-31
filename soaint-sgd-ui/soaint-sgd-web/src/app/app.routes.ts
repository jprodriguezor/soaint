import {RouterModule, Routes} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';
import {HomeComponent} from 'app/ui/page-components/home/home.component';
import {LoginComponent} from 'app/ui/page-components/login/login.component';
import {AuthGuard} from 'app/infrastructure/security/auth-guard';
import {ProductosComponent} from 'app/ui/page-components/productos/productos.component';
import {RadicarComunicacionesComponent} from './ui/page-components/radicar-comunicaciones/radicar-comunicaciones.component';

export const routes: Routes = [
    {path: '', redirectTo: 'home', pathMatch: 'full'},
    {path: 'login', component: LoginComponent},
    {path: 'home', component: HomeComponent, canActivate: [AuthGuard]},
    {path: 'productos', component: ProductosComponent, canActivate: [AuthGuard]},
    {path: 'radicar-comunicaciones', component: RadicarComunicacionesComponent}
  ]
;

export const AppRoutes: ModuleWithProviders = RouterModule.forRoot(routes);
