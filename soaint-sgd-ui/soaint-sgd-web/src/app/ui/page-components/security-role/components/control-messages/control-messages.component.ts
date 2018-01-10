import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { ValidationService } from './validation.service';

@Component({
  selector: 'control-messages',
  template: `<div class="ui-message ui-messages-error" *ngIf="errorMessage !== null">{{errorMessage}}</div>`
})
export class ControlMessagesComponent implements OnInit {
  @Input() control: FormControl;
  constructor() { }
  ngOnInit() {
  }
  get errorMessage() {
    for (let propertyName in this.control.errors) {
      if (this.control.errors.hasOwnProperty(propertyName) && this.control.touched) {
        return ValidationService.getValidatorErrorMessage(propertyName, this.control.errors[propertyName]);
      }
    }

    return null;
  }
}
