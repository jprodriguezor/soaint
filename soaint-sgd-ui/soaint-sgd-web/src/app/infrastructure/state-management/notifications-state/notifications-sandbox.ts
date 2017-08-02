import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './notifications-actions';
import {ToastrService} from 'ngx-toastr';
import {CustomNotification} from './notifications-reducers';

@Injectable()
export class Sandbox {

  constructor(private notify: ToastrService) {
  }

  showNotification(notification: CustomNotification) {
    switch (notification.severity) {
      case 'info':
        return this.notify.info(notification.summary, notification.detail);
      case 'success':
        return this.notify.success(notification.summary, notification.detail);
      case 'warn':
        return this.notify.warning(notification.summary, notification.detail);

      default:
        return this.notify.error(notification.summary, notification.detail);
    }

  }

}

