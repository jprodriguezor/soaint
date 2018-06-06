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
import {CargaMasivaComponent} from './ui/page-components/carga-masiva/carga-masiva.component';
import {ROUTES_PATH} from './app.route-names';
import {DocumentosTramiteComponent} from './ui/page-components/documentos-tramite/documentos-tramite.component';
import {RadicarSalidaComponent} from './ui/page-components/radicacion-salida/radicar-salida.component';
import {CargaMasivaDetailsComponent} from './ui/page-components/carga-masiva/components/cm-details.component';
import {DistribucionFisicaComponent} from './ui/page-components/distribucion-fisica/distribucion-fisica.component';
import {CargarPlanillasComponent} from './ui/page-components/cargar-planillas/cargar-planillas.component';
import {ProduccionDocumentalComponent} from './ui/page-components/produccion-documental/produccion-documental.component';
import {ProduccionDocumentalMultipleComponent} from './ui/page-components/produccion-documental/produccion-documental-multiple.component';
import {SeleccionarUnidadDocumentalComponent} from './ui/page-components/organizacion-archivo/archivar-documento/components/seleccionar-unidad-documental/seleccionar-unidad-documental.component';
import {SecurityRoleComponent} from './ui/page-components/security-role/security-role.component';
import {UnidadesDocumentalesComponent} from './ui/page-components/unidades-documentales/unidades-documentales.component';
import {GestionarDevolucionesComponent} from './ui/page-components/gestionar-devoluciones/gestionar-devoluciones.component';
import {CorregirRadicacionComponent} from './ui/page-components/corregir-radicacion/corregir-radicacion.component';
import {RedirectSeleccionarDocumentoComponent} from "./ui/page-components/organizacion-archivo/redirect-seleccionar-documento/redirect-seleccionar-documento.component";
import {CrearUnidadDocumentalComponent} from "./ui/page-components/organizacion-archivo/crear-unidad-documental/crear-unidad-documental.component";
import {DisposicionFinalComponent} from './ui/page-components/disposicion-final/disposicion-final.component';
import {RadicarDocumentoProducidoComponent} from "./ui/page-components/radicacion-salida/radicar-documento-producido.component";
import {ArchivarDocumentoComponent} from "./ui/page-components/organizacion-archivo/archivar-documento/archivar-documento.component";
import {DistribucionComponent} from "./ui/page-components/radicacion-salida/components/distribucion/distribucion.component";
import {TransferenciasDocumentalesComponent} from './ui/page-components/transferencias-documentales/transferencias-documentales.component'
import { DistribucionFisicaSalidaComponent } from './ui/page-components/distribucion-fisica-salida/distribucion-fisica-salida.component';

