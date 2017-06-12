import {LAYOUT_COMPONENTS} from './layout-components/__layout-components.include';
import {BUSSINESS_COMPONENTS} from './bussiness-components/__bussiness-components.include';
import {PAGE_COMPONENTS} from './page-components/__page-components.include';

export const UI_COMPONENTS = [
  ...LAYOUT_COMPONENTS,
  ...BUSSINESS_COMPONENTS,
  ...PAGE_COMPONENTS
];

export * from './page-components/__page-components.include';
export * from './layout-components/__layout-components.include';
export * from './bussiness-components/__bussiness-components.include';

