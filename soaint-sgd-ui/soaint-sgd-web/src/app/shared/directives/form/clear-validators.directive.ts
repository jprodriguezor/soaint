import {Directive, Host, Input, OnChanges, OnInit, Optional, SkipSelf} from '@angular/core';
import {AbstractControl, ControlContainer, FormGroup} from '@angular/forms';


@Directive({
  selector: '[formControlName][clearValidators]'
})
export class ClearValidatorsDirective implements OnInit {

  @Input() formControlName: string;

  private ctrl: AbstractControl;

  constructor(@Optional() @Host() @SkipSelf() private parent: ControlContainer) {

  }

  ngOnInit() {
    if (this.parent && this.parent['form']) {
      this.ctrl = (<FormGroup>this.parent['form']).get(this.formControlName);
    }
  }

  @Input('clearValidators')
  set clearValidators(check: boolean) {

    if (!this.ctrl) {
      return;
    }

    if (check) {
      (<any>this.ctrl).__validator = this.ctrl.validator || (<any>this.ctrl).__validator;
      this.ctrl.clearValidators();
    } else {
      this.ctrl.setValidators(this.ctrl.validator || (<any>this.ctrl).__validator);
    }

    this.ctrl.updateValueAndValidity();
  }
}