export const routes: Routes = [
  {path: '', redirectTo: ROUTES_PATH.dashboard, pathMatch: 'full'},
  {path: ROUTES_PATH.login, component: LoginComponent},
  {path: ROUTES_PATH.dashboard, component: HomeComponent, canActivate: [AuthenticatedGuard]},
  {
    path: ROUTES_PATH.task,
    canActivate: [AuthenticatedGuard],
    children: [
      {
        path: ROUTES_PATH.radicarCofEntrada,
        component: RadicarComunicacionesComponent,
        canDeactivate: [TareaDtoGuard]
      },
      {
        path: ROUTES_PATH.radicarCofSalida,
        component: RadicarSalidaComponent,
        canDeactivate: [TareaDtoGuard]
      },
      {
        path: ROUTES_PATH.radicarDocumentoSalida,
        component: RadicarDocumentoProducidoComponent,
        canDeactivate: [TareaDtoGuard]
      },
      {
        path: ROUTES_PATH.digitalizarDocumento,
        component: DigitalizarDocumentoComponent,
        canActivate: [AuthenticatedGuard],
        canDeactivate: [TareaDtoGuard]
      },
      {
        path: ROUTES_PATH.adjuntarDocumento,
        component: DigitalizarDocumentoComponent,
        canActivate: [AuthenticatedGuard],
        canDeactivate: [TareaDtoGuard]
      },
      {
        path: ROUTES_PATH.documentosTramite,
        component: DocumentosTramiteComponent,
        canActivate: [AuthenticatedGuard]
      },
      {
        path: ROUTES_PATH.cargarPlanillas,
        component: CargarPlanillasComponent,
        canActivate: [AuthenticatedGuard]
      },
      {
        path: ROUTES_PATH.produccionDocumentalMultiple,
        component: ProduccionDocumentalMultipleComponent,
        canActivate: [AuthenticatedGuard]
      },
      {
        path: ROUTES_PATH.produccionDocumental + '/:status',
        component: ProduccionDocumentalComponent,
        canActivate: [AuthenticatedGuard]
      },
      {
        path: ROUTES_PATH.gestionarDevoluciones,
        component: GestionarDevolucionesComponent,
        canActivate: [AuthenticatedGuard]
      },
      {
        path: ROUTES_PATH.corregirRadicacion,
        component: CorregirRadicacionComponent,
        canActivate: [AuthenticatedGuard]
      },

      {
        path: ROUTES_PATH.gestionUnidadDocumental,
        component: UnidadesDocumentalesComponent,
        canActivate: [AuthenticatedGuard]
      },
      {
        path: ROUTES_PATH.archivarDocumento,
        component: ArchivarDocumentoComponent,
        canActivate:[AuthenticatedGuard]
      },
      {
        path: ROUTES_PATH.crearUnidadDocumental,
        component: CrearUnidadDocumentalComponent,
        canActivate:[AuthenticatedGuard]
      },
      {
      	path: ROUTES_PATH.completarDatosDistribucion,
        component: DistribucionComponent,
        canActivate: [AuthenticatedGuard]
      },
      {
        path: ROUTES_PATH.transferenciasDocumentales + '/:status',
        component: TransferenciasDocumentalesComponent,
        canActivate: [AuthenticatedGuard]
      },
      {
        path: ROUTES_PATH.generarPlanillaSalida,
        component: DistribucionFisicaSalidaComponent,
        canActivate: [AuthenticatedGuard]
      },
      {
        path: ROUTES_PATH.cargarPlanillaSalida,
        component: DistribucionFisicaSalidaComponent,
        canActivate: [AuthenticatedGuard]
      },

    ]
  },
  {path: ROUTES_PATH.workspace, component: WorkspaceComponent, canActivate: [AuthenticatedGuard]},
  {path: ROUTES_PATH.processList, component: ProcessComponent, canActivate: [AuthenticatedGuard]},
  {
    path: ROUTES_PATH.asignacionComunicaciones,
    component: AsignarComunicacionesComponent,
    canActivate: [AuthenticatedGuard]
  },
  {
    path: ROUTES_PATH.radicarCofSalida,
    component: RadicarSalidaComponent,
    canActivate: [AuthenticatedGuard]
  },
  {
    path: ROUTES_PATH.cargaMasiva,
    canActivate: [AuthenticatedGuard],
    component: CargaMasivaComponent
  },
  {
    path: ROUTES_PATH.cargaMasivaDetails,
    component: CargaMasivaDetailsComponent,
    canActivate: [AuthenticatedGuard]
  },
  {
    path: ROUTES_PATH.distribucionFisica,
    component: DistribucionFisicaComponent,
    canActivate: [AuthenticatedGuard]
  },
  {
    path: ROUTES_PATH.archivarDocumento,
    component: RedirectSeleccionarDocumentoComponent,
    canActivate: [AuthenticatedGuard]
  },
  {
    path: ROUTES_PATH.seleccionarUnidadDocumental,
    component: SeleccionarUnidadDocumentalComponent,
    canActivate: [AuthenticatedGuard]
  },
  {
    path: ROUTES_PATH.securityRole,
    component: SecurityRoleComponent
  },
  {
    path: ROUTES_PATH.disposicionFinal,
    component: DisposicionFinalComponent,
    canActivate: [AuthenticatedGuard]
  },
];

export const AppRoutes: ModuleWithProviders = RouterModule.forRoot(routes);

