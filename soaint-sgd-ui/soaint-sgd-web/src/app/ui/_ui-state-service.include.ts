import {LAYOUT_COMPONENTS} from './layout-components/__layout-components.include';
import {BUSSINESS_COMPONENTS} from './bussiness-components/__bussiness-components.include';
import {PAGE_COMPONENTS} from './page-components/__page-components.include';
import { DatosGeneralesStateService } from './bussiness-components/datos-generales-edit/datos-generales-state-service';
import { DatosRemitenteStateService } from './bussiness-components/datos-remitente-edit/datos-remitente-state-service';
import { DatosDestinatarioStateService } from './bussiness-components/datos-destinatario-edit/datos-destinatario-state-service';
import { StateUnidadDocumentalService } from './page-components/unidades-documentales/state.unidad.documental';

export const UI_STATE_SERVICES = [
    DatosGeneralesStateService,
    DatosRemitenteStateService,
    DatosDestinatarioStateService,
    StateUnidadDocumentalService,
];

