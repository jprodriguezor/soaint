import {EventEmitter, Injectable} from '@angular/core';

@Injectable()
export class LoadingService {

  private loading: EventEmitter<boolean> = new EventEmitter(true);

  constructor() {

  }

  presentLoading() {
    this.loading.next(true);
  }

  dismissLoading() {
    this.loading.next(false);
  }

  getLoaderAsObservable() {
    return this.loading.asObservable();
  }
}
