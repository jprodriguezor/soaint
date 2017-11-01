import { Directive, ElementRef, HostListener, Input, OnInit } from '@angular/core';
import { MobilePhonePipe } from '../../pipes/mobile-input.pipe';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator } from '@angular/forms';

@Directive({
  selector: '[formControlName][mobilePhoneInput]',
  providers: [
    {provide: NG_VALIDATORS, useExisting: MobilePhoneDirective, multi: true}
  ]
})
export class MobilePhoneDirective implements Validator, OnInit {

  private el: HTMLInputElement;
  private _enabled: boolean;
  private _onChange: () => void;

  constructor(private elementRef: ElementRef,
              private phonePipe: MobilePhonePipe) {
    this.el = this.elementRef.nativeElement;
    console.log('[formControlName][mobilePhoneInput]');
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
  set mobilePhoneInput(value: boolean | string) {
    this._enabled = value === '' || value === true || value === 'true';
    if (this._onChange) this._onChange();
  }

  validate(c: AbstractControl): ValidationErrors | null {
    return this._enabled && c.value ? this.isValid(c.value) : null;
  }

  isValid(v: string): ValidationErrors | null {
    const cleaned = v.replace(/\D/g, '');
    if (cleaned.length === 10) {
      return null;
    }
    return {
      'mobilePhone': true
    }
  }

}
