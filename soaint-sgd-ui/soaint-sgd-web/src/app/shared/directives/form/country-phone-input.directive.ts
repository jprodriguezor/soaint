import { Directive, HostListener, ElementRef, OnInit, Input } from '@angular/core';
import { CountryPhonePipe } from '../../pipes/countryPhone-input.pipe';
import { AbstractControl, NG_VALIDATORS, ValidationErrors } from '@angular/forms';

@Directive({
  selector: '[formControlName][countryPhoneInput]',
  providers: [
    {provide: NG_VALIDATORS, useExisting: CountryPhoneDirective, multi: true}
  ]
})
export class CountryPhoneDirective implements OnInit {

  private el: HTMLInputElement;
  private _enabled: boolean;
  private _onChange: () => void;

  constructor(private elementRef: ElementRef,
              private phonePipe: CountryPhonePipe) {
    this.el = this.elementRef.nativeElement;
    console.log('[formControlName][countryPhoneInput]');
  }

  ngOnInit() {
    if (this.el.value) {
      this.el.value = this.phonePipe.transform(this.el.value);
    }
  }

  @HostListener('blur', ['$event.target.value'])
  onBlur(value) {
    if (this.el.value) {
      this.el.value = this.phonePipe.transform(value);
    }
  }

  @Input()
  set countryPhoneInput(value: boolean | string) {
    this._enabled = value === '' || value === true || value === 'true';
    if (this._onChange) this._onChange();
  }

  validate(c: AbstractControl): ValidationErrors | null {
    return this._enabled && c.value ? this.isValid(c.value) : null;
  }

  isValid(value: string): ValidationErrors | null {
    const cleaned = value.replace(/\D/g, '');
    if (cleaned.length === 12 || cleaned.length === 7 || cleaned.length === 9) {
      return null;
    }

    return {
      'countryPhone': true
    }

  }

}
