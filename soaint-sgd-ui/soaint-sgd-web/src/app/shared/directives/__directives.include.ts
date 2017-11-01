import { PrintDirective } from './print/print.directive';
import { DynamicDisableDirective } from './form/dinamic-disable.directive';
import { ClearValidatorsDirective } from './form/clear-validators.directive';
import { CountryPhoneDirective } from './form/country-phone-input.directive';
import { MobilePhoneDirective } from './form/mobile-phone-input.directive';
import { OptionalEmailValidatorDirective } from './form/optional-email-validator.directive';

export const DIRECTIVES = [
  PrintDirective,
  DynamicDisableDirective,
  ClearValidatorsDirective,
  MobilePhoneDirective,
  CountryPhoneDirective,
  OptionalEmailValidatorDirective
];
