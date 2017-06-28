import { NgModule } from '@angular/core';

import { EllipsisPipe } from './ellipsis';
import { DropdownItemPipe } from './dropdown-item';

export const PIPES = [
  EllipsisPipe,
  DropdownItemPipe
];

@NgModule({
  declarations: PIPES,
  exports: PIPES
})
export class PipesModule { }
