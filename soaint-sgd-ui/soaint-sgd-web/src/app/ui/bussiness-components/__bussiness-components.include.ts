import {DatosGeneralesComponent} from './datos-generales/datos-generales.component';
import {DatosDestinatarioComponent} from './datos-destinatario/datos-destinatario.component';
import {DatosRemitenteComponent} from './datos-remitente/datos-remitente.component';

/**
 * Presentational components receieve data through @Input() and communicate events
 * through @Output() but generally maintain no internal state of their
 * own. All decisions are delegated to 'container', or 'smart'
 * components before data updates flow back down.
 *
 * More on 'smart' and 'presentational' components: https://gist.github.com/btroncone/a6e4347326749f938510#utilizing-container-components
 */

export const BUSSINESS_COMPONENTS = [
  DatosGeneralesComponent,
  DatosRemitenteComponent,
  DatosDestinatarioComponent
];

export * from './__bussiness-providers.include';
