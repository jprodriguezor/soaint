import { NgModule } from '@angular/core';

import { EllipsisPipe } from './ellipsis';
import { DropdownItemPipe } from './dropdown-item';
import { DropdownSingleItemPipe } from './dropdown-single-item';

export const PIPES = [
  EllipsisPipe,
  DropdownItemPipe,
  DropdownSingleItemPipe
];

@NgModule({
  declarations: PIPES,
  exports: PIPES
})
export class PipesModule { }
