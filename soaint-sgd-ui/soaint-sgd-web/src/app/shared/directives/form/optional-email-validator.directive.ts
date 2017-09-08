import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator, Validators} from '@angular/forms';
import {Directive, Input} from '@angular/core';

@Directive({
  selector: '[emailOptional][formControlName],[emailOptional][formControl],[emailOptional][ngModel]',
  providers: [
    {provide: NG_VALIDATORS, useExisting: OptionalEmailValidatorDirective, multi: true}
  ]
})
export class OptionalEmailValidatorDirective implements Validator {
  private _enabled: boolean;
  private _onChange: () => void;

  @Input()
  set emailOptional(value: boolean | string) {
    this._enabled = value === '' || value === true || value === 'true';
    if (this._onChange) this._onChange();
  }

  validate(c: AbstractControl): ValidationErrors | null {
    return this._enabled && c.value ? Validators.email(c) : null;
  }

}
