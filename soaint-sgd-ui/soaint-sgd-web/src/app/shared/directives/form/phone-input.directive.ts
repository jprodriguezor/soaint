import {Directive, HostListener, ElementRef, OnInit} from '@angular/core';
import {CountryPhonePipe} from '../../pipes/phone-input.pipe';

@Directive({
  selector: '[formControlName][phoneInput]'
})
export class PhoneInputDirective implements OnInit {

  private el: HTMLInputElement;

  constructor(private elementRef: ElementRef,
              private phonePipe: CountryPhonePipe) {
    this.el = this.elementRef.nativeElement;
  }

  ngOnInit() {
    // this.el.value = this.phonePipe.transform(this.el.value);
  }

  @HostListener('blur', ['$event.target.value'])
  onBlur(value) {
    this.el.value = this.phonePipe.transform(value);
  }

}
