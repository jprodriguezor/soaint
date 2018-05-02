import { DatosGeneralesStateService } from 'app/ui/bussiness-components/datos-generales-edit/datos-generales-state-service';
import { DatosRemitenteStateService } from 'app/ui/bussiness-components/datos-remitente-edit/datos-remitente-state-service';
import { DatosDestinatarioStateService } from 'app/ui/bussiness-components/datos-destinatario-edit/datos-destinatario-state-service';
import { StateUnidadDocumentalService } from 'app/infrastructure/service-state-management/state.unidad.documental';

export const UI_STATE_SERVICES = [
    DatosGeneralesStateService,
    DatosRemitenteStateService,
    DatosDestinatarioStateService,
    StateUnidadDocumentalService,
];

