// import {Directive, Host, Input, OnChanges, OnInit, Optional, SkipSelf} from '@angular/core';
// import {AbstractControl, ControlContainer, FormGroup} from '@angular/forms';
//
// /**
//  *  Utilizar en Combinación con el atributo [hidden], no funciona con *ngIf
//  * */
// @Directive({
//   selector: '[formControlName][validationErrors]'
// })
// export class ValidationErrorsDirective implements OnInit {
//
//   @Input() formControlName: string;
//
//   private ctrl: AbstractControl;
//
//   constructor(@Optional() @Host() @SkipSelf() private parent: ControlContainer) {
//
//   }
//
//   ngOnInit() {
//
//     if (this.parent && this.parent['form']) {
//       this.ctrl = (<FormGroup>this.parent['form']).get(this.formControlName);
//     }
//   }
//
//   get dynamicDisable(): boolean { // getter, not needed, but here only to completude
//     return !!this.ctrl && this.ctrl.disabled;
//   }
//
//   @Input('validationErrors') set dynamicDisable(s: boolean) {
//     if (!this.ctrl) {
//       return;
//     }
//
//     const ac = this.ctrl;
//     ac.valueChanges.subscribe(value => {
//       if ((ac.touched || ac.dirty) && ac.errors) {
//         const error_keys = Object.keys(ac.errors);
//         const last_error_key = error_keys[error_keys.length - 1];
//         this.validations[this.ctrl.] = VALIDATION_MESSAGES[last_error_key];
//       } else {
//         delete this.validations[control];
//       }
//     });
//
//     if (!this.dynamicDisable) {
//       this.ctrl.disable();
//     } else {
//       this.ctrl.enable();
//     }
//   }
//
//   // ngOnChanges() {
//   //   debugger;
//   //   if (!this.ctrl) {
//   //     return;
//   //   }
//   //
//   //   if (this.dynamicDisable) {
//   //     this.ctrl.disable();
//   //   } else {
//   //     this.ctrl.enable();
//   //   }
//   // }
// }
