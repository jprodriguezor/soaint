import {FooterComponent} from './presentation/footer/footer.component';
import {AppMenuComponent, AppSubMenuComponent} from './presentation/menu/menu.component';
import {InlineProfileComponent} from './presentation/profile/inline-profile.component';
import {TopBarComponent} from './presentation/topbar/topbar.component';

import {AdminLayoutComponent} from './container/admin-layout/admin-layout.component';



/**
 * Presentational components receieve data through @Input() and communicate events
 * through @Output() but generally maintain no internal state of their
 * own. All decisions are delegated to 'container', or 'smart'
 * components before data updates flow back down.
 *
 * More on 'smart' and 'presentational' components: https://gist.github.com/btroncone/a6e4347326749f938510#utilizing-container-components
 */
export const LAYOUT_COMPONENTS = [
  FooterComponent,
  AppMenuComponent,
  AppSubMenuComponent,
  InlineProfileComponent,
  TopBarComponent,
  AdminLayoutComponent
];

export * from './__layout-providers.include';
