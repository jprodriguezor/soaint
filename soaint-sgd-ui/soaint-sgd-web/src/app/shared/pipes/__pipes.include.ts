// import {NgModule} from '@angular/core';
//
import {EllipsisPipe} from './ellipsis';
import {DropdownItemPipe, DropdownItemPipeFullName} from './dropdown-item';
import {DropdownSingleItemPipe} from './dropdown-single-item';
import {ConstantCodePipe} from './constant-code-pipe.pipe';
import { CountryPhonePipe } from './countryPhone-input.pipe';
import { MobilePhonePipe } from './mobile-input.pipe';

export const PIPES = [
  EllipsisPipe,
  DropdownItemPipe,
  DropdownSingleItemPipe,
  DropdownItemPipeFullName,
  ConstantCodePipe,
  CountryPhonePipe,
  MobilePhonePipe
];

export const PIPES_AS_PROVIDERS = [
  CountryPhonePipe,
  MobilePhonePipe
];
//
// @NgModule({
//   declarations: PIPES,
//   exports: PIPES
// })
// export class PipesModule {
// }
