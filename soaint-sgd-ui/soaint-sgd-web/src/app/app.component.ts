import {Component, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {LoadingService} from './infrastructure/utils/loading.service';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  isLoading: boolean;
  loading$: Observable<boolean>;
  constructor(private _store: Store<RootState>, private loading: LoadingService) {
    this.loading$ = this.loading.getLoaderAsObservable();

    this.loading$.subscribe(value => {
      this.isLoading = value;
    });
  }

  ngOnInit() {

  }

}
