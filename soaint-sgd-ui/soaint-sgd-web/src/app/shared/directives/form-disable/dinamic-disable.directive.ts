import {Directive, Host, Input, OnChanges, OnInit, Optional, SkipSelf} from '@angular/core';
import {AbstractControl, ControlContainer, FormGroup} from '@angular/forms';

@Directive({
  selector: '[formControlName][dynamicDisable]'
})
export class DynamicDisableDirective implements OnInit, OnChanges {

  @Input() formControlName: string;
  @Input() dynamicDisable: boolean;

  private ctrl: AbstractControl;

  constructor(@Optional() @Host() @SkipSelf() private parent: ControlContainer,) {

  }

  ngOnInit() {
    if (this.parent && this.parent['form']) {
      this.ctrl = (<FormGroup>this.parent['form']).get(this.formControlName);
    }
  }

  ngOnChanges() {
    if (!this.ctrl) {
      return;
    }

    if (this.dynamicDisable) {
      this.ctrl.disable();
    } else {
      this.ctrl.enable();
    }
  }
}
