import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {

  constructor(private _store: Store<RootState>) {
  }

  ngOnInit() {

  }

}
