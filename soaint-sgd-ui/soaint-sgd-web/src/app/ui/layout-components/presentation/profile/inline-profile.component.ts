import {
  Component,
  Output,
  OnInit,
  EventEmitter, ChangeDetectionStrategy,
} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';


@Component({
  selector: 'inline-profile',
  templateUrl: './inline-profile.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  animations: [
    trigger('menu', [
      state('hidden', style({
        height: '0px'
      })),
      state('visible', style({
        height: '*'
      })),
      transition('visible => hidden', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)')),
      transition('hidden => visible', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)'))
    ])
  ]
})
export class InlineProfileComponent {

  active: boolean;

  @Output() onSignOffUser: EventEmitter<any> = new EventEmitter();

  constructor() {
  }

  public logout(event): void {
     this.onSignOffUser.emit(null);
      event.preventDefault();
  }

  public onClick(event): void {
    this.active = !this.active;
    event.preventDefault();
  }
}