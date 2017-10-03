import {Directive, Host, Input, OnChanges, OnInit, Optional, SkipSelf} from '@angular/core';
import {AbstractControl, ControlContainer, FormGroup} from '@angular/forms';

/**
 *  Utilizar en Combinación con el atributo [hidden], no funciona con *ngIf
 * */
@Directive({
  selector: '[formControlName][dynamicDisable]'
})
export class DynamicDisableDirective implements OnInit {

  @Input() formControlName: string;

  private ctrl: AbstractControl;

  constructor(@Optional() @Host() @SkipSelf() private parent: ControlContainer) {

  }

  ngOnInit() {
    if (this.parent && this.parent['form']) {
      this.ctrl = (<FormGroup>this.parent['form']).get(this.formControlName);
    }
  }

  get dynamicDisable(): boolean { // getter, not needed, but here only to completude
    return !!this.ctrl && this.ctrl.disabled;
  }

  @Input('dynamicDisable') set dynamicDisable(s: boolean) {
    if (!this.ctrl) {
      return;
    }
    if (s) {
      this.ctrl.reset();
      this.ctrl.disable();
    } else {
      this.ctrl.enable();
    }
  }
}
