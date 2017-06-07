import {
  Component,
  Input,
  Output,
  OnInit,
  EventEmitter,
  ViewChild
} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';


@Component({
  selector: 'inline-profile',
  templateUrl: './inline-profile.component.html',
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
export class InlineProfileComponent implements OnInit {

  active: boolean;

  @Output()
  onLogout: EventEmitter<any>;

  constructor() {
  }

  ngOnInit() {
    this.onLogout = new EventEmitter();
  }

  public logout(): void {
    this.onLogout.emit();
  }

  public onClick(event): void {
    this.active = !this.active;
    event.preventDefault();
  }
}
