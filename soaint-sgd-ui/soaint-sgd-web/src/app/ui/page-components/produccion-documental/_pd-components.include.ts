import {ProduccionDocumentalComponent} from './produccion-documental.component';
import {PDDatosGeneralesComponent} from './components/datos-generales/datos-generales.component';
import {PDGestionarProduccionComponent} from './components/gestionar-produccion/gestionar-produccion.component';
import {PDDatosContactoComponent} from './components/datos-contacto/datos-contacto.component';
import {ProduccionDocumentalMultipleComponent} from './produccion-documental-multiple.component';
import {AdjuntarDocumentosComponent} from "./components/adjuntar-documentos/adjuntar-documentos.component";

export const PRODUCCION_DOCUMENTAL_COMPONENTS = [
    ProduccionDocumentalMultipleComponent,
    ProduccionDocumentalComponent,
    PDDatosGeneralesComponent,
    PDDatosContactoComponent,
    PDGestionarProduccionComponent,
    AdjuntarDocumentosComponent
];
