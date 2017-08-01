import {Component, OnInit} from '@angular/core';
import {State as RootState} from '../../../../infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';


@Component({
  moduleId: module.id,
  selector: 'growl-messages',
  templateUrl: 'growl-messages.component.html'
})

export class GrowlMessagesComponent implements OnInit {

  // messages$: Observable<any[]>;

  constructor(private _store: Store<RootState>) {
  }

  ngOnInit() {
    console.log('GrowlMessagesComponent');
    // this.messages$ = this._store.withLatestFrom(getNotificationArrayData);
  }
}
