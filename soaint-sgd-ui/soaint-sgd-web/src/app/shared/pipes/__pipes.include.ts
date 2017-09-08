// import {NgModule} from '@angular/core';
//
import {EllipsisPipe} from './ellipsis';
import {DropdownItemPipe, DropdownItemPipeFullName} from './dropdown-item';
import {DropdownSingleItemPipe} from './dropdown-single-item';
import {ConstantCodePipe} from './constant-code-pipe.pipe';
import {CountryPhonePipe} from './phone-input.pipe';

export const PIPES = [
  EllipsisPipe,
  DropdownItemPipe,
  DropdownSingleItemPipe,
  DropdownItemPipeFullName,
  ConstantCodePipe,
  CountryPhonePipe
];
//
// @NgModule({
//   declarations: PIPES,
//   exports: PIPES
// })
// export class PipesModule {
// }
