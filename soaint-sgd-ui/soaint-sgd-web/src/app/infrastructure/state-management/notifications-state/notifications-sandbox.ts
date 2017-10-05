import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './notifications-actions';
import {ToastrService} from 'ngx-toastr';
import {CustomNotification} from './notifications-reducers';
import * as statusCodes from 'app/shared/bussiness-properties/error-codes.properties';
import * as statusMessages from 'app/shared/bussiness-properties/error-messages.properties';

@Injectable()
export class Sandbox {

  constructor(private notify: ToastrService) {
  }

  showNotification(notification: CustomNotification) {
    switch (notification.severity) {
      case 'info':
        return this.notify.info(notification.detail, notification.summary, notification.options);
      case 'success':
        return this.notify.success(notification.detail, notification.summary, notification.options);
      case 'warn':
        return this.notify.warning(notification.detail, notification.summary, notification.options);
      case 'error':
        return this.notify.error(notification.detail, notification.summary, notification.options);
      default:
        return this.showConnectionError(notification.summary)
    }

  }

  showConnectionError(code) {
    return this.notify.error('', this.getStatusMessage(code));
  }

  hideNotification(id) {
    this.notify.clear(id);
  }

  getStatusMessage(statusCode) {
    switch (statusCode) {
      case statusCodes.BAD_GATEWAY: return statusMessages.BAD_GATEWAY;
      case statusCodes.NOT_AUTHORIZED: return statusMessages.NOT_AUTHORIZED;
      case statusCodes.SERVER_ERROR: return statusMessages.SERVER_ERROR;
      case statusCodes.GATEWAY_TIMEOUT: return statusMessages.GATEWAY_TIMEOUT;
      case statusCodes.NOT_FOUND: return statusMessages.NOT_FOUND;
      case statusCodes.CUSTOMER_NOT_SUPPORTED: return statusMessages.CUSTOMER_NOT_SUPPORTED;
      case statusCodes.BAD_REQUEST: return statusMessages.CUSTOMER_NOT_SUPPORTED;
      default:
        return statusMessages.SERVER_ERROR;
    }
  }

}

